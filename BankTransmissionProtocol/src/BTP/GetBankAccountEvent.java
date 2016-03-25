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
public class GetBankAccountEvent extends BTPEvent {
    private int account_no;
    public GetBankAccountEvent(BTPClient client, int account_no) {
        super(client);
        this.account_no = account_no;
    }
    
    public int getAccountNo() {
        return this.account_no;
    }
}
