/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.*;
import java.io.IOException;
import java.util.Scanner;


/**
 *
 * @author Daniel
 */
public class BankClient {

    /**
     * @param args the command line arguments
     */
    private BTPSystem system = null;
    private Scanner scanner = null;
    private BTPClient client = null;
    private PageNavigator navigator = null;
    BTPAccount selected_account = null;
    private boolean running = true;

    public BankClient() {
        this.system = new BTPSystem(new BTPBank("22-33-44", "127.0.0.1", 4444));
        this.system.getOurBank().setBankAccount(new BTPAccount(55555555, "22-33-44", null, null));
        this.scanner = new Scanner(System.in);
        this.navigator = new PageNavigator(this);
        setRunning(true);
    }
    
    public BTPSystem getSystem() {
        return this.system;
    }
    
    public Scanner getScanner() {
        return this.scanner;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void setBTPClient(BTPClient client) {
        this.client = client;
    }
    
    public BTPClient getBTPClient() {
        return this.client;
    }
    
    public PageNavigator getPageNavigator() {
        return this.navigator;
    }
    
    public void run() {
        // Show the login page
       getPageNavigator().showPage(new BankLogin(this));
    }

    public static void main(String[] args) throws IOException {
        BankClient client = new BankClient();
        client.run();
    }

}
