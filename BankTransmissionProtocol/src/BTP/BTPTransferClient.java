/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import BTP.exceptions.BTPUnknownException;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Daniel
 */
public class BTPTransferClient extends BTPConnectorClient {

    public BTPTransferClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);
    }

    public boolean login(String bank_sortcode, String auth_code) throws
            BTPPermissionDeniedException, BTPAccountNotFoundException, BTPDataException, BTPUnknownException, Exception {
        this.getPrintStream().println(bank_sortcode);
        this.getPrintStream().println(auth_code);
        int response = this.getBufferedReader().read();
        if (response == BTPResponseCode.ALL_OK) {
            this.setAuthenticated(true);
            return true;
        } else {
            String message = this.getBufferedReader().readLine();
            this.getSystem().throwExceptionById(response, message);
        }
        return false;
    }

    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount)
            throws BTPPermissionDeniedException, BTPAccountNotFoundException, BTPDataException, BTPUnknownException, Exception {
        if (this.isAuthenticated()) {
            this.getProtocolHelper().transfer(account_from, account_to, amount);
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("Authentication must happen before transfeering funds");
        }
    }

}
