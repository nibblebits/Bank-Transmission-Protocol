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
public abstract class BTPProtocolHelper {
    private BTPSystem system;
    private Socket socket;
    public BTPProtocolHelper(BTPSystem system, Socket socket) {
        this.system = system;
        this.socket = socket;
    }
    
    public BTPSystem getSystem() {
        return this.system;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
}
