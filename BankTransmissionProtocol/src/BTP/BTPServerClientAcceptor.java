/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BTPServerClientAcceptor implements Runnable {

    private BTPServer server;
    private ServerSocket server_socket;
    
    public BTPServerClientAcceptor(BTPServer server, ServerSocket server_socket) {
        this.server = server;
        this.server_socket = server_socket;
    }
    
    @Override
    public void run() {
        try {
            Socket socket = server_socket.accept();
            BTPServerClient client = null;
            int auth_type = socket.getInputStream().read();
            if (auth_type == BTPServerClient.Customer) { // Customer Login
                client = new BTPServerCustomerClient(this.server, socket);
            } else if(auth_type == BTPServerClient.Employee) { // Employee login
                client = new BTPServerEmployeeClient(this.server, socket);
            } else if(auth_type == BTPServerClient.Transfer) { // Transfer Login
                client = new BTPServerTransferClient(this.server, socket);
            } else if(auth_type == BTPServerClient.Administrator) { // Administrator Login
                client = new BTPServerAdministratorClient(this.server, socket);
            }
            
            if (client != null) {
                this.server.addClient(client);
            }
        } catch (IOException ex) {
            Logger.getLogger(BTPServerClientAcceptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
