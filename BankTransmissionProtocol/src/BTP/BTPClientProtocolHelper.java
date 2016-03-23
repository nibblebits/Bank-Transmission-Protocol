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
public class BTPClientProtocolHelper extends BTPProtocolHelper {

    public BTPClientProtocolHelper(BTPSystem system, Socket socket) {
        super(system, socket);
    }
    
}
