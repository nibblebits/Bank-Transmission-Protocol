/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.*;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BankClientOld {

    /**
     * @param args the command line arguments
     */
    private BTPSystem system = null;
    private Scanner scanner = null;
    private BTPCustomerClient client = null;
    BTPAccount selected_account = null;
    private boolean running = true;

    public BTPSystem getSystem() {
        return this.system;
    }
    
    public Scanner getScanner() {
        return this.scanner;
    }
    public void showWelcomeMessage() {
        System.out.println("Welcome to Dans bank.");
    }

    public void showMainMenuOptions() {
        System.out.println("OPTIONS =====");
        System.out.println("1. Select a bank account");
        System.out.println("2. Quit");
    }

    public void selectBankAccount() {
        try {
            // Request the bank accounts from the server.
            BTPAccount[] accounts = client.getBankAccounts();
            System.out.println("ACCOUNTS");
            System.out.println("=================");
            if (accounts.length == 0) {
                System.out.println("No accounts registered");
            }

            if (accounts.length != 0) {
                do {
                    for (BTPAccount account : accounts) {
                        BTPKeyContainer extra_detail = account.getExtraDetail();
                        System.out.println(account.getAccountNumber());
                        for (int i = 0; i < extra_detail.getTotalKeys(); i++) {
                            System.out.println("\t" + extra_detail.getKey(i).getIndexName()
                                    + "=" + extra_detail.getKey(i).getValue());
                        }
                    }
                    System.out.println("Enter the account number to select: ");
                    int account_no = scanner.nextInt();
                    for (BTPAccount account : accounts) {
                        if (account.getAccountNumber() == account_no) {
                            this.selected_account = account;
                        }
                    }

                    if (this.selected_account != null) {
                        System.out.println("Account: " + account_no + " selected");

                    } else {
                        System.out.println("The account " + account_no + " is not listed.");
                    }
                } while (this.selected_account == null);

            }

        } catch (IOException ex) {
            Logger.getLogger(BankClientOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BTPDataException ex) {
            Logger.getLogger(BankClientOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BankClientOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processMainMenuOptions() {
        int option = scanner.nextInt();
        switch (option) {
            case 1: { // Select a bank account
                selectBankAccount();
            }
            break;

            case 2: { // Quit 
                System.out.println("Thank you.");
                if (client != null) {
                    try {
                        // Shutdown the client
                        client.shutdown();

                    } catch (IOException ex) {
                        Logger.getLogger(BankClientOld.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                running = false;
            }
        }
    }

    public boolean login() {
        int customer_id;
        String password;
        System.out.println("Login >>");
        System.out.println("Enter your customer id: ");
        customer_id = scanner.nextInt();
        System.out.println("Enter your password: ");
        password = scanner.next();

        try {
            client = system.newCustomerClientFromLogin(customer_id, password);
            System.out.println("Login Succesful.");
            return true;
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission Denied: " + ex.getMessage());

        } catch (BTPDataException ex) {
            Logger.getLogger(BankClientOld.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BankClientOld.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void init() {
        this.scanner = new Scanner(System.in);
        this.system = new BTPSystem(new BTPBank("22-33-44", "127.0.0.1", 4444));
    }

    public void run() {
        showWelcomeMessage();
        if (login()) {
            while (running) {
                if (this.selected_account == null) {
                    showMainMenuOptions();
                    processMainMenuOptions();
                } else {
         
                }
            }
        }
    }


}
