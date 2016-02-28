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
    private Socket socket;
    private boolean authenticated;
    private PrintStream output;
    private BufferedReader input;
    public BTPClient(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new PrintStream(socket.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.authenticated = false;
    }
    
    protected PrintStream getPrintStream() {
        return this.output;
    }
    
    protected BufferedReader getBufferedReader() {
        return this.input;
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
