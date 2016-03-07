/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class CustomerMainMenu extends Page {

    private Scanner scanner;
    private int selected_option;

    public CustomerMainMenu(BankClient client) {
        super(client);
        this.scanner = client.getScanner();
        this.selected_option = 0;
    }

    public void showMainMenuOptions() {
        System.out.println("OPTIONS =====");
        System.out.println("1. Select a bank account");
        System.out.println("2. Quit");
    }

    public boolean processMainMenuOptions() {
        int option = scanner.nextInt();
        this.selected_option = option;
        switch (option) {
            case 1: { // Select a bank account 
                this.getBankClient().getPageNavigator().showPage(new SelectBankAccountPage(this.getBankClient()));
            }
            break;
            
            case 2: { // Quit 
                System.out.println("Thank you.");
                return false;
            }
        }

        return true;
    }

    @Override
    public void run() {
        do {
            this.showMainMenuOptions();
            if (!this.processMainMenuOptions()) {
                break;
            }
        } while (true);
    }

}
