/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPBankNotFoundException;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPInvalidAccountTypeException;
import BTP.exceptions.BTPPermissionDeniedException;
import BTP.exceptions.BTPUnknownException;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BTPServerCustomerClient extends BTPServerClient {
    
    private BTPCustomer customer;
    
    public BTPServerCustomerClient(BTPSystem system, BTPServer server, Socket client) throws IOException {
        super(system, server, client);
        this.customer = null;
    }
    
    @Override
    public void run() {
        super.run();
    }
    
    @Override
    protected void authenticate() throws Exception {
        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        String password = this.getBufferedReader().readLine();
        /*
         Here we attempt to call the event handlers customerLogin method so that the server may authenticate the customer.
         Upon failure the event handler can throw any exception at us.
         */
        try {
            this.getServer().getEventHandler().customerLogin(new CustomerLoginEvent(customer_id, password));
            /*
             If we reached this point with no exception thrown then its safe to assume that the customer 
             has logged in succesfully.
             */
            // Send a ALL_OK status to state that the authentication was succesful
            this.getPrintStream().write(BTPResponseCode.ALL_OK);
            this.setAuthenticated(true);
            this.customer = new BTPCustomer(customer_id, null, null, null, null, null);
        } catch (Exception ex) {
            // Send the exception response to the client as their was a problem
            this.sendExceptionResponseOverSocket(ex);
        }

        // Flush the print stream
        this.getPrintStream().flush();
    }
    
    @Override
    protected void handleSocketInput() throws IOException {
        int operation = this.getBufferedReader().read();
        switch (operation) {
            case BTPOperation.TRANSFER: {
                BTPAccount account_from = new BTPAccount(
                        this.getCustomer().getId(),
                        Integer.parseInt(this.getBufferedReader().readLine()),
                        this.getBufferedReader().readLine(),
                        null
                );
                BTPAccount account_to = new BTPAccount(
                        -1,
                        Integer.parseInt(this.getBufferedReader().readLine()),
                        this.getBufferedReader().readLine(),
                        null
                );
                
                double amount = Double.parseDouble(this.getBufferedReader().readLine());
                if (account_to.getSortCode().equals(this.getServer().getSystem().getOurBank().getSortcode())) {
                    try {
                        this.getServer().getEventHandler().transfer(new LocalTransferEvent(this, account_from, account_to, amount));
                        this.getPrintStream().write(BTPResponseCode.ALL_OK);
                    } catch (Exception ex) {
                        // Send an exception response to the client as their was an error
                        this.sendExceptionResponseOverSocket(ex);
                    }
                } else {
                    try {
                        this.getServer().getEventHandler().transfer(new RemoteTransferEvent(this, account_from, account_to, amount));
                        this.getPrintStream().write(BTPResponseCode.ALL_OK);
                    } catch (Exception ex) {
                        // Send an exception response to the client as their was an error
                        this.sendExceptionResponseOverSocket(ex);
                    }
                }
            }
            break;
            
            case BTPOperation.GET_BANK_ACCOUNTS: {
                try {
                    BTPAccount[] bank_accounts = this.getServer().getEventHandler().getBankAccountsOfCustomer(new GetBankAccountsOfCustomerEvent(this, this.customer.getId()));
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
                } catch (Exception ex) {
                    // Send an exception response to the client as their was an error
                    this.sendExceptionResponseOverSocket(ex);
                }
            }
            break;
            
            case BTPOperation.GET_BALANCE: {
                int account_no = Integer.parseInt(this.getBufferedReader().readLine());
                BTPAccount account = new BTPAccount(this.getCustomer().getId(), account_no, this.getSystem().getOurBank().getSortcode(), null);
                
                try {
                    double balance = this.getServer().getEventHandler().getBalance(new BalanceEnquiryEvent(this, account));
                    this.getPrintStream().write(BTPResponseCode.ALL_OK);
                    this.getPrintStream().println(Double.toString(balance));
                } catch (Exception ex) {
                    // Send an exception response to the client as their was an error
                    this.sendExceptionResponseOverSocket(ex);
                }
            }
            break;
            
            case BTPOperation.GET_TRANSACTIONS: {
                BTPAccount account;
                Date date_from = new Date();
                Date date_to = new Date();
                account = new BTPAccount(
                        this.getCustomer().getId(),
                        Integer.parseInt(this.getBufferedReader().readLine()),
                        this.getSystem().getOurBank().getSortcode(),
                        null
                );
                date_from.setTime(Long.valueOf(this.getBufferedReader().readLine()));
                date_to.setTime(Long.valueOf(this.getBufferedReader().readLine()));
                
                try {
                    BTPTransaction[] transactions = this.getServer().getEventHandler().getTransactionsOfAccount(
                            new GetTransactionsOfBankAccountEvent(this, account, date_from, date_to)
                    );
                    this.getPrintStream().write(BTPResponseCode.ALL_OK);
                    this.getPrintStream().write(transactions.length);
                    for (BTPTransaction transaction : transactions) {
                        this.writeTransactionToSocket(transaction);
                    }
                } catch (Exception ex) {
                    this.sendExceptionResponseOverSocket(ex);
                }
                this.getPrintStream().flush();
            }
            break;
        }
    }
    
    public BTPCustomer getCustomer() {
        return this.customer;
    }
}
