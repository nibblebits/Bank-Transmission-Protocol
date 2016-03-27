/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Daniel
 */
public abstract class BTPClient {

    public static int Customer = 0;
    public static int Employee = 1;
    public static int Transfer = 2;
    
    private Socket socket;
    private BTPSystem system;
    private boolean authenticated;
    private PrintStream output;
    private BufferedReader input;
    private int peers_build;

    public BTPClient(BTPSystem system, Socket socket) throws IOException {
        this.system = system;
        this.socket = socket;
        this.output = new PrintStream(socket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.authenticated = false;
        this.peers_build = -1;

        try {
            // Exchange the client information with the peer
            this.exchangeClientInformation();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    protected synchronized PrintStream getPrintStream() {
        return this.output;
    }

    protected synchronized BufferedReader getBufferedReader() {
        return this.input;
    }

    public synchronized boolean isAuthenticated() {
        return this.authenticated;
    }

    protected synchronized void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    protected synchronized Socket getSocket() {
        return this.socket;
    }

    public BTPSystem getSystem() {
        return this.system;
    }

    public void shutdown() throws IOException {
        if (this.socket != null) {
            this.getPrintStream().write(BTPOperation.SHUTDOWN);
            this.socket.close();
        }
    }

    public int getPeersClientBuild() throws IOException {
        return this.peers_build;
    }

    public void setPeersClientBuild(int build) {
        this.peers_build = build;
    }

    protected abstract void exchangeClientInformation() throws IOException;
}
