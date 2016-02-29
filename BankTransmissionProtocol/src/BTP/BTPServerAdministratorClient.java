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
public class BTPServerAdministratorClient extends BTPServerEmployeeClient {

    public BTPServerAdministratorClient(BTPServer server, Socket client) throws IOException {
        super(server, client);
    }
    
    @Override
    public void run() {
        super.run();
    }
    
    @Override
    protected void handleSocketInput() {
         throw new UnsupportedOperationException("Not supported yet.");
    }
}
