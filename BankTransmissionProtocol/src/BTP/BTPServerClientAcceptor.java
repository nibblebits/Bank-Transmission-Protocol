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
        while (true) {
            try {
                Socket socket = server_socket.accept();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            BTPServerClient client = null;
                            int auth_type = socket.getInputStream().read();
                            
                            if (auth_type == BTPClient.Customer) { // Customer Login
                                client = new BTPServerCustomerClient(server.getSystem(), server, socket);
                            } else if (auth_type == BTPClient.Employee) { // Employee login
                                client = new BTPServerEmployeeClient(server.getSystem(), server, socket);
                            } else if (auth_type == BTPClient.Transfer) { // Transfer Login
                                client = new BTPServerTransferClient(server.getSystem(), server, socket);
                            }
                            
                            if (client != null) {
                                server.addClient(client);
                                // we are already in a thread so no need to create another one. Just invoke its run method
                                client.run();

                                // Now shutdown the client
                                try {
                                    client.shutdown();
                                } catch (IOException ex1) {
                                    Logger.getLogger(BTPServerClient.class.getName()).log(Level.SEVERE, null, ex1);
                                }
                            } else {
                                // An invalid client was chosen so close the socket.
                                socket.close();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(BTPServerClientAcceptor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(BTPServerClientAcceptor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();

            } catch (IOException ex) {
                Logger.getLogger(BTPServerClientAcceptor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
