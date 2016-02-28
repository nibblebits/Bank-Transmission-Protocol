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
public class BTPServerCustomerClient extends BTPServerClient {

    private BTPCustomer customer;
    public BTPServerCustomerClient(BTPServer server, Socket client) throws IOException {
        super(server, client);
        this.customer = null;
    }

    @Override
    public void run() {
        super.run();
    }
    
    @Override
    protected void handleSocketInput() {
       
    }
    
    public BTPCustomer getCustomer() {
       return this.customer; 
    }
}
