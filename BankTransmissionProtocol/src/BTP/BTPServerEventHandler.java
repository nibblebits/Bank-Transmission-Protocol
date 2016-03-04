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
    public void createBankAccount(CreateNewBankAccountEvent event);
    public void createCustomer(CreateCustomerEvent event); 
    public void setDailyOverdrawnCharge(SetDailyOverdrawnChargeEvent event);
    public BTPAccountType[] getBankAccountTypes(GetBankAccountTypesEvent event);
    public BTPAccount[] getBankAccountsOfCustomer(GetBankAccountsOfCustomerEvent event);
    public void setBankAccountsOverdraftLimit(SetBankAccountOverdraftLimitEvent event);
    public void transfer(RemoteTransferEvent event) throws BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPBankNotFoundException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPInvalidAccountTypeException, BTP.exceptions.BTPPermissionDeniedException;
    public void transfer(LocalTransferEvent event) throws BTP.exceptions.BTPAccountNotFoundException, BTP.exceptions.BTPBankNotFoundException, BTP.exceptions.BTPDataException, BTP.exceptions.BTPInvalidAccountTypeException, BTP.exceptions.BTPPermissionDeniedException;
    public BTPCustomer getCustomer(GetCustomerEvent event);
    public double getBalance(BalanceEnquiryEvent event);
    public void setSavingsAccountInterestRate(SetSavingsAccountInterestRateEvent event);
    public BTPTransaction[] getTransactionsOfAccount(GetTransactionsOfBankAccountEvent event);
}
