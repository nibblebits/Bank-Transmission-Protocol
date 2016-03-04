/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;
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
public class BankClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Bank Client");
 
        BTPSystem system = new BTPSystem(new BTPBank("22-33-44", "127.0.0.1", 4444));
         BTPCustomerClient customerClient = null;
        try {
           customerClient = system.newCustomerClientFromLogin(123456789, "1234");
        } catch (IOException ex) {
            Logger.getLogger(BankClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Failed to login: " + ex.getMessage());
        } catch(Exception ex) {
            Logger.getLogger(BankClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (customerClient != null) {
            try {
                customerClient.transfer(new BTPAccount(-1, 58473732, "22-33-44", null), new BTPAccount(-1, 58473734, "22-33-44", null), 22.12);
                System.out.println("Transfer complete!");
            } catch (BTPPermissionDeniedException ex) {
                Logger.getLogger(BankClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BankClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BTPDataException ex) {
                Logger.getLogger(BankClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (customerClient != null) {
            customerClient.shutdown();
        }
    }
    
}
