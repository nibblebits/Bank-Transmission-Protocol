/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

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
            this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
        }

        // Flush the print stream
        this.getPrintStream().flush();
    }
    
    @Override
    protected void handleOperation(int opcode) throws Exception {
        switch (opcode) {
            case BTPOperation.TRANSFER: {
                BTPAccount account_from = this.getProtocolHelper().readAccountFromSocket();
                BTPAccount account_to = this.getProtocolHelper().readAccountFromSocket();
                
                double amount = Double.parseDouble(this.getBufferedReader().readLine());
                this.getProtocolHelper().handleTransferEnquiry(account_from, account_to, amount);
            }
            break;
            
            case BTPOperation.GET_BANK_ACCOUNTS: {
                try {
                    BTPAccount[] bank_accounts = this.getServer().getEventHandler().getBankAccountsOfCustomer(
                            new GetBankAccountsOfCustomerEvent(this, this.getCustomer().getId())
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
                            this.getProtocolHelper().writeAccountToSocket(account);
                        }
                    }
                } catch (Exception ex) {
                    // Send an exception response to the client as their was an error
                    this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
                }
            }
            break;
            
            case BTPOperation.GET_BALANCE: {
                int account_no = Integer.parseInt(this.getBufferedReader().readLine());
                try {
                    if (!this.isMyBankAccount(account_no)) {
                        throw new BTP.exceptions.BTPPermissionDeniedException("This is not your bank account.");
                    }
                    BTPAccount account = new BTPAccount(this.getCustomer().getId(),
                            account_no,
                            this.getSystem().getOurBank().getSortcode(),
                            this.getBankAccount(account_no).getAccountType(),
                            null);
                    this.getProtocolHelper().handleBalanceEnquiry(account);
                    
                } catch (BTPPermissionDeniedException ex) {
                    this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
                }
                
            }
            break;
            
            case BTPOperation.GET_TRANSACTIONS: {
                BTPAccount account;
                Date date_from = new Date();
                Date date_to = new Date();
                
                int account_no = Integer.parseInt(this.getBufferedReader().readLine());
                try {
                    if (!this.isMyBankAccount(account_no)) {
                        throw new BTP.exceptions.BTPPermissionDeniedException(
                                "You may not request transactions, this is not your bank account."
                        );
                    }
                    account = this.getBankAccount(account_no);
                    
                    date_from.setTime(Long.valueOf(this.getBufferedReader().readLine()));
                    date_to.setTime(Long.valueOf(this.getBufferedReader().readLine()));
                    
                    this.getProtocolHelper().handleTransactionsEnquiry(account, date_from, date_to);
                } catch (BTPPermissionDeniedException ex) {
                    this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
                }
            }
            break;
        }
    }
    
    public BTPCustomer getCustomer() {
        return this.customer;
    }
    
    protected BTPAccount getBankAccount(int id) throws BTPPermissionDeniedException, BTPDataException, Exception {
        BTPAccount[] accounts = this.getServer().getEventHandler().getBankAccountsOfCustomer(
                new GetBankAccountsOfCustomerEvent(this, this.getCustomer().getId())
        );
        
        for (BTPAccount account : accounts) {
            if (account.getAccountNumber() == id) {
                return account;
            }
        }
        
        return null;
    }
    
    public boolean isMyBankAccount(int id) throws BTPPermissionDeniedException, BTPDataException, Exception {
        return getBankAccount(id) != null;
    }
}
