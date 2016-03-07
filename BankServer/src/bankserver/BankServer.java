/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.*;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BankServer implements BTPServerEventHandler {

    private Database database = null;
    private BTPBank this_bank = null;

    public void init() {
        database = new Database();
        database.setup();
        this_bank = new BTPBank("22-33-44", "dmwkei38823r238r23amn3b4583j", "127.0.0.1", 4444);

        BTPSystem system = new BTPSystem(this_bank);
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
    public synchronized void customerLogin(CustomerLoginEvent event) throws BTPPermissionDeniedException, BTPDataException {
        DBCustomer db_customer;
        
        try {
            db_customer = database.getCustomer(event.getCustomerId());
            if (db_customer != null && db_customer.getPassword().equals(event.getPassword())) {
                // Login was success so return
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
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
    public synchronized BTPAccount[] getBankAccountsOfCustomer(GetBankAccountsOfCustomerEvent event) {
        BTPAccount[] accounts = new BTPAccount[2];
        BTPKeyContainer extra = new BTPKeyContainer();
        extra.addKey(new BTPKey("account_type", "Student"));
        extra.addKey(new BTPKey("graduation", "2017"));
        accounts[0] = new BTPAccount(1, 123456789, "22-33-44", null);
        accounts[1] = new BTPAccount(1, 222228362, "22-33-44", extra);
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
    public synchronized void transfer(LocalTransferEvent event) throws BTPPermissionDeniedException {
        if (event.getAccountToTransferFrom().getCustomerId() == 123456789) {
            // All good!
            if (event.getAccountToTransferFrom().getAccountNumber() == 58473732) {
                return;
            }
        }
        throw new BTP.exceptions.BTPPermissionDeniedException("Transfer failed, permission denied");
    }

    @Override
    public synchronized BTPCustomer getCustomer(GetCustomerEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized double getBalance(BalanceEnquiryEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void setSavingsAccountInterestRate(SetSavingsAccountInterestRateEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized BTPTransaction[] getTransactionsOfAccount(GetTransactionsOfBankAccountEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
