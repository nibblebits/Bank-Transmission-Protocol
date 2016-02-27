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

public abstract class BTPClient {
    private Socket socket;
    private boolean authenticated;
    
    public BTPClient(Socket socket) {
        this.socket = socket;
        this.authenticated = false;
    }
    
    public boolean isAuthenticated() {
        return this.authenticated;
    }
    
    protected void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    protected Socket getSocket() {
        return this.socket;
    }
}
