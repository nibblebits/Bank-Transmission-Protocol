/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;
import BTP.exceptions.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BTPCustomerClient extends BTPClient {

    private int customer_id = -1;

    public BTPCustomerClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);

    }

    public boolean login(int customer_id, String password) throws BTPPermissionDeniedException, BTPDataException, Exception {
        // Send the customer client authentication type.
        this.getPrintStream().write(BTPClient.Customer);
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
            this.getPrintStream().write(BTPOperation.TRANSFER);
            this.getPrintStream().println(Integer.toString(account_from.getAccountNumber()));
            this.getPrintStream().println(account_from.getSortCode());
            this.getPrintStream().println(Integer.toString(account_to.getAccountNumber()));
            this.getPrintStream().println(account_to.getSortCode());
            this.getPrintStream().println(Double.toString(amount));
            int response = this.getBufferedReader().read();
            if (response != BTPResponseCode.ALL_OK) {
                String message = this.getBufferedReader().readLine();
                this.getSystem().throwExceptionById(response, message);
            }
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be logged in to perform a transfer.");
        }
    }

    public double getBalance(BTPAccount account) throws IOException, BTPDataException, Exception {
        if (this.isAuthenticated()) {
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
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("Could not get balance of bank account: "
                    + account.getAccountNumber() + " as this client is not authenticated");
        }
    }

    public BTPTransaction[] getTransactions() {
        return null;
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
                int account_no = Integer.parseInt(this.getBufferedReader().readLine());
                int total_options = this.getBufferedReader().read();
                BTPKeyContainer extra = new BTPKeyContainer();
                for (int b = 0; b < total_options; b++) {
                    BTPKey key = new BTPKey(
                            this.getBufferedReader().readLine(),
                            this.getBufferedReader().readLine()
                    );
                    extra.addKey(key);
                }

                // This will be changed shortly their is a cleaner way of doing this.
                accounts[i] = new BTPAccount(this.customer_id, account_no, this.getSystem().getOurBank().getSortcode(), extra);
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
