/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPAccount;
import BTP.BTPCustomerClient;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class CustomerBankAccountMenu extends Page {

    private BTPCustomerClient btp_client;
    private Scanner scanner;
    private BTPAccount bank_account;
    public CustomerBankAccountMenu(BankClient client, BTPAccount bank_account) {
        super(client);
        this.btp_client = (BTPCustomerClient) client.getBTPClient();
        this.scanner = this.getBankClient().getScanner();
        this.bank_account = bank_account;
    }

    public void displayMenu() {
        System.out.println("Bank Account: " + bank_account.getAccountNumber());
        System.out.println("=========================");
        System.out.println("Select an option: ");
        System.out.println("1. View balance");
        System.out.println("2. Quit");
    }
    
    public void showBalance() {
        System.out.println("Balance: " + this.btp_client.getBalance(this.bank_account));
    }
    
    public boolean selectOption() {
        int option = this.scanner.nextInt();
        switch(option) {
            case 1: { // View balance
                showBalance();
            }
            break;
                
            case 2: { // Quit
                return false;
            }
        }
        
        return true;
    }
    @Override
    public void run() {
        do {
            this.displayMenu();
            if(!this.selectOption()) {
                break;
            }
        } while(true);
    }
    
}
