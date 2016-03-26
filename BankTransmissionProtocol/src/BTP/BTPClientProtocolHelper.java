/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import BTP.exceptions.BTPUnknownException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class BTPClientProtocolHelper extends BTPProtocolHelper {

    public BTPClientProtocolHelper(BTPSystem system, BTPClient client, BufferedReader input, PrintStream output) {
        super(system, client, input, output);
    }

    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) throws BTPPermissionDeniedException, BTPDataException, Exception {
        this.getPrintStream().write(BTPOperation.TRANSFER);
        this.getPrintStream().println(Integer.toString(account_from.getAccountNumber()));
        this.writeAccountToSocket(account_to, true);
        this.getPrintStream().println(Double.toString(amount));
        int response = this.getBufferedReader().read();
        if (response != BTPResponseCode.ALL_OK) {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
    }

    public double getBalance(BTPAccount account) throws BTPPermissionDeniedException, BTPDataException, Exception {
        this.getPrintStream().write(BTPOperation.GET_BALANCE);
        this.getPrintStream().println(account.getAccountNumber());
        int response = this.getBufferedReader().read();
        if (response == BTPResponseCode.ALL_OK) {
            return Double.valueOf(this.getBufferedReader().readLine());
        } else {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }

        return 0;
    }

    public BTPTransaction[] getTransactions(BTPAccount account, Date from, Date to) throws BTPPermissionDeniedException, BTPDataException, BTPUnknownException, IOException, Exception {
        BTPTransaction[] transactions = null;
        this.getPrintStream().write(BTPOperation.GET_TRANSACTIONS);
        this.getPrintStream().println(account.getAccountNumber());
        this.getPrintStream().println(from.getTime());
        this.getPrintStream().println(to.getTime());
        int response = this.getBufferedReader().read();
        if (response == BTPResponseCode.ALL_OK) {
            int total_transactions = this.getBufferedReader().read();
            transactions = new BTPTransaction[total_transactions];

            for (int i = 0; i < total_transactions; i++) {
                transactions[i] = this.readTransactionFromSocket();
            }
        } else {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
        return transactions;
    }

    public int createCustomer(BTPCustomer customer) throws BTPPermissionDeniedException, BTPDataException, Exception {
        this.getPrintStream().write(BTPOperation.CREATE_CUSTOMER);
        this.writeCustomerToSocket(customer);
        int response = this.getBufferedReader().read();
        if (response != BTPResponseCode.ALL_OK) {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }

        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        return customer_id;
    }

    public BTPCustomer getCustomer(int customer_id)
            throws BTPPermissionDeniedException, BTPDataException, BTPAccountNotFoundException, Exception {
        this.getPrintStream().write(BTPOperation.GET_CUSTOMER);
        this.getPrintStream().println(Integer.toString(customer_id));
        int response = this.getBufferedReader().read();
        if (response == BTPResponseCode.ALL_OK) {
            return this.readCustomerFromSocket();
        } else {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
        return null;
    }

    public BTPAccount[] getBankAccountsOfCustomer(int customer_id)
            throws BTPPermissionDeniedException, BTPAccountNotFoundException,
            BTPDataException, BTPUnknownException, Exception {
        this.getPrintStream().write(BTPOperation.GET_BANK_ACCOUNTS);
        /* Only send the customer id if this is not a customer client, 
        as customers can only get bank accounts of their own customer account*/
        if (!(this.getClient() instanceof BTPCustomerClient)) {
            this.getPrintStream().println(Integer.toString(customer_id));
        }
        int response = this.getBufferedReader().read();
        if (response != BTPResponseCode.ALL_OK) {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
        int amount = this.getBufferedReader().read();
        BTPAccount[] accounts = new BTPAccount[amount];
        for (int i = 0; i < amount; i++) {
            accounts[i] = this.readAccountFromSocket();
        }
        return accounts;
    }
}
