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
public abstract class BTPServerClient extends BTPClient implements Runnable {
    
    public static int Customer = 0;
    public static int Employee = 1;
    public static int Transfer = 2;
    public static int Administrator = 3;
    
    private final BTPServer server;
    
    public BTPServerClient(BTPServer server, Socket client) throws IOException {
        super(client);
        this.server = server;
    }
    
   
    @Override
    public void run() {
        this.handleSocketInput();
    }
    
    protected abstract void handleSocketInput();
    
    public BTPServer getServer() {
        return this.server;
    }
}
