
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

    public int getCustomerId() {
        return this.customer_id;
    }

    public int getAccountNumber() {
        return this.account_no;
    }

    public String getSortCode() {
        return this.sort_code;
    }

    public BTPKeyContainer getExtraDetail() {
        return this.extra;
    }

    public BTPAccountType getAccountType() {
        return this.account_type;
    }
}
