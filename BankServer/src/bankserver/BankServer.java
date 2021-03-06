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
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
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
    private long last_overdrawn_check = 0;
    private long last_interest_check = 0;

    private DecimalFormat decimal_format;

    public BTPSystem getSystem() {
        return this.system;
    }

    public Database getDatabase() {
        return this.database;
    }

    public void init() {
        try {
            database = new Database(this);
            database.setup();
            this_bank = new BTPBank("22-33-44", "127.0.0.1", 4444);
            this_bank.setBankAccount(new BTPAccount(55555555, this_bank.getSortcode(), null, null));
            this.decimal_format = new DecimalFormat("£##.##");

            system = new BTPSystem(this_bank);
            system.addTrustedBank(new BTPBank("33-44-55", "MASKAMSKWEIJR32734yr734hf3fiwkmdw@", "xeno.solutions", 4444));
            BTPServer server = system.newServer(this);

            System.out.println("BTP Bank Server");
            System.out.println("BTP Build: " + this.getSystem().getBuildVersion());
            try {
                server.listen();
                System.out.println("Started listening on port " + system.getOurBank().getPort());
            } catch (IOException ex) {
                Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void process() {
        // If their has been 24 hours since the last overdrawn check..
        if ((System.currentTimeMillis() - last_overdrawn_check) > 1440000) {
            System.out.println("Overdrawn Check Initiaited");
            try {
                /* Get all the accounts in the database 
                 * and charge any that are overdrawn.*/
                DBAccount[] accounts = this.getDatabase().getAllBankAccounts();
                for (DBAccount account : accounts) {
                    if (account.isOverdraftEnabled()) {
                        if (account.isOverdrawn()) {
                            double balance = account.getBalance();
                            if (Math.abs(balance) > account.getOverdraftLimit()) {
                                // Take off the selected percentage based on the account type they are part of
                                double to_remove = Math.abs(balance) / 100 * account.getBalancePercentageChange();

                                try {
                                    try {
                                        // Withdraw the money from the account
                                        account.withdraw(to_remove);
                                        this.getDatabase().updateAccount(account);
                                    } catch (BTPPermissionDeniedException ex) {
                                        Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    System.out.println("Removed: " + decimal_format.format(to_remove)
                                            + " from overdrawn account: " + account.getAccountNumber());
                                } catch (SQLException ex) {
                                    Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.last_overdrawn_check = System.currentTimeMillis();
        }

        if ((System.currentTimeMillis() - last_interest_check) > 2880000) {
            System.out.println("Interest Check Initiaited");
            try {
                /* Get all the accounts in the database 
                 * and give interest to those who are intitled to it*/
                DBAccount[] accounts = this.getDatabase().getAllBankAccounts();
                for (DBAccount account : accounts) {
                    if (account.isInterestRateEnabled()) {
                        if (account.isInBalance()) {
                            // Take off the selected percentage based on the account type they are part of
                            double balance = account.getBalance();
                            if (balance > 0) {
                                double to_add = Math.abs(balance) / 100 * account.getBalancePercentageChange();
                                TransferAgent agent = new TransferAgent(this);
                                try {
                                    try {
                                        agent.transfer(
                                                this.getDatabase().getBankAccount(this.getSystem().getOurBank().getBankAccount().getAccountNumber()),
                                                account, to_add);
                                    } catch (BTPPermissionDeniedException ex) {
                                        Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    System.out.println("Added: " + decimal_format.format(to_add)
                                            + " to bank account: " + account.getAccountNumber());
                                } catch (SQLException ex) {
                                    Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.last_interest_check = System.currentTimeMillis();
        }
    }

    public static void main(String[] args) {
        BankServer server = new BankServer();
        server.init();
        // Never quit
        while (true) {
            // Process the Bank
            server.process();
            try {
                // Wait for a second no need to steal CPU resources.
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public void employeeLogin(EmployeeLoginEvent event) throws BTPPermissionDeniedException, BTPDataException, BTPAccountNotFoundException, Exception {
        try {
            DBEmployee employee = this.database.getEmployee(event.getEmployeeId());

            if (employee != null && event.getPassword().equals(employee.getPassword())) {
                // Login success!
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
    public synchronized int createBankAccount(CreateNewBankAccountEvent event) throws BTPDataException {
        try {
            return this.getDatabase().newBankAccount(event.getCustomerId(), event.getBankAccount());
        } catch (SQLException ex) {
            throw new BTP.exceptions.BTPDataException("A database error occured");
        }
    }

    @Override
    public synchronized int createCustomer(CreateCustomerEvent event) throws BTPPermissionDeniedException, SQLException, BTPDataException {
        return this.getDatabase().newCustomer(event.getCustomerToCreate(), event.getPassword());
    }

    @Override
    public synchronized BTPAccountType[] getBankAccountTypes(GetBankAccountTypesEvent event) throws BTPDataException {
        try {
            return this.getDatabase().getBankAccountTypes();
        } catch (SQLException ex) {
            throw new BTP.exceptions.BTPDataException("A database issue occured");
        }
    }

    @Override
    public synchronized BTPAccount[] getBankAccountsOfCustomer(GetBankAccountsOfCustomerEvent event) throws SQLException {
        DBAccount[] accounts = this.getDatabase().getBankAccounts(event.getCustomerId());
        return accounts;
    }

    @Override
    public synchronized BTPAccount getBankAccount(GetBankAccountEvent event) throws BTPPermissionDeniedException, BTPDataException, BTPDataException, BTPAccountNotFoundException {
        try {
            return this.getDatabase().getBankAccount(event.getAccountNo());
        } catch (SQLException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new BTP.exceptions.BTPDataException("Database error.");
        }
    }

    @Override
    public synchronized void transfer(RemoteTransferEvent event) throws BTPPermissionDeniedException, BTPDataException, SQLException {
        BTPAccount account_from = event.getAccountToTransferFrom();
        BTPAccount account_to = event.getAccountToTransferTo();

        try {
            TransferAgent transferAgent = new TransferAgent(this);
            if (event.isIncoming()) {
                DBAccount account_to_db = this.getDatabase().getBankAccount(account_to.getAccountNumber());
                try {
                    transferAgent.transfer(account_from, account_to_db, event.getAmountToTransfer());
                } catch (SQLException ex) {
                    Logger.getLogger(SQLException.class.getName()).log(Level.SEVERE, null, ex);
                    throw new BTP.exceptions.BTPDataException("A database problem occured");
                }
            } else {
                DBAccount account_from_db = this.getDatabase().getBankAccount(account_to.getAccountNumber());
                try {
                    transferAgent.transfer(account_from_db, account_to, event.getAmountToTransfer());
                } catch (SQLException ex) {
                    Logger.getLogger(SQLException.class.getName()).log(Level.SEVERE, null, ex);
                    throw new BTP.exceptions.BTPDataException("A database problem occured");
                }
            }

        } catch (SQLException ex) {
            throw new BTP.exceptions.BTPDataException("A problem occured querying the database");
        }

    }

    @Override
    public synchronized void transfer(LocalTransferEvent event) throws BTPPermissionDeniedException, BTPDataException, BTPAccountNotFoundException {
        BTPAccount account_from = event.getAccountToTransferFrom();
        BTPAccount account_to = event.getAccountToTransferTo();

        try {
            DBAccount account_from_db = this.getDatabase().getBankAccount(account_from.getAccountNumber());
            DBAccount account_to_db = this.getDatabase().getBankAccount(account_to.getAccountNumber());

            TransferAgent transferAgent = new TransferAgent(this);
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
    public synchronized BTPCustomer getCustomer(GetCustomerEvent event) throws BTPDataException {
        try {
            return this.getDatabase().getCustomer(event.getCustomerId());
        } catch (SQLException ex) {
            throw new BTP.exceptions.BTPDataException("A database error occured");
        }
    }

    @Override
    public synchronized double getBalance(BalanceEnquiryEvent event) throws BTPPermissionDeniedException, SQLException, BTPDataException {
        DBAccount c_account = this.getDatabase().getBankAccount(event.getAccount().getAccountNumber());
        if (c_account == null) {
            throw new BTP.exceptions.BTPDataException("The bank account " + event.getAccount().getAccountNumber() + " does not exist");
        }
        return c_account.getBalance();
    }

    @Override
    public synchronized BTPTransaction[] getTransactionsOfAccount(GetTransactionsOfBankAccountEvent event) throws BTPDataException, BTPPermissionDeniedException {
        BTPClient client = event.getClient();
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
}
