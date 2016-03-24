/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPAccount;
import BTP.BTPCustomerClient;
import BTP.BTPTransaction;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import BTP.exceptions.BTPUnknownException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
public class CustomerBankAccountMenu extends Page {

    private BTPCustomerClient btp_client;
    private Scanner scanner;
    private BTPAccount bank_account;
    private SimpleDateFormat default_date_format;
    
    public CustomerBankAccountMenu(BankClient client, BTPAccount bank_account) {
        super(client);
        this.btp_client = (BTPCustomerClient) client.getBTPClient();
        this.scanner = this.getBankClient().getScanner();
        this.bank_account = bank_account;
        this.default_date_format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public void displayMenu() {
        System.out.println("Bank Account: " + bank_account.getAccountNumber());
        System.out.println("=========================");
        System.out.println("Select an option: ");
        System.out.println("1. View balance");
        System.out.println("2. View Transactions");
        System.out.println("3. Make a transfer");
        System.out.println("4. Back to main menu");
    }

    public void showBalance() {
        try {
            System.out.println("Balance: " + this.btp_client.getBalance(this.bank_account));
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission denied: " + ex.getMessage());
        } catch (BTPDataException ex) {
            Logger.getLogger(CustomerBankAccountMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CustomerBankAccountMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void outputTransactions(PrintStream out, Date date_from, Date date_to) {
        try {
            BTPTransaction[] transactions = this.btp_client.getTransactions(this.bank_account, date_from, date_to);
            out.println(
                    "Total of " + transactions.length + " transactions between "
                    + default_date_format.format(date_from)
                    + " - "
                    + default_date_format.format(date_to)
            );

            out.println("====TRANSACTIONS====");
            for (BTPTransaction transaction : transactions) {
                BTPAccount receiver = transaction.getReceiverAccount();
                BTPAccount sender = transaction.getSenderAccount();
                out.println("£" + transaction.getAmountTransferred() + " sent from "
                        + sender.getAccountNumber() + ":" + sender.getSortCode()
                        + " to " + receiver.getAccountNumber() + ":" + receiver.getSortCode());
            }
            out.println("====END====");
        } catch (BTPPermissionDeniedException ex) {
            out.println("Failed to get transactions: " + ex.getMessage());
        } catch (BTPUnknownException ex) {
            Logger.getLogger(CustomerBankAccountMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CustomerBankAccountMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void viewTransactions() {
        String date_str = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
        Date date_from = null;
        Date date_to = null;
        do {
            System.out.println("Enter the date from: e.g " + date_str);
            try {
                date_from = default_date_format.parse(this.scanner.next() + " 0:0:0");
                break;
            } catch (ParseException ex) {
                System.err.println("Please enter a valid from date! Format: " + date_str);
            }
        } while (true);

        do {
            System.out.println("Enter the date to: e.g " + date_str);
            try {
                date_to = default_date_format.parse(this.scanner.next() + " 23:59:59");
                break;
            } catch (ParseException ex) {
                System.err.println("Please enter a valid to date! Format: " + date_str);
            }
        } while (true);

        this.outputTransactions(System.out, date_from, date_to);
        System.out.println("Would you like to save this information to a file? Y/N");
        String response = scanner.next();
        if (response.equals("Y") || response.equals("y")) {
            System.out.println("Enter the filename: ");
            String filename = scanner.next();
            try {
                FileOutputStream f_out = new FileOutputStream(filename);
                this.outputTransactions(new PrintStream(f_out), date_from, date_to);
                f_out.close();
            } catch (IOException ex) {
                System.err.println("Woops we couldn't open the file: " + ex.getMessage());
            }
            
        }
    }

    public void transfer() {
        int account_no;
        String sortcode;
        double amount;
        System.out.println("Enter the account number to transfer to: ");
        account_no = scanner.nextInt();
        System.out.println("Enter the sortcode: ");
        sortcode = scanner.next();
        System.out.println("Enter the amount to transfer: ");
        amount = scanner.nextDouble();
        try {
            this.btp_client.transfer(this.bank_account, new BTPAccount(account_no, sortcode, null, null), amount);
            System.out.println("Transfer complete!");
        } catch (Exception ex) {
            System.err.println("Transfer failed: " + ex.getMessage());
        }
    }

    public boolean selectOption() {
        int option = this.scanner.nextInt();
        switch (option) {
            case 1: { // View balance
                showBalance();
            }
            break;

            case 2: { // View transactions
                viewTransactions();
            }
            break;

            case 3: { // Make a transfer
                transfer();
            }
            break;

            case 4: { // Back to the main menu
                return false;
            }
        }

        return true;
    }

    @Override
    public void run() {
        do {
            this.displayMenu();
            if (!this.selectOption()) {
                break;
            }
        } while (true);

    }

}
