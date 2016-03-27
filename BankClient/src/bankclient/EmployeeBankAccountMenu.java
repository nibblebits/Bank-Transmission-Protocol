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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private SimpleDateFormat default_date_format;

    public EmployeeBankAccountMenu(BankClient client, BTPAccount bank_account) {
        super(client);
        this.bank_account = bank_account;
        this.scanner = this.getBankClient().getScanner();
        this.btp_client = (BTPEmployeeClient) this.getBankClient().getBTPClient();
        this.default_date_format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public void deposit() {
        System.out.println("Enter the amount to deposit: ");
        double amount = Double.parseDouble(this.scanner.nextLine().replace("£", ""));
        try {
            this.btp_client.transfer(this.getBankClient().getSystem().getOurBank().getBankAccount(),
                    bank_account, amount);
        } catch (Exception ex) {
            System.err.println("Deposit failed: " + ex.getMessage());
        }
    }

    public void withdraw() {
        System.out.println("Enter the amount to withdraw: ");
        double amount = Double.parseDouble(this.scanner.nextLine().replace("£", ""));
        try {
            this.btp_client.transfer(bank_account,
                    this.getBankClient().getSystem().getOurBank().getBankAccount(), amount);
        } catch (Exception ex) {
            System.err.println("Withdraw failed: " + ex.getMessage());
        }
    }

    public void showMenu() {
        System.out.println("Select an option: ");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. More options");
        System.out.println("4. Back");
    }

    public void accessMore() {
        this.getBankClient().getPageNavigator().showPage(new CustomerBankAccountMenu(this.getBankClient(), this.bank_account));
    }

    public boolean processOption() {
        int option = this.scanner.nextInt();
        // Clear new line left over by previous scan.
        scanner.nextLine();
        switch (option) {
            case 1: // Deposit
                this.deposit();
                break;
            case 2: // Withdraw
                this.withdraw();
                break;
            case 3:  // Access more
                this.accessMore();
                break;
            case 4: // Back
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
