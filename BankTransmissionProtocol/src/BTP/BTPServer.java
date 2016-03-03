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
        this.port = system.getOurBank().getPort();
        this.server_socket = null;
        this.client = new ArrayList<BTPClient>();
        this.eventHandler = eventHandler;
        this.system = system;
    }
    
    public void listen() throws IOException {
        this.server_socket = new ServerSocket(this.port);
        // Create a new thread that will listen for clients
        new Thread(new BTPServerClientAcceptor(this, this.server_socket)).start();
    }
    
    public synchronized BTPSystem getSystem() {
        return this.system;
    }
    
    public synchronized BTPServerEventHandler getEventHandler() {
        return this.eventHandler;
    }
    
    public synchronized void addClient(BTPServerClient client) {
        this.client.add(client);
    }
    
    public synchronized ArrayList<BTPClient> getClients() {
        return this.client;
    }
    
    public synchronized void shutdown() {
        
    }
}
