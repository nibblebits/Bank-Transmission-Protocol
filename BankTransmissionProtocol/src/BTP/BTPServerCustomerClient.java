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

    private final BTPCustomer customer;
    public BTPServerCustomerClient(BTPServer server, Socket client) throws IOException {
        super(server, client);
        this.customer = null;
    }

    @Override
    public void run() {
        super.run();
    }
    
    @Override
    protected void authenticate() throws Exception {
        int customer_id = Integer.parseInt(this.getBufferedReader().readLine());
        String password = this.getBufferedReader().readLine();
        /*
           Here we attempt to call the event handlers customerLogin method so that the server may authenticate the customer.
           Upon failure the event handler can throw any exception at us.
        */
        try {
            this.getServer().getEventHandler().customerLogin(new CustomerLoginEvent(customer_id, password));
            /*
                If we reached this point with no exception thrown then its safe to assume that the customer 
                has logged in succesfully.
            */
            // Send a 0 to state that the authentication was succesful
            this.getPrintStream().write(0);
            this.setAuthenticated(true);
        } catch(BTP.exceptions.BTPPermissionDeniedException ex) {
            // Send a 1 since their was a permissio
            this.getPrintStream().write(1);
            // Send the failure message
            this.getPrintStream().println(ex.getMessage());
            throw ex;
        }
        
        // Flush the print stream
        this.getPrintStream().flush();
    }
    
    @Override
    protected void handleSocketInput() {
       
    }
    
    public BTPCustomer getCustomer() {
       return this.customer; 
    }
}
