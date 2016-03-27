/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class BTPServerTransferClient extends BTPServerClient {

    private BTPBank authenticated_bank;

    public BTPServerTransferClient(BTPSystem system, BTPServer server, Socket client) throws IOException {
        super(system, server, client);
        this.authenticated_bank = null;
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected void authenticate() throws Exception {
        String sort_code = this.getBufferedReader().readLine();
        String auth_code = this.getBufferedReader().readLine();
        try {
            ArrayList<BTPBank> trusted_banks = this.getSystem().getTrustedBanks();
            for (BTPBank bank : trusted_banks) {
                if (bank.getSortcode().equals(sort_code) && bank.getAuthCode().equals(auth_code)) {
                    this.setAuthenticated(true);
                    this.authenticated_bank = bank;
                    break;
                }
            }

            if (this.isAuthenticated()) {
                this.getPrintStream().write(BTPResponseCode.ALL_OK);
                System.out.println("Transfer client authenticated");
            } else {
                throw new BTP.exceptions.BTPPermissionDeniedException("Autentication denied. You are not authorized");
            }
        } catch (Exception ex) {
            this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
        }

    }

    @Override
    protected void handleOperation(int opcode) throws IOException {
        switch (opcode) {
            case BTPOperation.TRANSFER: {
                try {
                    int account_from_no = Integer.parseInt(this.getBufferedReader().readLine());
                    BTPAccount account_to = this.getProtocolHelper().readAccountFromSocket(true);
                    double amount = Double.parseDouble(this.getBufferedReader().readLine());
                    if(this.getServer().getEventHandler().getBankAccount(
                            new GetBankAccountEvent(this, account_to.getAccountNumber())) == null) {
                        throw new BTP.exceptions.BTPAccountNotFoundException(
                                "The bank account " + account_to.getAccountNumber() + " could not be found");
                    }
                    this.getProtocolHelper().handleTransferEnquiry(
                            new BTPAccount(
                                    account_from_no,
                                    this.authenticated_bank.getSortcode(),
                                    null,
                                    null), account_to, amount);
                    this.getPrintStream().write(BTPResponseCode.ALL_OK);
                } catch (Exception ex) {
                    this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
                }
            }
            break;
        }
    }

}
