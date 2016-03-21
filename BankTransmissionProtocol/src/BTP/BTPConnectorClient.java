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
public class BTPConnectorClient extends BTPClient {

    public BTPConnectorClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);
    }
    
}
