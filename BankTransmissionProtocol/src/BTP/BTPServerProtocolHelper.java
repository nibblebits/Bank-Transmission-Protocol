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
public class BTPServerProtocolHelper extends BTPProtocolHelper {

    private BTPServer server;

    public BTPServerProtocolHelper(BTPSystem system, BufferedReader input, PrintStream output, Socket socket, BTPServer server) {
        super(system, input, output, socket);
        this.server = server;
    }


    public void handleTransferEnquiry(BTPAccount account_from, BTPAccount account_to, double amount) {

    }

    public double handleBalanceEnquiry(BTPAccount account) {
        return 0;
    }

    public BTPTransaction[] handleTransactionsEnquiry(BTPAccount account, Date from, Date to) {
        return null;
    }
}
