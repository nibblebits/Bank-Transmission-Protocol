/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;
import BTP.exceptions.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BTPCustomerClient extends BTPClient{

    public BTPCustomerClient(Socket socket) throws IOException {
        super(socket);
       
    }
    
    public boolean login(int customer_id, String password) throws BTPPermissionDeniedException, BTPDataException {
        // Send the customer client authentication type.
        this.getPrintStream().write(BTPClient.Customer);
        this.getPrintStream().println(Integer.toString(customer_id));
        this.getPrintStream().println(password);
        this.getPrintStream().flush();
        try {
            int response = this.getBufferedReader().read();
            if (response == BTPResponseCode.ALL_OK) {
                // Set this client as authenticated
                this.setAuthenticated(true);
                return true;
            } else {
               String message = this.getBufferedReader().readLine();
               if (response == BTPResponseCode.PERMISSION_DENIED_EXCEPTION) {
                   throw new BTP.exceptions.BTPPermissionDeniedException(message);
               } else if(response == BTPResponseCode.DATA_EXCEPTION) {
                   throw new BTP.exceptions.BTPDataException(message);
               }
            }
        } catch (IOException ex) {
            Logger.getLogger(BTPCustomerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) throws BTPPermissionDeniedException {
        if (this.isAuthenticated()) {
            
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("You must be logged in to perform a transfer");
        }
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
