/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.net.Socket;

/**
 *
 * @author Daniel
 */
public class BTPServerTransferClient extends BTPServerClient {

    public BTPServerTransferClient(BTPServer server, Socket client) {
        super(server, client);
    }

    @Override
    public void run() {
        super.run();
    }
    
    @Override
    protected void handleSocketInput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
