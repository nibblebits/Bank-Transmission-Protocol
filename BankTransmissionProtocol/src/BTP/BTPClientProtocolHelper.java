/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class BTPClientProtocolHelper extends BTPProtocolHelper {

    public BTPClientProtocolHelper(BTPSystem system, BufferedReader input, PrintStream output, Socket socket) {
        super(system, input, output, socket);
    }
    
    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) {
        
    }
    
    public double getBalance(BTPAccount account) {
        return 0;
    }
    
    public BTPTransaction[] getTransactions(BTPAccount account, Date from, Date to) {
        return null;
    }
}
