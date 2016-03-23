/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;
import BTP.exceptions.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BTPCustomerClient extends BTPConnectorClient {

    private int customer_id = -1;

    public BTPCustomerClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);
    }

    public boolean login(int customer_id, String password) throws BTPPermissionDeniedException, BTPDataException, Exception {
        this.getPrintStream().println(Integer.toString(customer_id));
        this.getPrintStream().println(password);
        this.getPrintStream().flush();

        try {
            int response = this.getBufferedReader().read();
            if (response == BTPResponseCode.ALL_OK) {
                // Set this client as authenticated
                this.setAuthenticated(true);
                this.customer_id = customer_id;
                return true;
            } else {
                String message = this.getBufferedReader().readLine();
                this.getSystem().throwExceptionById(response, message);
            }
        } catch (IOException ex) {
            Logger.getLogger(BTPCustomerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) throws BTPPermissionDeniedException, IOException, BTPDataException, Exception {
        if (this.isAuthenticated()) {
            this.getProtocolHelper().transfer(account_from, account_to, amount);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be logged in to perform a transfer.");
        }
    }

    public double getBalance(BTPAccount account) throws BTPPermissionDeniedException, BTPDataException, IOException, Exception {
        if (this.isAuthenticated()) {
            return this.getProtocolHelper().getBalance(account);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("Could not get balance of bank account: "
                    + account.getAccountNumber() + " as this client is not authenticated");
        }
    }

    public BTPTransaction[] getTransactions(BTPAccount account, Date from, Date to) throws BTPPermissionDeniedException, IOException, Exception {
        BTPTransaction[] transactions = null;
        if (this.isAuthenticated()) {
            transactions = this.getProtocolHelper().getTransactions(account, from, to);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be authenticated to get the transactions of the bank account " + account.getAccountNumber());
        }
        return transactions;
    }

    public BTPAccount[] getBankAccounts() throws BTPPermissionDeniedException, IOException, BTPDataException, Exception {
        BTPAccount[] accounts = null;
        if (this.isAuthenticated()) {
            this.getPrintStream().write(BTPOperation.GET_BANK_ACCOUNTS);
            int response = this.getBufferedReader().read();
            if (response != BTPResponseCode.ALL_OK) {
                String message = this.getBufferedReader().readLine();
                this.getSystem().throwExceptionById(response, message);
            }
            int amount = this.getBufferedReader().read();
            accounts = new BTPAccount[amount];
            for (int i = 0; i < amount; i++) {
                accounts[i] = this.getProtocolHelper().readAccountFromSocket();
            }
            return accounts;
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be logged in to retrieve bank accounts.");
        }
    }

    public int getCustomerId() {
        return this.customer_id;
    }
}
