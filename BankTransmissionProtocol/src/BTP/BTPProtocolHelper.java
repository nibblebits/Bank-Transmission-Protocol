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

/**
 *
 * @author Daniel
 */
public abstract class BTPProtocolHelper {
    private BTPSystem system;
    private BufferedReader input;
    private PrintStream output;
    public BTPProtocolHelper(BTPSystem system, BufferedReader input, PrintStream output) {
        this.system = system;
        this.input = input;
        this.output = output;
    }
    
    public BTPSystem getSystem() {
        return this.system;
    }
    
    public BufferedReader getBufferedReader() {
        return this.input;
    }
    
    public PrintStream getPrintStream() {
        return this.output;
    }
    
    protected BTPAccount readAccountFromSocket() throws IOException {
        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        int account_no = Integer.parseInt(this.getBufferedReader().readLine());
        String sort_code = this.getBufferedReader().readLine();
        int total_options = this.getBufferedReader().read();
        BTPKeyContainer extra = new BTPKeyContainer();
        for (int b = 0; b < total_options; b++) {
            BTPKey key = new BTPKey(
                    this.getBufferedReader().readLine(),
                    this.getBufferedReader().readLine()
            );
            extra.addKey(key);
        }

        return new BTPAccount(customer_id, account_no, sort_code, extra);
    }

    protected void writeAccountToSocket(BTPAccount account) {
        this.getPrintStream().println(account.getCustomerId());
        this.getPrintStream().println(account.getAccountNumber());
        this.getPrintStream().println(account.getSortCode());
        this.getPrintStream().write(account.getExtraDetail().getTotalKeys());
        for (int i = 0; i < account.getExtraDetail().getTotalKeys(); i++) {
            BTPKey key = account.getExtraDetail().getKey(i);
            this.getPrintStream().println(key.getIndexName());
            this.getPrintStream().println(key.getValue());
        }

        this.getPrintStream().flush();
    }

    protected void writeTransactionToSocket(BTPTransaction transaction) {
        this.writeAccountToSocket(transaction.getSenderAccount());
        this.writeAccountToSocket(transaction.getReceiverAccount());
        this.getPrintStream().println(transaction.getAmountTransferred());
        this.getPrintStream().flush();
    }

    protected BTPTransaction readTransactionFromSocket() throws IOException {
        return new BTPTransaction(
                this.readAccountFromSocket(),
                this.readAccountFromSocket(),
                Double.valueOf(this.getBufferedReader().readLine()));
    }
}
