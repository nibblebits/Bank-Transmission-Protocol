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
public class BTPTransferClient extends BTPConnectorClient {

    public BTPTransferClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);
    }
    
    public boolean login(String bank_sortcode, String auth_code) {
        return true;
    }
    
    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) {
        
    }
    
}
