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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Daniel
 */
public class BankServer implements BTPServerEventHandler {

    public static void main(String[] args) {
        // TODO code application logic here
        BTPSystem system = new BTPSystem(new BTPBank("22-33-44", "dmwkei38823r238r23amn3b4583j", "127.0.0.1", 4444));
        BTPServer server = system.newServer(new BankServer());
        try {
            server.listen();
            System.out.println("Started listening on port " + system.getOurBank().getPort());
        } catch (IOException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Never quit
        while(true) { }
    }

    @Override
    public void customerLogin(CustomerLoginEvent event) throws BTPPermissionDeniedException, BTPDataException {
        if(event.getCustomerId() == 123456789 && event.getPassword().equals("1234")) {
            return;
        }
        throw new BTP.exceptions.BTPPermissionDeniedException("Failed to login due to bad login details");
    }

    @Override
    public void createBankAccount(CreateNewBankAccountEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createCustomer(CreateCustomerEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDailyOverdrawnCharge(SetDailyOverdrawnChargeEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BTPAccountType[] getBankAccountTypes(GetBankAccountTypesEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BTPAccount[] getBankAccountsOfCustomer(GetBankAccountsOfCustomerEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBankAccountsOverdraftLimit(SetBankAccountOverdraftLimitEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void transfer(RemoteTransferEvent event) throws BTPPermissionDeniedException {
       throw new BTP.exceptions.BTPPermissionDeniedException("Transfers are not allowed.");
    }

    @Override
    public void transfer(LocalTransferEvent event) throws BTPPermissionDeniedException {
       if (event.getAccountToTransferFrom().getCustomerId() == 123456789) {
           // All good!
           if (event.getAccountToTransferFrom().getAccountNumber() == 58473732) {
               return;
           }
       }
       throw new BTP.exceptions.BTPPermissionDeniedException("Transfer failed, permission denied");
    }

    @Override
    public BTPCustomer getCustomer(GetCustomerEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getBalance(BalanceEnquiryEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSavingsAccountInterestRate(SetSavingsAccountInterestRateEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BTPTransaction[] getTransactionsOfAccount(GetTransactionsOfBankAccountEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
