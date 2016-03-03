/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                                client = new BTPServerCustomerClient(server, socket);
                            } else if (auth_type == BTPClient.Employee) { // Employee login
                                client = new BTPServerEmployeeClient(server, socket);
                            } else if (auth_type == BTPClient.Transfer) { // Transfer Login
                                client = new BTPServerTransferClient(server, socket);
                            } else if (auth_type == BTPClient.Administrator) { // Administrator Login
                                client = new BTPServerAdministratorClient(server, socket);
                            }

                            if (client != null) {
                                server.addClient(client);
                                // Start the new client thread
                                new Thread(client).start();
                            } else {
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
