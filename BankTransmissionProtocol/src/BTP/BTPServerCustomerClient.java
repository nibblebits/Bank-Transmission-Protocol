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
import java.io.IOException;
import java.net.Socket;
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
        } catch (BTP.exceptions.BTPPermissionDeniedException ex) {
            // Send a permission denied status since the login was denied
            this.getPrintStream().write(BTPResponseCode.PERMISSION_DENIED_EXCEPTION);
            // Send the failure message
            this.getPrintStream().println(ex.getMessage());
            throw ex;
        } catch (BTP.exceptions.BTPDataException ex) {
            // Send a data exception status since their was a data issue
            this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
            // Send the failure message
            this.getPrintStream().println(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            // We don't know what this exception is so send it as a standard exception
            this.getPrintStream().write(5959);
            this.getPrintStream().println(ex.getMessage());
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
                        this.getServer().getEventHandler().transfer(new LocalTransferEvent(account_from, account_to, amount));
                        this.getPrintStream().write(BTPResponseCode.ALL_OK);
                    } catch (BTPAccountNotFoundException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPBankNotFoundException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPDataException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPInvalidAccountTypeException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPPermissionDeniedException ex) {
                        this.getPrintStream().write(BTPResponseCode.PERMISSION_DENIED_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    }
                } else {
                    try {
                        this.getServer().getEventHandler().transfer(new RemoteTransferEvent(account_from, account_to, amount));
                        this.getPrintStream().write(BTPResponseCode.ALL_OK);
                    } catch (BTPAccountNotFoundException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPBankNotFoundException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPDataException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPInvalidAccountTypeException ex) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    } catch (BTPPermissionDeniedException ex) {
                        this.getPrintStream().write(BTPResponseCode.PERMISSION_DENIED_EXCEPTION);
                        this.getPrintStream().println(ex.getMessage());
                    }
                }
            }
            break;

            case BTPOperation.GET_BANK_ACCOUNTS: {
                try {
                    BTPAccount[] bank_accounts = this.getServer().getEventHandler().getBankAccountsOfCustomer(new GetBankAccountsOfCustomerEvent(this.customer.getId()));
                    // A default check just in case the event handler does not throw an exception upon their being no bank accounts
                    if (bank_accounts == null || bank_accounts.length == 0) {
                        this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                        this.getPrintStream().println("Error no bank accounts have been found.");
                    } else {
                        this.getPrintStream().write(BTPResponseCode.ALL_OK);
                        this.getPrintStream().write(bank_accounts.length);
                        for (int i = 0; i < bank_accounts.length; i++) {
                            BTPAccount account = bank_accounts[i];
                            BTPKeyContainer container = account.getExtraDetail();
                            this.getPrintStream().println(Integer.toString(account.getAccountNumber()));
                            this.getPrintStream().write(container.getTotalKeys());
                            for (int b = 0; b < container.getTotalKeys(); b++) {
                                BTPKey key = container.getKey(b);
                                this.getPrintStream().println(key.getIndexName());
                                this.getPrintStream().println(key.getValue());
                            }

                        }
                    }
                } catch (BTPPermissionDeniedException ex) {
                    this.getPrintStream().write(BTPResponseCode.PERMISSION_DENIED_EXCEPTION);
                    this.getPrintStream().println(ex.getMessage());
                } catch (BTPDataException ex) {
                    this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                    this.getPrintStream().println(ex.getMessage());
                } catch (Exception ex) {
                    // To be done later
                }
            }
            break;

            case BTPOperation.GET_BALANCE: {
                int account_no = Integer.parseInt(this.getBufferedReader().readLine());
                BTPAccount account = new BTPAccount(this.getCustomer().getId(), account_no, this.getSystem().getOurBank().getSortcode(), null);

                try {
                    double balance = this.getServer().getEventHandler().getBalance(new BalanceEnquiryEvent(account));
                    this.getPrintStream().write(BTPResponseCode.ALL_OK);
                    this.getPrintStream().println(Double.toString(balance));
                } catch (BTPDataException ex) {
                    this.getPrintStream().write(BTPResponseCode.DATA_EXCEPTION);
                    this.getPrintStream().println(ex.getMessage());
                } catch (BTPPermissionDeniedException ex) {
                    this.getPrintStream().write(BTPResponseCode.PERMISSION_DENIED_EXCEPTION);
                    this.getPrintStream().println(ex.getMessage());
                } catch (Exception ex) {
                    Logger.getLogger(BTPServerCustomerClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
        }
    }

    public BTPCustomer getCustomer() {
        return this.customer;
    }
}
