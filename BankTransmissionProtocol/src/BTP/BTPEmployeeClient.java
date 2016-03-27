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
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Daniel
 */
public class BTPEmployeeClient extends BTPConnectorClient {

    public BTPEmployeeClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);
    }

    public boolean login(int employee_id, String password) throws IOException, BTPPermissionDeniedException, BTPDataException, Exception {
        this.getPrintStream().println(Integer.toString(employee_id));
        this.getPrintStream().println(password);
        int response = this.getBufferedReader().read();
        if (response == BTPResponseCode.ALL_OK) {
            this.setAuthenticated(true);
            return true;
        } else {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
        return false;
    }

    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) throws BTPDataException, Exception {
        if (this.isAuthenticated()) {
            this.getProtocolHelper().transfer(account_from, account_to, amount);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be logged in to perform a transfer.");
        }
    }

    public double getBalance(BTPAccount account) throws BTPPermissionDeniedException, BTPAccountNotFoundException, BTPDataException, Exception {
        if (this.isAuthenticated()) {
            return this.getProtocolHelper().getBalance(account);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("Could not get balance of bank account: "
                    + account.getAccountNumber() + " as this client is not authenticated");
        }
    }

    public BTPTransaction[] getTransactions(BTPAccount account) {
        return null;
    }

    public BTPAccount getBankAccount(int account_no) throws BTPPermissionDeniedException, BTPAccountNotFoundException, BTPDataException, Exception {
        if (this.isAuthenticated()) {
            this.getPrintStream().write(BTPOperation.GET_BANK_ACCOUNT);
            this.getPrintStream().println(Integer.toString(account_no));
            int response = this.getBufferedReader().read();
            if (response != BTPResponseCode.ALL_OK) {
                String message = this.getBufferedReader().readLine();
                this.getSystem().throwExceptionById(response, message);
            }

            return this.getProtocolHelper().readAccountFromSocket();
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be authenticated to retrieve a bank account");
        }
    }

    public BTPAccount[] getBankAccounts(int customer_id)
            throws BTPPermissionDeniedException, BTPDataException, BTPUnknownException, Exception {
        if (this.isAuthenticated()) {
            return this.getProtocolHelper().getBankAccountsOfCustomer(customer_id);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException(
                    "Could not get banks of customer as you are not authenticated");
        }
    }

    public BTPAccount[] getBankAccounts(BTPCustomer customer)
            throws BTPPermissionDeniedException, BTPDataException, BTPUnknownException, Exception {
        return this.getBankAccounts(customer.getId());
    }

    public BTPAccountType[] getBankAccountTypes() throws BTPPermissionDeniedException, BTPDataException, Exception {
        if (this.isAuthenticated()) {
            return this.getProtocolHelper().getBankAccountTypes();
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be authenticated to retrieve the bank account types");
        }
    }

    public int createCustomer(BTPCustomer customer, String password) throws BTP.exceptions.BTPPermissionDeniedException,
            BTP.exceptions.BTPDataException, BTP.exceptions.BTPUnknownException, Exception {
        if (this.isAuthenticated()) {
            return this.getProtocolHelper().createCustomer(customer, password);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException(
                    "Permission denied, you must be logged in to create a customer");
        }
    }

    public BTPCustomer getCustomer(int customer_id) throws BTPPermissionDeniedException, BTPAccountNotFoundException, Exception {
        if (this.isAuthenticated()) {
            return this.getProtocolHelper().getCustomer(customer_id);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException(
                    "Permission denied, you must be authenticated to retrieve a customer");
        }
    }

    public int createBankAccount(int customer_id, BTPAccount account)
            throws BTPPermissionDeniedException, BTPDataException, Exception {
        if (this.isAuthenticated()) {
            return this.getProtocolHelper().createBankAccount(customer_id, account);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException(
                    "Permission denied, you must be authenticated to create bank accounts");
        }
    }

    public int createBankAccount(BTPCustomer customer, BTPAccount account)
            throws BTPPermissionDeniedException, BTPDataException, Exception {
        return this.createBankAccount(customer.getId(), account);
    }

    public void setBankAccountDetail(BTPKeyContainer detail) {

    }
}
