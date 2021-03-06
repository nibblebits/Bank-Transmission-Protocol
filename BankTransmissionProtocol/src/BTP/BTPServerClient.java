/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public abstract class BTPServerClient extends BTPClient implements Runnable {

    private final BTPServer server;
    private BTPServerProtocolHelper protocol_helper;

    public BTPServerClient(BTPSystem system, BTPServer server, Socket client) throws IOException {
        super(system, client);
        this.server = server;
        this.protocol_helper = new BTPServerProtocolHelper(system, this,
                this.getBufferedReader(), this.getPrintStream(), server);
    }

    @Override
    public void run() {
        try {
            this.authenticate();
        } catch (Exception ex) {
            // They failed to authenticate so return
            return;
        }

        while (true) {

            try {
                if (!this.handleSocketInput()) {
                    // Client wants to shutdown so return
                    return;
                }
            } catch (Exception ex) {
                // Something went wrong? Could be anything so log the error and then return
                Logger.getLogger(BTPServerClient.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
    }

    public boolean handleSocketInput() throws IOException, Exception {
        int opcode = this.getBufferedReader().read();
        // Client has signalled that they wish to shutdown
        if (opcode == BTPOperation.SHUTDOWN) {
            return false;
        }
        // Pass the operation to the child
        this.handleOperation(opcode);
        return true;
    }

    protected abstract void authenticate() throws Exception;

    protected abstract void handleOperation(int opcode) throws Exception;

    public synchronized BTPServer getServer() {
        return this.server;
    }

    @Override
    protected void exchangeClientInformation() throws IOException {
        int peer_build = this.getBufferedReader().read();
        this.setPeersClientBuild(peer_build);
        this.getPrintStream().write(this.getSystem().getBuildVersion());
        this.getPrintStream().flush();
    }

    @Override
    public void shutdown() throws IOException {
        if (this.getSocket() != null) {
            this.getSocket().close();
        }
    }
    
    public BTPServerProtocolHelper getProtocolHelper() {
        return this.protocol_helper;
    }
}
