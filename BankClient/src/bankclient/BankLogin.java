/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPClient;
import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
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

    public void login() {
        int customer_id;
        String password;
        System.out.println("Login >>");
        System.out.println("Enter your customer id: ");
        customer_id = scanner.nextInt();
        System.out.println("Enter your password: ");
        password = scanner.next();

        try {
            client = this.getBankClient().getSystem().newCustomerClientFromLogin(customer_id, password);
            this.getBankClient().setBTPClient(client);
            System.out.println("Login Succesful.");
            // Show the customer main menu
            this.getBankClient().getPageNavigator().showPage(new CustomerMainMenu(this.getBankClient()));
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

    @Override
    public void run() {
        login();
    }

}
