/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class BTPServer {
    private int port;
    private BTPServerEventHandler eventHandler;
    private ServerSocket server_socket;
    private ArrayList<BTPClient> client;
    private BTPSystem system;
    
    public BTPServer(BTPSystem system, BTPServerEventHandler eventHandler) {
        this.port = -1;
        this.server_socket = null;
        this.client = new ArrayList<BTPClient>();
        this.eventHandler = eventHandler;
        this.system = system;
    }
    
    public void listen(int port) throws IOException {
        this.server_socket = new ServerSocket(port);
        // Create a new thread that will listen for clients
        new Thread(new BTPServerClientAcceptor(this, this.server_socket)).start();
    }
    
    public BTPSystem getSystem() {
        return this.system;
    }
    
    public BTPServerEventHandler getEventHandler() {
        return this.eventHandler;
    }
    
    public void addClient(BTPServerClient client) {
        this.client.add(client);
    }
    
    public ArrayList<BTPClient> getClients() {
        return this.client;
    }
    
    public void shutdown() {
        
    }
}
