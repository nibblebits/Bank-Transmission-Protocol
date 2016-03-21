/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

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
    
    public boolean login(int employee_id, String password) {
        
        return true;
    }
    
    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) {
        
    }
    
    public double getBalance(BTPAccount account) {
        return 0;
    }
    
    public BTPTransaction[] getTransactions(BTPAccount account) {
        return null;
    }
    
    public BTPAccount[] getBankAccounts(BTPCustomer customer) {
        return null;
    }
    
    public void createCustomer(BTPCustomer customer) {
  
    }
    
    public BTPCustomer getCustomer(int customer_id) {
        return null;
    }
    
    public void createBankAccount(BTPCustomer customer, BTPAccount account) {
        
    }
    
    public void setBankAccountDetail(BTPKeyContainer detail) {
        
    }
}
