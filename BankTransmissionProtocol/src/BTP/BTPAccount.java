
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

/**
 * <h1>Holds a bank account</h1>
 * The BTPAccount class is a container of a bank account BTPAccount's can be
 * transmitted over a socket both partially and entirely
 *
 * @version 1.0
 * @author Daniel
 */
public class BTPAccount {

    private final int customer_id;
    private final int account_no;
    private final String sort_code;
    private final BTPKeyContainer extra;
    private final BTPAccountType account_type;

    public BTPAccount(int customer_id, int account_no, String sort_code, BTPAccountType account_type, BTPKeyContainer extra) {
        this.customer_id = customer_id;
        this.account_no = account_no;
        this.sort_code = sort_code;
        if (extra != null) {
            this.extra = extra;
        } else {
            this.extra = new BTPKeyContainer();
        }

        if (account_type != null) {
            this.account_type = account_type;
        } else {
            this.account_type = new BTPAccountType(-1, "Not shown");
        }
    }

    public BTPAccount(int account_no, String sort_code, BTPAccountType account_type, BTPKeyContainer extra) {
        this.customer_id = -1;
        this.account_no = account_no;
        this.sort_code = sort_code;
        if (extra != null) {
            this.extra = extra;
        } else {
            this.extra = new BTPKeyContainer();
        }

        if (account_type != null) {
            this.account_type = account_type;
        } else {
            this.account_type = new BTPAccountType(-1, "Not shown");
        }
    }

    /* 
     This method returns the customer id of this bank account.
     @return int Returns the customer id of the BTPAccount
     */
    public int getCustomerId() {
        return this.customer_id;
    }

    /* 
     This method returns the account number of this bank account.
     @return int Returns the account number of the BTPAccount
     */
    public int getAccountNumber() {
        return this.account_no;
    }

    /* 
     This method returns the sort code of this bank account.
     @return String Returns the sort code of the BTPAccount
     */
    public String getSortCode() {
        return this.sort_code;
    }

    /* 
     This method returns the extra detail of this bank account.
     @return BTPKeyContainer Returns the extra detail of the BTPAccount
     @see BTPKeyContainer
     */
    public BTPKeyContainer getExtraDetail() {
        return this.extra;
    }

    /* 
     This method returns the account type of this bank account.
     @return BTPAccountType Returns the account type of the BTPAccount
     @see BTPAccountType
     */
    public BTPAccountType getAccountType() {
        return this.account_type;
    }
}
