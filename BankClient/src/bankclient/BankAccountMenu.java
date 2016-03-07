/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPCustomerClient;

/**
 *
 * @author Daniel
 */
public class BankAccountMenu extends Page {

    private BTPCustomerClient btp_client;
    public BankAccountMenu(BankClient client) {
        super(client);
        this.btp_client = (BTPCustomerClient) client.getBTPClient();
    }

    public void displayMenu() {
        //System.out.println("Bank Account: " + this.btp_client);
    }
    @Override
    public void run() {
        
    }
    
}
