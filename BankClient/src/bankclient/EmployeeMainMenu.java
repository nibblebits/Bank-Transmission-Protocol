/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPAccount;
import BTP.BTPAccountType;
import BTP.BTPCustomer;
import BTP.BTPEmployeeClient;
import BTP.BTPKey;
import BTP.BTPKeyContainer;
import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import BTP.exceptions.BTPUnknownException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class EmployeeMainMenu extends Page {

    private BTPEmployeeClient btp_client;
    private Scanner scanner;

    public EmployeeMainMenu(BankClient client) {
        super(client);
        this.btp_client = (BTPEmployeeClient) client.getBTPClient();
        this.scanner = client.getScanner();
    }

    public void displayMenu() {
        System.out.println("Select an option: ");
        System.out.println("1. Make a transfer");
        System.out.println("2. Select a bank account");
        System.out.println("3. Create a customer");
        System.out.println("4. Create a bank account");
        System.out.println("5. List customer bank accounts");
        System.out.println("6. Get cutomer information");
        System.out.println("7. Logout");
    }

    public void displayCustomer(BTPCustomer customer) {
        System.out.println("Full Name: " + customer.getFullname());
        if (customer.getExtraDetail().getTotalKeys() > 0) {
            System.out.println("Extra information");
            System.out.println("----------------------");
            for (int i = 0; i < customer.getExtraDetail().getTotalKeys(); i++) {
                BTPKey key = customer.getExtraDetail().getKey(i);
                System.out.println(key.getIndexName() + ": " + key.getValue());
            }
        }
    }

    public void selectABankAccount() {
        try {
            System.out.println("Enter the bank account number to select: ");
            int account_no = this.scanner.nextInt();
            // Remove the line terminator left over.
            this.scanner.nextLine();
            BTPAccount account = this.btp_client.getBankAccount(account_no);
            this.getBankClient().getPageNavigator().showPage(new EmployeeBankAccountMenu(this.getBankClient(), account));
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission Denied: " + ex.getMessage());
        } catch (BTPAccountNotFoundException ex) {
            System.err.println("The account could not be found");
        } catch (BTPDataException ex) {
            System.err.println("Data error: " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void createACustomer() {
        String customer_title;
        String customer_firstname;
        String customer_middlename;
        String customer_surname;
        String customer_password;
        BTPKeyContainer extra = new BTPKeyContainer();
        System.out.println("Please enter the customers full name: e.g MR PAUL MICHEAL OBRIEN");
        customer_title = this.scanner.next();
        customer_firstname = this.scanner.next();
        customer_middlename = this.scanner.next();
        customer_surname = this.scanner.next();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        System.out.println("Enter the password for this customer: ");
        customer_password = this.scanner.nextLine();
        String response;
        do {
            System.out.println("Would you like to add extra detail about this customer? Y/N");
            response = this.scanner.nextLine();
            if (response.toUpperCase().equals("Y")) {
                System.out.println("Enter the detail name: e.g Company name");
                String key = this.scanner.nextLine();
                System.out.println("Now enter the detail value: e.g Mcdonalds");
                String value = this.scanner.nextLine();
                extra.addKey(new BTPKey(key, value));
            }
        } while (!response.equals("N"));

        int customer_id;
        try {
            customer_id = this.btp_client.createCustomer(
                    new BTPCustomer(-1,
                            customer_title,
                            customer_firstname,
                            customer_middlename,
                            customer_surname,
                            extra), customer_password);
            System.out.println("Customer created succesfully! Customer Id: " + customer_id);
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission Denied: " + ex.getLocalizedMessage());
        } catch (BTPDataException ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BTPUnknownException ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCustomerInformation() {
        System.out.println("Enter the customer id to get information for: ");
        int customer_id = this.scanner.nextInt();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        BTPCustomer customer;
        try {
            customer = this.btp_client.getCustomer(customer_id);
            displayCustomer(customer);
            System.out.println("-------------------------");
            listBankAccountsOfCustomer(customer_id);
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission denied: " + ex.getMessage());
        } catch (BTPAccountNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listBankAccountsOfCustomer(int customer_id) {
        try {
            BTPAccount[] accounts = this.btp_client.getBankAccounts(customer_id);
            if (accounts.length == 0) {
                System.err.println("This customer does not have any bank accounts");
            } else {
                System.out.println("Displaying " + accounts.length + " accounts");
                for (BTPAccount account : accounts) {
                    System.out.println("Account number: " + account.getAccountNumber());
                    // Everyones account will have the same sort code but we may as well display it
                    System.out.println("Sortcode: " + account.getSortCode());
                    System.out.println("Account type: " + account.getAccountType().getName());
                    if (account.getExtraDetail().getTotalKeys() != 0) {
                        System.out.println("Extra details");
                        for (int i = 0; i < account.getExtraDetail().getTotalKeys(); i++) {
                            BTPKey key = account.getExtraDetail().getKey(i);
                            System.out.println("\t" + key.getIndexName() + ": " + key.getValue());
                        }
                    }
                    System.out.println("---------------------");
                }
            }
        } catch (BTPPermissionDeniedException ex) {
            System.err.println(ex.getMessage());
        } catch (BTPAccountNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (BTPDataException ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BTPUnknownException ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void transfer() {
        int account_no_to_send_from = 0;
        int account_no_to_send_to = 0;
        String account_sortcode_to_send_to = "";
        double amount = 0;
        System.out.println("Enter the account number to send from: ");
        account_no_to_send_from = this.scanner.nextInt();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        System.out.println("Enter the account number to send to: ");
        account_no_to_send_to = this.scanner.nextInt();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        System.out.println("Enter the sortcode of the account to send to: ");
        account_sortcode_to_send_to = this.scanner.next();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        System.out.println("Enter the amount to transfer: ");
        amount = this.scanner.nextDouble();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        try {
            this.btp_client.transfer(new BTPAccount(account_no_to_send_from,
                    this.getBankClient().getSystem().getOurBank().getSortcode(), null, null),
                    new BTPAccount(account_no_to_send_to, account_sortcode_to_send_to, null, null),
                    amount);
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission Denied: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Transfer failed: " + ex.getMessage());
        }
    }

    public void listAccountTypes(BTPAccountType[] account_types) {
        System.out.println("Bank Account Types");
        System.out.println("-----------------------");
        for (BTPAccountType account_type : account_types) {
            System.out.println(account_type.getName());
        }
        System.out.println("-----------------------");
    }

    public void createBankAccount() {
        System.out.println("Enter the customer id to create a bank account for: ");
        int customer_id = this.scanner.nextInt();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        this.createBankAccount(customer_id);
    }

    public void createBankAccount(int customer_id) {
        BTPAccountType type = null;
        BTPKeyContainer extra = new BTPKeyContainer();
        BTPAccountType[] account_types;
        String account_type_name;
        try {
            account_types = this.btp_client.getBankAccountTypes();
            do {
                listAccountTypes(account_types);
                System.out.println("Enter the account type: ");
                account_type_name = this.scanner.nextLine();
                // Find the account type based on the name inputted.
                for (BTPAccountType account_type : account_types) {
                    if (account_type.getName().equalsIgnoreCase(account_type_name)) {
                        type = account_type;
                        break;
                    }
                }
                if (type == null) {
                    System.err.println("Please choose a valid account type");
                }
            } while (type == null);
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission Denied: " + ex.getMessage());
        } catch (BTPDataException ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EmployeeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        // if(isValidAccountType())

    }

    public boolean selectOption() {
        int option = this.scanner.nextInt();
        // Remove the line terminator left over.
        this.scanner.nextLine();
        switch (option) {
            case 1: {// Make a transfer
                transfer();
            }
            break;
            case 2: { // Select a bank account
                selectABankAccount();
            }
            break;
            case 3: { // Create a customer
                createACustomer();
            }
            break;
            case 4: { // Create a bank account
                createBankAccount();
            }
            break;
            case 5: { // List customer bank accounts

            }
            break;
            case 6: { // Get customer information
                getCustomerInformation();
            }
            break;
            case 7: { // Logout
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
