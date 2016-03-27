/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPClient;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.net.ConnectException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BankLogin extends Page {

    private Scanner scanner;
    private BTPClient client;

    public BankLogin(BankClient client) {
        super(client);
        this.scanner = this.getBankClient().getScanner();
        this.client = null;
    }

    public void showLoginTypes() {
        System.out.println("LOGIN TYPES");
        System.out.println("===============");
        System.out.println("1. Customer Login");
        System.out.println("2. Employee Login");
    }

    public void customerLogin() {
        int customer_id = -1;
        String password;
        System.out.println("Login >>");
        System.out.println("Enter your customer id: ");
        try {
            customer_id = scanner.nextInt();
        } catch (InputMismatchException ex) {
            System.err.println("The customer id has to be an integer");
            return;
        }
        System.out.println("Enter your password: ");
        password = scanner.next();

        try {
            client = this.getBankClient().getSystem().newCustomerClientFromLogin(customer_id, password);
            this.getBankClient().setBTPClient(client);
            System.out.println("Login Succesful.");
            // Show the customer main menu
            this.getBankClient().getPageNavigator().showPage(new CustomerMainMenu(this.getBankClient()));
        } catch (ConnectException ex) {
            System.err.println("The banking server appears to be offline or you are not connected to the internet.");
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission Denied: " + ex.getMessage());
        } catch (BTPDataException ex) {
            Logger.getLogger(BankClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BankClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void employeeLogin() {
        int employee_id = -1;
        String password;
        System.out.println("Login >>");
        System.out.println("Enter your employee id: ");
        try {
            employee_id = scanner.nextInt();
        } catch (InputMismatchException ex) {
            System.err.println("The employee id has to be an integer");
            return;
        }
        System.out.println("Enter your password: ");
        password = scanner.next();

        try {
            client = this.getBankClient().getSystem().newEmployeeClientFromLogin(employee_id, password);
            this.getBankClient().setBTPClient(client);
            System.out.println("Login Succesful.");
            // Show the customer main menu
            this.getBankClient().getPageNavigator().showPage(new EmployeeMainMenu(this.getBankClient()));
        } catch (ConnectException ex) {
            System.err.println("The banking server appears to be offline or you are not connected to the internet.");
        } catch (BTPPermissionDeniedException ex) {
            System.err.println("Permission Denied: " + ex.getMessage());
        } catch (BTPDataException ex) {
            Logger.getLogger(BankClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BankClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void login() {
        this.showLoginTypes();
        System.out.println("Please select what login you would like to do: ");
        int login_type = this.scanner.nextInt();
        if (login_type == 1) {
            this.customerLogin();
        } else if (login_type == 2) {
            this.employeeLogin();
        }
    }

    @Override
    public void run() {
        login();
    }

}
