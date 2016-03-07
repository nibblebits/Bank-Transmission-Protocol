/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import BTP.BTPAccount;
import BTP.BTPClient;
import BTP.BTPCustomerClient;
import BTP.BTPKeyContainer;
import BTP.exceptions.BTPDataException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class SelectBankAccountPage extends Page {

    private Scanner scanner;
    private BTPCustomerClient btp_client;

    public SelectBankAccountPage(BankClient client) {
        super(client);
        this.scanner = client.getScanner();
        this.btp_client = (BTPCustomerClient) client.getBTPClient();
    }

    public void selectBankAccount() {
        BTPAccount selected_account = null;
        try {
            // Request the bank accounts from the server.
            BTPAccount[] accounts = btp_client.getBankAccounts();
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
                            selected_account = account;
                        }
                    }

                    if (selected_account != null) {
                        System.out.println("Account: " + account_no + " selected");

                    } else {
                        System.out.println("The account " + account_no + " is not listed.");
                    }
                } while (selected_account == null);

            }

        } catch (IOException ex) {
            Logger.getLogger(BankClientOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BTPDataException ex) {
            Logger.getLogger(BankClientOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BankClientOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        this.selectBankAccount();
    }

}
