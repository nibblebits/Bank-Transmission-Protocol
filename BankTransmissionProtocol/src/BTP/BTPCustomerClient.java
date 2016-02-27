/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.net.Socket;

/**
 *
 * @author Daniel
 */
public class BTPCustomerClient extends BTPClient{

    public BTPCustomerClient(Socket socket) {
        super(socket);
    }
    
    public boolean login(int customer_id, String password) {
        return true;
    }
    
    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) {
        
    }
    
    public double getBalance(BTPAccount account) {
        return 0;
    }
    
    
   public BTPTransaction[] getTransactions()
   {
       return null;
   }
    
   public BTPAccount[] getBankAccounts() {
       return null;
   }
}
