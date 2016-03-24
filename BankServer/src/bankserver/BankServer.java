/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.*;
import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BankServer implements BTPServerEventHandler {

    private Database database = null;
    private BTPBank this_bank = null;
    private BTPSystem system = null;

    public BTPSystem getSystem() {
        return this.system;
    }

    public Database getDatabase() {
        return this.database;
    }

    public void init() {
        database = new Database(this);
        database.setup();
        this_bank = new BTPBank("22-33-44", "dmwkei38823r238r23amn3b4583j", "127.0.0.1", 4444);

        System.out.println((new Date()).getTime());

        system = new BTPSystem(this_bank);
        BTPServer server = system.newServer(this);
        try {
            server.listen();
            System.out.println("Started listening on port " + system.getOurBank().getPort());
        } catch (IOException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {

    }

    public static void main(String[] args) {
        BankServer server = new BankServer();
        server.init();
        server.run();

        // Never quit
        while (true) {
        }
    }

    @Override
    public synchronized void customerLogin(CustomerLoginEvent event) throws BTPPermissionDeniedException, BTPDataException, Exception {
        DBCustomer db_customer;

        try {
            db_customer = database.getCustomer(event.getCustomerId());
            if (db_customer != null && db_customer.getPassword().equals(event.getPassword())) {
                // Login was success so return
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLException.class.getName()).log(Level.SEVERE, null, ex);
            throw new BTP.exceptions.BTPDataException("An issue occured when reading the database");
        } catch (Exception ex) {
            Logger.getLogger(Exception.class.getName()).log(Level.SEVERE, null, ex);
            throw new BTP.exceptions.BTPDataException("An unknown issue occured.");
        }
        throw new BTP.exceptions.BTPPermissionDeniedException("Failed to login due to bad login details");
    }

    @Override
    public synchronized void createBankAccount(CreateNewBankAccountEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void createCustomer(CreateCustomerEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void setDailyOverdrawnCharge(SetDailyOverdrawnChargeEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized BTPAccountType[] getBankAccountTypes(GetBankAccountTypesEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized BTPAccount[] getBankAccountsOfCustomer(GetBankAccountsOfCustomerEvent event) throws SQLException {
        DBAccount[] accounts = this.getDatabase().getBankAccounts(event.getCustomerId());
        return accounts;
    }

    @Override
    public synchronized void setBankAccountsOverdraftLimit(SetBankAccountOverdraftLimitEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void transfer(RemoteTransferEvent event) throws BTPPermissionDeniedException {
        throw new BTP.exceptions.BTPPermissionDeniedException("Transfers are not allowed.");
    }

    @Override
    public synchronized void transfer(LocalTransferEvent event) throws BTPPermissionDeniedException, BTPDataException, BTPAccountNotFoundException {
        BTPAccount account_from = event.getAccountToTransferFrom();
        BTPAccount account_to = event.getAccountToTransferTo();
        try {
            DBAccount account_from_db = this.getDatabase().getBankAccount(account_from.getAccountNumber());
            DBAccount account_to_db = this.getDatabase().getBankAccount(account_to.getAccountNumber());
            if (account_from_db == null) {
                throw new BTP.exceptions.BTPAccountNotFoundException("Your bank account could not be found? has "
                        + "it been deleted recently?");
            }
            if (account_to_db == null) {
                throw new BTP.exceptions.BTPAccountNotFoundException("The recipient account could not be found.");
            }

            TransferAgent transferAgent = new TransferAgent(this.getDatabase());
            try {
                transferAgent.transfer(account_from_db, account_to_db, event.getAmountToTransfer());
            } catch (SQLException ex) {
                Logger.getLogger(SQLException.class.getName()).log(Level.SEVERE, null, ex);
                throw new BTP.exceptions.BTPDataException("A database problem occured");
            }

        } catch (SQLException ex) {
            throw new BTP.exceptions.BTPDataException("A problem occured querying the database");
        }
    }

    @Override
    public synchronized BTPCustomer getCustomer(GetCustomerEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized double getBalance(BalanceEnquiryEvent event) throws BTPPermissionDeniedException, SQLException, BTPDataException {
        BTPClient client = event.getClient();
        BTPAccount bank_account_to_view = event.getAccount();

        BTPServerCustomerClient c_client = (BTPServerCustomerClient) client;
        DBCustomer c = database.getCustomer(c_client.getCustomer().getId());
        DBAccount c_account = c.getBankAccount(event.getAccount().getAccountNumber());
        if (c_account == null) {
            throw new BTP.exceptions.BTPDataException("The bank account " + bank_account_to_view.getAccountNumber() + " does not exist");
        }
        return c_account.getBalance();
    }

    @Override
    public synchronized void setSavingsAccountInterestRate(SetSavingsAccountInterestRateEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized BTPTransaction[] getTransactionsOfAccount(GetTransactionsOfBankAccountEvent event) throws BTPDataException, BTPPermissionDeniedException {
        BTPClient client = event.getClient();
        if (client instanceof BTPServerCustomerClient) {
            try {
                BTPTransaction[] transactions = this.getDatabase().getTransactions(
                        event.getAccount(),
                        event.getDateFrom(),
                        event.getDateTo());
                return transactions;
            } catch (SQLException ex) {
                throw new BTP.exceptions.BTPDataException("An issue occured while querying the database");
            }
        }
        throw new BTP.exceptions.BTPPermissionDeniedException("Client type is not allowed to view transactions");
    }

    @Override
    public void employeeLogin(EmployeeLoginEvent event) throws BTPPermissionDeniedException, BTPDataException, BTPAccountNotFoundException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
