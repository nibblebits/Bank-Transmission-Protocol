/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPAccount;
import BTP.BTPEmployeeClient;
import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPDataException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class EmployeeBankAccountMenu extends Page {

    private BTPAccount bank_account;
    private Scanner scanner;
    private BTPEmployeeClient btp_client;

    public EmployeeBankAccountMenu(BankClient client, BTPAccount bank_account) {
        super(client);
        this.bank_account = bank_account;
        this.scanner = this.getBankClient().getScanner();
        this.btp_client = (BTPEmployeeClient) this.getBankClient().getBTPClient();
    }

    public void showMenu() {
        System.out.println("Select an option: ");
        System.out.println("1. View balance");
        System.out.println("2. Make a transfer");
        System.out.println("3. View transactions");
        System.out.println("4. Deposit");
        System.out.println("5. Withdraw");
        System.out.println("6. Back");
    }

    public void viewBalance() {
        try {
            double balance = this.btp_client.getBalance(bank_account);
            System.out.println("Balance: " + balance);
        } catch (BTPAccountNotFoundException ex) {
            System.err.println("This account no longer exists.");
        } catch (BTPDataException ex) {
            System.err.println("Data problem: " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(EmployeeBankAccountMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean processOption() {
        int option = this.scanner.nextInt();
        switch (option) {
            case 1: // View balance
                viewBalance();
                break;
            case 2: // Make a transfer
                break;
            case 3: // View transactions
                break;
            case 4: // Deposit
                break;
            case 5: // Withdraw
                break;
            case 6: // Back
                return false;
        }

        return true;
    }

    @Override
    public void run() {
        while (true) {
            this.showMenu();
            if (!this.processOption()) {
                break;
            }
        }
    }

}
