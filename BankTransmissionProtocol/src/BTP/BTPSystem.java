/*
 * To change this license header, choose License Headers inroject Properties.
 * To change this template file, choose Tools | Templates P
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPBankNotFoundException;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class BTPSystem {

    private BTPBank bank;
    private ArrayList<BTPBank> trusted_bank;

    public BTPSystem(BTPBank bank) {
        this.bank = bank;
        this.trusted_bank = new ArrayList<BTPBank>();
    }

    public synchronized BTPCustomerClient newCustomerClientFromLogin(int customer_id, String password) throws IOException, BTPPermissionDeniedException, BTPDataException, Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.bank.getAddress(), this.bank.getPort()), 5000);
        BTPCustomerClient client = new BTPCustomerClient(this, socket);
        client.login(customer_id, password);
        return client;
    }

    public synchronized BTPEmployeeClient newEmployeeClientFromLogin(int employee_id, String password) throws IOException, BTPPermissionDeniedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.bank.getAddress(), this.bank.getPort()), 5000);
        BTPEmployeeClient client = new BTPEmployeeClient(this, socket);
        if (client.login(employee_id, password)) {
            return client;
        }

        throw new BTPPermissionDeniedException("Failed to login as an employee permission denied.");
    }

    public synchronized BTPAdministratorClient newAdministrtorClientFromLogin(int admin_id, String password) throws IOException, BTPPermissionDeniedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.bank.getAddress(), this.bank.getPort()), 5000);
        BTPAdministratorClient client = new BTPAdministratorClient(this, socket);
        if (client.login(admin_id, password)) {
            return client;
        }

        throw new BTPPermissionDeniedException("Failed to login as an administrator permission denied.");
    }

    public synchronized BTPTransferClient newTransferClient(String bank_sortcode) throws BTPBankNotFoundException, IOException, BTPPermissionDeniedException {
        BTPBank receiver_bank = this.getTrustedBank(bank_sortcode);
        if (bank == null) {
            throw new BTPBankNotFoundException("Transfer is not possible as their is no bank could be found with the sortcode " + bank_sortcode);
        }

        // We must connect to the receiver bank to request a transfer
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(receiver_bank.getAddress(), receiver_bank.getPort()), 5000);
        BTPTransferClient client = new BTPTransferClient(this, socket);
        // We must login to the server with our banks sortcode. Both banks share the same auth code with eachother.
        if (client.login(this.getOurBank().getSortcode(), receiver_bank.getAuthCode())) {
            return client;
        }

        throw new BTPPermissionDeniedException("Failed to login as a transfer client permission denied.");
    }

    public synchronized BTPServer newServer(BTPServerEventHandler eventHandler) {
        return new BTPServer(this, eventHandler);
    }

    public synchronized BTPBank getOurBank() {
        return this.bank;
    }

    public synchronized ArrayList<BTPBank> getTrustedBanks() {
        return this.trusted_bank;
    }

    public synchronized BTPBank getTrustedBank(String sortcode) {
        for (BTPBank bank : this.trusted_bank) {
            if (bank.getSortcode().equals(sortcode)) {
                return bank;
            }
        }
        return null;
    }

    public synchronized void addTrustedBank(BTPBank bank) {
        this.trusted_bank.add(bank);
    }

    public synchronized void throwExceptionById(int exception_id, String message) throws BTPPermissionDeniedException, BTPDataException, Exception {
        if (exception_id == BTPResponseCode.PERMISSION_DENIED_EXCEPTION) {
            throw new BTP.exceptions.BTPPermissionDeniedException(message);
        } else if (exception_id == BTPResponseCode.DATA_EXCEPTION) {
            throw new BTP.exceptions.BTPDataException(message);
        } else if (exception_id == BTPResponseCode.BANK_NOT_FOUND_EXCEPTION) {
            throw new BTP.exceptions.BTPBankNotFoundException(message);
        } else if (exception_id == BTPResponseCode.INVALID_ACCOUNT_TYPE_EXCEPTION) {
            throw new BTP.exceptions.BTPInvalidAccountTypeException(message);
        } else if (exception_id == BTPResponseCode.ACCOUNT_NOT_FOUND_EXCEPTION) {
            throw new BTP.exceptions.BTPAccountNotFoundException(message);
        } else if (exception_id == BTPResponseCode.UNKNOWN_EXCEPTION) {
            throw new BTP.exceptions.BTPUnknownException(message);
        } else {
            throw new BTP.exceptions.BTPUnknownException(message);
        }
    }

    public int getBuildVersion() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("version.properties")));
        int build = -1;
        String line;
        while (true) {
            line = reader.readLine();
            if (line.startsWith("BUILD=")) {
                line = line.replace("BUILD=", "");
                build = Integer.parseInt(line);
                break;
            }
        }
        return build;
    }
}
