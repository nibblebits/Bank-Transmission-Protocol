/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public abstract class BTPServerClient extends BTPClient implements Runnable {
    
    private final BTPServer server;
    
    public BTPServerClient(BTPSystem system, BTPServer server, Socket client) throws IOException {
        super(system, client);
        this.server = server;
    }
    
   
    @Override
    public void run()  {
        try {
            this.authenticate();
        } catch (Exception ex) {
            // They failed to authenticate so return.
            System.err.println("Login failed: " + ex.getMessage());
            return;
        }
        
        while(true) {
            try {
                this.handleSocketInput();
            } catch (Exception ex) {
                // Something went wrong? Could be anything so log the error and return.
                Logger.getLogger(BTPServerClient.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
    }
    
    protected abstract void authenticate() throws Exception;
    protected abstract void handleSocketInput() throws Exception;
    
    public synchronized BTPServer getServer() {
        return this.server;
    }
}
