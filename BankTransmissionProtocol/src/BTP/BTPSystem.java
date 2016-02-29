/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPBankNotFoundException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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
    
    public BTPCustomerClient newCustomerClientFromLogin(int customer_id, String password) throws IOException, BTPPermissionDeniedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.bank.getAddress(), this.bank.getPort()), 5000);
        BTPCustomerClient client = new BTPCustomerClient(socket);
        if (client.login(customer_id, password)) {
            return client;
        }
        
        throw new BTPPermissionDeniedException("Failed to login as a customer permission denied.");
    }
    
    public BTPEmployeeClient newEmployeeClientFromLogin(int employee_id, String password) throws IOException, BTPPermissionDeniedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.bank.getAddress(), this.bank.getPort()), 5000);
        BTPEmployeeClient client = new BTPEmployeeClient(socket);
        if(client.login(employee_id, password)) {
            return client;
        }
        
         throw new BTPPermissionDeniedException("Failed to login as an employee permission denied.");
    }
    
    public BTPAdministratorClient newAdministrtorClientFromLogin(int admin_id, String password) throws IOException, BTPPermissionDeniedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.bank.getAddress(), this.bank.getPort()), 5000);
        BTPAdministratorClient client = new BTPAdministratorClient(socket);
        if (client.login(admin_id, password)) {
            return client;
        }
        
         throw new BTPPermissionDeniedException("Failed to login as an administrator permission denied.");
    }
    
    public BTPTransferClient newTransferClient(String bank_sortcode) throws BTPBankNotFoundException, IOException, BTPPermissionDeniedException {
        BTPBank receiver_bank = this.getTrustedBank(bank_sortcode);
        if (bank == null) {
            throw new BTPBankNotFoundException("Transfer is not possible as their is no bank could be found with the sortcode " + bank_sortcode);
        }
        
        // We must connect to the receiver bank to request a transfer
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(receiver_bank.getAddress(), receiver_bank.getPort()), 5000);
        BTPTransferClient client = new BTPTransferClient(socket);
        // We must login to the server with our banks sortcode. Both banks share the same auth code with eachother.
        if(client.login(this.getOurBank().getSortcode(), receiver_bank.getAuthCode())) {
            return client;
        }
        
        throw new BTPPermissionDeniedException("Failed to login as a transfer client permission denied.");
    }
    
    public BTPServer newServer(BTPServerEventHandler eventHandler) {
        return new BTPServer(this, eventHandler);
    }
    
    public BTPBank getOurBank() {
        return this.bank;
    }
    
    public ArrayList<BTPBank> getTrustedBanks() {
        return this.trusted_bank;
    }
    
    public BTPBank getTrustedBank(String sortcode) {
        for (BTPBank bank : this.trusted_bank) {
            if (bank.getSortcode().equals(sortcode)) {
                return bank;
            }
        }
        return null;
    }
    
    public void addTrustedBank(BTPBank bank) {
        this.trusted_bank.add(bank);
    }
}
