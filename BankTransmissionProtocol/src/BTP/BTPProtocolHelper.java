/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public abstract class BTPProtocolHelper {

    private BTPSystem system;
    private BufferedReader input;
    private PrintStream output;
    private BTPClient client;

    public BTPProtocolHelper(BTPSystem system, BTPClient client, BufferedReader input, PrintStream output) {
        this.system = system;
        this.input = input;
        this.output = output;
        this.client = client;
    }

    protected BTPSystem getSystem() {
        return this.system;
    }

    protected BufferedReader getBufferedReader() {
        return this.input;
    }

    protected PrintStream getPrintStream() {
        return this.output;
    }

    public BTPAccount readAccountFromSocket(boolean bare_minimum) throws IOException {
        BTPAccountType account_type = null;
        BTPKeyContainer extra = null;

        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        int account_no = Integer.parseInt(this.getBufferedReader().readLine());
        String sort_code = this.getBufferedReader().readLine();

        if (!bare_minimum) {
            int total_options = this.getBufferedReader().read();
            extra = new BTPKeyContainer();
            for (int b = 0; b < total_options; b++) {
                BTPKey key = new BTPKey(
                        this.getBufferedReader().readLine(),
                        this.getBufferedReader().readLine()
                );
                extra.addKey(key);
            }

            account_type = new BTPAccountType(this.getBufferedReader().read(),
                    this.getBufferedReader().readLine());
        }
        return new BTPAccount(customer_id, account_no, sort_code, account_type, extra);
    }

    public BTPAccount readAccountFromSocket() throws IOException {
        return this.readAccountFromSocket(false);
    }

    public void writeAccountToSocket(BTPAccount account, boolean bare_minimum) {
        this.getPrintStream().println(account.getCustomerId());
        this.getPrintStream().println(account.getAccountNumber());
        this.getPrintStream().println(account.getSortCode());
        if (!bare_minimum) {
            this.getPrintStream().write(account.getExtraDetail().getTotalKeys());
            for (int i = 0; i < account.getExtraDetail().getTotalKeys(); i++) {
                BTPKey key = account.getExtraDetail().getKey(i);
                this.getPrintStream().println(key.getIndexName());
                this.getPrintStream().println(key.getValue());
            }

            this.getPrintStream().write(account.getAccountType().getId());
            this.getPrintStream().println(account.getAccountType().getName());
        }
        this.getPrintStream().flush();
    }

    public void writeAccountToSocket(BTPAccount account) {
        this.writeAccountToSocket(account, false);
    }

    public void writeTransactionToSocket(BTPTransaction transaction) {
        this.writeAccountToSocket(transaction.getSenderAccount());
        this.writeAccountToSocket(transaction.getReceiverAccount());
        this.getPrintStream().println(transaction.getAmountTransferred());
        this.getPrintStream().println(Long.toString(transaction.getDate().getTime()));
        this.getPrintStream().flush();
    }

    public BTPTransaction readTransactionFromSocket() throws IOException {
        BTPAccount account_from = this.readAccountFromSocket();
        BTPAccount account_to = this.readAccountFromSocket();
        double amount = Double.valueOf(this.getBufferedReader().readLine());
        Date date = new Date();
        date.setTime(Long.parseLong(this.getBufferedReader().readLine()));
        return new BTPTransaction(account_from, account_to, amount, date);
    }

    public void writeCustomerToSocket(BTPCustomer customer) throws IOException {
        this.getPrintStream().println(Integer.toString(customer.getId()));
        this.getPrintStream().println(customer.getTitle());
        this.getPrintStream().println(customer.getFirstname());
        this.getPrintStream().println(customer.getMiddlename());
        this.getPrintStream().println(customer.getSurname());
        this.writeKeyContainerToSocket(customer.getExtraDetail());
    }

    public BTPCustomer readCustomerFromSocket() throws IOException {
        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        String customer_title = this.getBufferedReader().readLine();
        String customer_firstname = this.getBufferedReader().readLine();
        String customer_middlename = this.getBufferedReader().readLine();
        String customer_surname = this.getBufferedReader().readLine();
        BTPKeyContainer extra;

        // Read in the extra information
        extra = this.readKeyContainerFromSocket();
        return new BTPCustomer(
                customer_id,
                customer_title,
                customer_firstname,
                customer_middlename,
                customer_surname,
                extra);
    }

    public void writeKeyContainerToSocket(BTPKeyContainer container) {
        this.getPrintStream().println(Integer.toString(container.getTotalKeys()));
        for (int i = 0; i < container.getTotalKeys(); i++) {
            BTPKey key = container.getKey(i);
            this.getPrintStream().println(key.getIndexName());
            this.getPrintStream().println(key.getValue());
        }
    }

    public BTPKeyContainer readKeyContainerFromSocket() throws IOException {
        BTPKeyContainer container = new BTPKeyContainer();
        int total_keys = Integer.parseInt(this.getBufferedReader().readLine());
        for (int i = 0; i < total_keys; i++) {
            container.addKey(new BTPKey(this.getBufferedReader().readLine(),
                    this.getBufferedReader().readLine()));
        }
        return container;
    }

    protected BTPClient getClient() {
        return this.client;
    }
}
