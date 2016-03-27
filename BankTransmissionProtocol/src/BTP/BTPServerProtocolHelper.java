/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPInvalidAccountTypeException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class BTPServerProtocolHelper extends BTPProtocolHelper {

    private BTPServer server;

    public BTPServerProtocolHelper(BTPSystem system, BTPServerClient client, BufferedReader input, PrintStream output, BTPServer server) {
        super(system, client, input, output);
        this.server = server;
    }

    public void handleTransferEnquiry(BTPAccount account_from, BTPAccount account_to, double amount) {
        try {
            // Little bit of security we don't want people sending money to themselves ;)
            if (account_from.getAccountNumber() == account_to.getAccountNumber()
                    && account_from.getSortCode().equals(account_to.getSortCode())) {
                throw new BTP.exceptions.BTPPermissionDeniedException("You may not send money to the same account your sending from.");
            }
            if (account_to.getSortCode().equals(server.getSystem().getOurBank().getSortcode())) {

                this.server.getEventHandler().transfer(new LocalTransferEvent(this.getClient(), account_from, account_to, amount));
                this.getPrintStream().write(BTPResponseCode.ALL_OK);

            } else {

                this.server.getEventHandler().transfer(new RemoteTransferEvent(this.getClient(), account_from, account_to, amount));
                this.getPrintStream().write(BTPResponseCode.ALL_OK);
            }
        } catch (Exception ex) {
            // Send an exception response to the client as their was an error
            this.sendExceptionResponseOverSocket(ex);
        }
    }

    public double handleBalanceEnquiry(BTPAccount account) {
        double balance = -1;
        try {
            if (account == null) {
                throw new BTP.exceptions.BTPDataException("No account was specified to query a balance from.");
            }
            balance = server.getEventHandler().getBalance(new BalanceEnquiryEvent(this.getClient(), account));
            this.getPrintStream().write(BTPResponseCode.ALL_OK);
            this.getPrintStream().println(Double.toString(balance));
        } catch (Exception ex) {
            // Send an exception response to the client as their was an error
            this.sendExceptionResponseOverSocket(ex);
        }

        return balance;
    }

    public BTPTransaction[] handleTransactionsEnquiry(BTPAccount account, Date from, Date to) {
        BTPTransaction[] transactions = null;
        try {
            transactions = this.server.getEventHandler().getTransactionsOfAccount(
                    new GetTransactionsOfBankAccountEvent(this.getClient(), account, from, to));
            this.getPrintStream().write(BTPResponseCode.ALL_OK);
            this.getPrintStream().write(transactions.length);
            for (BTPTransaction transaction : transactions) {
                this.writeTransactionToSocket(transaction);
            }
        } catch (Exception ex) {
            this.sendExceptionResponseOverSocket(ex);
        }
        this.getPrintStream().flush();
        return transactions;
    }

    public int handleCustomerCreationEnquiry() throws BTPPermissionDeniedException, BTPDataException, Exception {
        BTPCustomer customer = this.readCustomerFromSocket();
        String password = this.getBufferedReader().readLine();
        int customer_id = this.server.getEventHandler().createCustomer(
                new CreateCustomerEvent(this.getClient(), customer, password));
        this.getPrintStream().write(BTPResponseCode.ALL_OK);
        this.getPrintStream().println(Integer.toString(customer_id));
        return customer_id;
    }

    public void handleBankCreationEnquiry()
            throws BTPPermissionDeniedException, BTPDataException,
            BTPAccountNotFoundException, BTPInvalidAccountTypeException, Exception {
        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        BTPAccount account = this.readAccountFromSocket();
        this.server.getEventHandler().createBankAccount(
                new CreateNewBankAccountEvent(this.getClient(), customer_id, account));
        this.getPrintStream().write(BTPResponseCode.ALL_OK);
    }

    public BTPCustomer handleGetCustomerEnquiry()
            throws BTPPermissionDeniedException, BTPAccountNotFoundException, BTPDataException, IOException {
        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        BTPCustomer customer = this.server.getEventHandler().getCustomer(
                new GetCustomerEvent(this.getClient(), customer_id));
        this.getPrintStream().write(BTPResponseCode.ALL_OK);
        this.writeCustomerToSocket(customer);
        return customer;
    }

    public BTPAccount[] handleGetBankAccountsOfCustomerEnquiry(int customer_id)
            throws BTPPermissionDeniedException, BTPAccountNotFoundException, BTPDataException, Exception {
        BTPAccount[] bank_accounts = this.server.getEventHandler().getBankAccountsOfCustomer(
                new GetBankAccountsOfCustomerEvent(this.getClient(), customer_id)
        );
        // A default check just in case the event handler does not throw an exception upon their being no bank accounts
        if (bank_accounts == null || bank_accounts.length == 0) {
            this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
            this.getPrintStream().println("Error no bank accounts have been found.");
        } else {
            this.getPrintStream().write(BTPResponseCode.ALL_OK);
            this.getPrintStream().write(bank_accounts.length);
            for (int i = 0; i < bank_accounts.length; i++) {
                BTPAccount account = bank_accounts[i];
                this.writeAccountToSocket(account);
            }
        }
        return bank_accounts;
    }

    public BTPAccountType[] handleGetBankAccountTypesEnquiry() throws BTPPermissionDeniedException, BTPDataException, Exception {
        BTPAccountType[] account_types = this.server.getEventHandler().getBankAccountTypes(
                new GetBankAccountTypesEvent(this.getClient()));
        
        this.getPrintStream().write(BTPResponseCode.ALL_OK);
        this.getPrintStream().println(Integer.toString(account_types.length));
        for(BTPAccountType account_type : account_types) {
            this.getPrintStream().println(Integer.toString(account_type.getId()));
            this.getPrintStream().println(account_type.getName());
        }
        
        return account_types;
    }
    public void sendExceptionResponseOverSocket(Exception exception) {
        int response_code;
        if (exception instanceof BTP.exceptions.BTPAccountNotFoundException) {
            response_code = BTPResponseCode.ACCOUNT_NOT_FOUND_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPBankNotFoundException) {
            response_code = BTPResponseCode.BANK_NOT_FOUND_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPDataException) {
            response_code = BTPResponseCode.DATA_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPInvalidAccountTypeException) {
            response_code = BTPResponseCode.INVALID_ACCOUNT_TYPE_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPPermissionDeniedException) {
            response_code = BTPResponseCode.PERMISSION_DENIED_EXCEPTION;
        } else {
            response_code = BTPResponseCode.UNKNOWN_EXCEPTION;
        }

        this.getPrintStream().write(response_code);
        this.getPrintStream().println(exception.getMessage());
        this.getPrintStream().flush();
    }
}
