/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

/**
 *
 * @author Daniel
 */
public interface BTPServerEventHandler {
    public void customerLogin(CustomerLoginEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPAccountNotFoundException, Exception;
    public void employeeLogin(EmployeeLoginEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPAccountNotFoundException, Exception;
    public int createBankAccount(CreateNewBankAccountEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPInvalidAccountTypeException, Exception;
    public int createCustomer(CreateCustomerEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, Exception;
    public BTPAccountType[] getBankAccountTypes(GetBankAccountTypesEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, Exception;
    public BTPAccount[] getBankAccountsOfCustomer(GetBankAccountsOfCustomerEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, Exception;
    public BTPAccount getBankAccount(GetBankAccountEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPAccountNotFoundException;
    public void transfer(RemoteTransferEvent event) throws BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPUnknownException, BTP.exceptions.BTPBankNotFoundException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPInvalidAccountTypeException, BTP.exceptions.BTPPermissionDeniedException, Exception;
    public void transfer(LocalTransferEvent event) throws BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPUnknownException, BTP.exceptions.BTPBankNotFoundException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPInvalidAccountTypeException, BTP.exceptions.BTPPermissionDeniedException, Exception;
    public BTPCustomer getCustomer(GetCustomerEvent event) throws BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPUnknownException;
    public double getBalance(BalanceEnquiryEvent event) throws BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPPermissionDeniedException, Exception;
    public void setSavingsAccountInterestRate(SetSavingsAccountInterestRateEvent event);
    public BTPTransaction[] getTransactionsOfAccount(GetTransactionsOfBankAccountEvent event) throws BTP.exceptions.BTPPermissionDeniedException, BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPInvalidAccountTypeException, BTP.exceptions.BTPUnknownException;
}
