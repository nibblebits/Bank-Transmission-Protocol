/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPAccountNotFoundException;
import BTP.exceptions.BTPDataException;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class BTPServerProtocolHelper extends BTPProtocolHelper {

    private BTPServer server;

    public BTPServerProtocolHelper(BTPSystem system, BTPServerClient client, BufferedReader input, PrintStream output, BTPServer server) {
        super(system, client, input, output);
        this.server = server;
    }

    public void handleTransferEnquiry(BTPAccount account_from, BTPAccount account_to, double amount) {
        // Little bit of security we don't want people sending money to themselves ;)
        if (account_from.getAccountNumber() == account_to.getAccountNumber()
                && account_from.getSortCode().equals(account_to.getSortCode())) {
            this.sendExceptionResponseOverSocket(
                    new BTP.exceptions.BTPPermissionDeniedException("You may not send money to yourself!")
            );
            return;
        }
        if (account_to.getSortCode().equals(server.getSystem().getOurBank().getSortcode())) {
            try {
                this.server.getEventHandler().transfer(new LocalTransferEvent(this.getClient(), account_from, account_to, amount));
                this.getPrintStream().write(BTPResponseCode.ALL_OK);
            } catch (Exception ex) {
                // Send an exception response to the client as their was an error
                this.sendExceptionResponseOverSocket(ex);
            }
        } else {
            try {
                this.server.getEventHandler().transfer(new RemoteTransferEvent(this.getClient(), account_from, account_to, amount));
                this.getPrintStream().write(BTPResponseCode.ALL_OK);
            } catch (Exception ex) {
                // Send an exception response to the client as their was an error
                this.sendExceptionResponseOverSocket(ex);
            }
        }
    }

    public double handleBalanceEnquiry(BTPAccount account) {
        double balance = -1;
        try {
            if (account == null) {
                throw new BTP.exceptions.BTPDataException("No account was specified to query a balance from.");
            }
            balance = server.getEventHandler().getBalance(new BalanceEnquiryEvent(this.getClient(), account));
            this.getPrintStream().write(BTPResponseCode.ALL_OK);
            this.getPrintStream().println(Double.toString(balance));
        } catch (Exception ex) {
            // Send an exception response to the client as their was an error
            this.sendExceptionResponseOverSocket(ex);
        }

        return balance;
    }

    public BTPTransaction[] handleTransactionsEnquiry(BTPAccount account, Date from, Date to) {
        BTPTransaction[] transactions = null;
        try {
            transactions = this.server.getEventHandler().getTransactionsOfAccount(
                    new GetTransactionsOfBankAccountEvent(this.getClient(), account, from, to));
            this.getPrintStream().write(BTPResponseCode.ALL_OK);
            this.getPrintStream().write(transactions.length);
            for (BTPTransaction transaction : transactions) {
                this.writeTransactionToSocket(transaction);
            }
        } catch (Exception ex) {
            this.sendExceptionResponseOverSocket(ex);
        }
        this.getPrintStream().flush();
        return transactions;
    }

    public void sendExceptionResponseOverSocket(Exception exception) {
        int response_code;
        if (exception instanceof BTP.exceptions.BTPAccountNotFoundException) {
            response_code = BTPResponseCode.ACCOUNT_NOT_FOUND_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPBankNotFoundException) {
            response_code = BTPResponseCode.BANK_NOT_FOUND_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPDataException) {
            response_code = BTPResponseCode.DATA_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPInvalidAccountTypeException) {
            response_code = BTPResponseCode.INVALID_ACCOUNT_TYPE_EXCEPTION;
        } else if (exception instanceof BTP.exceptions.BTPPermissionDeniedException) {
            response_code = BTPResponseCode.PERMISSION_DENIED_EXCEPTION;
        } else {
            response_code = BTPResponseCode.UNKNOWN_EXCEPTION;
        }

        this.getPrintStream().write(response_code);
        this.getPrintStream().println(exception.getMessage());
        this.getPrintStream().flush();
    }
}
