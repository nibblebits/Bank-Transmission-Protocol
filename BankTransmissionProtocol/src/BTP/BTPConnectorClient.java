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
public abstract class BTPConnectorClient extends BTPClient {

    public BTPConnectorClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);
    }

    @Override
    protected void exchangeClientInformation() throws IOException {
        /* To avoid code being very ugly and confusing I believe this is a better way of sending the
         build version. If I can think of a better way I will change it in the future.
        Inheritance appears to be confusing from a programmer who looks at the code.
        This is why I didn't extend the exchangeClientInformation method in the children.
        This may change in the future if I can think of a cleaner way of doing things.
        */
        if (this instanceof BTPCustomerClient) {
            this.getPrintStream().write(BTPClient.Customer);
        } else if(this instanceof BTPEmployeeClient) {
            this.getPrintStream().write(BTPClient.Employee);
        } else if(this instanceof BTPAdministratorClient) {
            this.getPrintStream().write(BTPClient.Administrator);
        } else if(this instanceof BTPTransferClient) {
            this.getPrintStream().write(BTPClient.Transfer);
        }
        // Send our BTP build version
        this.getPrintStream().write(this.getSystem().getBuildVersion());
        this.getPrintStream().flush();

        // Retrieve our peers BTP build version
        int peer_build = this.getBufferedReader().read();
        this.setPeersClientBuild(peer_build);
    }

}
