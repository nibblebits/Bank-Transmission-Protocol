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
import java.io.IOException;
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
        System.out.println("2. View Transactions");
        System.out.println("3. Back to main menu");
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

    public void viewTransactions() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date_str = format.format(date);
        Date date_from = null;
        Date date_to = null;
        do {
            System.out.println("Enter the date from: e.g " + date_str);
            try {
                date_from = format.parse(this.scanner.next());
                break;
            } catch (ParseException ex) {
                System.err.println("Please enter a valid from date! Format: " + date_str);
            }
        } while (true);

        do {
            System.out.println("Enter the date to: e.g " + date_str);
            try {
                date_to = format.parse(this.scanner.next());
                break;
            } catch (ParseException ex) {
                System.err.println("Please enter a valid to date! Format: " + date_str);
            }
        } while (true);

        try {
            BTPTransaction[] transactions = this.btp_client.getTransactions(this.bank_account, date_from, date_to);
            System.out.println(
                    "Total of " + transactions.length + " transactions between "
                    + format.format(date_from)
                    + " - "
                    + format.format(date_to)
            );

            System.out.println("====TRANSACTIONS====");
            for (BTPTransaction transaction : transactions) {
                BTPAccount receiver = transaction.getReceiverAccount();
                BTPAccount sender = transaction.getSenderAccount();
                System.out.println("£" + transaction.getAmountTransferred() + " sent from "
                        + sender.getAccountNumber() + ":" + sender.getSortCode()
                        + " to " + receiver.getAccountNumber() + ":" + receiver.getSortCode());
            }
            System.out.println("====END====");
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Failed to get transactions: " + ex.getMessage());
        } catch (BTPUnknownException ex) {
            Logger.getLogger(CustomerBankAccountMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CustomerBankAccountMenu.class.getName()).log(Level.SEVERE, null, ex);
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

            case 3: { // Back to the main menu
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
