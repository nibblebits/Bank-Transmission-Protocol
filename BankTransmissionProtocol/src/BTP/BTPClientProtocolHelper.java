/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import BTP.exceptions.BTPUnknownException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class BTPClientProtocolHelper extends BTPProtocolHelper {

    public BTPClientProtocolHelper(BTPSystem system, BTPClient client, BufferedReader input, PrintStream output) {
        super(system, client, input, output);
    }

    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) throws BTPPermissionDeniedException, BTPDataException, Exception {
        this.getPrintStream().write(BTPOperation.TRANSFER);
        this.getPrintStream().println(Integer.toString(account_from.getAccountNumber()));
        this.writeAccountToSocket(account_to, true);
        this.getPrintStream().println(Double.toString(amount));
        int response = this.getBufferedReader().read();
        if (response != BTPResponseCode.ALL_OK) {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
    }

    public double getBalance(BTPAccount account) throws BTPPermissionDeniedException, BTPDataException, Exception {
        this.getPrintStream().write(BTPOperation.GET_BALANCE);
        this.getPrintStream().println(account.getAccountNumber());
        int response = this.getBufferedReader().read();
        if (response == BTPResponseCode.ALL_OK) {
            return Double.valueOf(this.getBufferedReader().readLine());
        } else {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }

        return 0;
    }

    public BTPTransaction[] getTransactions(BTPAccount account, Date from, Date to) throws BTPPermissionDeniedException, BTPDataException, BTPUnknownException, IOException, Exception {
        BTPTransaction[] transactions = null;
        this.getPrintStream().write(BTPOperation.GET_TRANSACTIONS);
        this.getPrintStream().println(account.getAccountNumber());
        this.getPrintStream().println(from.getTime());
        this.getPrintStream().println(to.getTime());
        int response = this.getBufferedReader().read();
        if (response == BTPResponseCode.ALL_OK) {
            int total_transactions = this.getBufferedReader().read();
            transactions = new BTPTransaction[total_transactions];

            for (int i = 0; i < total_transactions; i++) {
                transactions[i] = this.readTransactionFromSocket();
            }
        } else {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
        return transactions;
    }
}
