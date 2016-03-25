/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BTPServerEmployeeClient extends BTPServerClient {

    private BTPEmployee employee;

    public BTPServerEmployeeClient(BTPSystem system, BTPServer server, Socket client) throws IOException {
        super(system, server, client);
        this.employee = null;
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected void authenticate() throws Exception {
        int employee_id = Integer.parseInt(this.getBufferedReader().readLine());
        String password = this.getBufferedReader().readLine();
        try {
            this.getServer().getEventHandler().employeeLogin(new EmployeeLoginEvent(this, employee_id, password));
            this.setAuthenticated(true);
            this.getPrintStream().write(BTPResponseCode.ALL_OK);
        } catch (Exception ex) {
            this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
        }
    }

    @Override
    protected void handleOperation(int opcode) throws IOException {
        switch (opcode) {
            case BTPOperation.TRANSFER: {
                int account_from_no = Integer.parseInt(this.getBufferedReader().readLine());

                try {
                    BTPAccount account_from = this.getBankAccount(account_from_no);
                    BTPAccount account_to = this.getProtocolHelper().readAccountFromSocket(true);
                    if (account_from == null) {
                        throw new BTP.exceptions.BTPAccountNotFoundException("The account you are sending from does not exist");
                    }

                    double amount = Double.parseDouble(this.getBufferedReader().readLine());
                    this.getProtocolHelper().handleTransferEnquiry(account_from, account_to, amount);
                } catch (Exception ex) {
                    this.getProtocolHelper().sendExceptionResponseOverSocket(ex);
                }
                break;
            }
        }
    }

    public BTPEmployee getEmployee() {
        return this.employee;
    }

    protected BTPAccount getBankAccount(int id) throws BTPPermissionDeniedException, BTPDataException, Exception {
        return this.getServer().getEventHandler().getBankAccount(new GetBankAccountEvent(this, id));
    }
}
