/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import BTP.exceptions.BTPDataException;
import BTP.exceptions.BTPPermissionDeniedException;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public interface BTPCustomerQueryable {

    public void transfer(BTPAccount account_from, BTPAccount account_to, double amount) throws BTPPermissionDeniedException, IOException, BTPDataException, Exception;

    public double getBalance(BTPAccount account) throws BTPPermissionDeniedException, BTPDataException, IOException, Exception;

    public BTPTransaction[] getTransactions(BTPAccount account, Date from, Date to) throws BTPPermissionDeniedException, IOException, Exception;

    public BTPAccount[] getBankAccounts() throws BTPPermissionDeniedException, IOException, BTPDataException, Exception;
}
