/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class GetTransactionsOfBankAccountEvent extends BTPEvent {
    private final BTPAccount account;
    private final Date date_from;
    private final Date date_to;
    public GetTransactionsOfBankAccountEvent(BTPAccount account, Date date_from, Date date_to) {
        this.account = account;
        this.date_from = date_from;
        this.date_to = date_to;
    }
    
    public BTPAccount getAccount() {
        return this.account;
    }
    
    public Date getDateFrom() {
        return this.date_from;
    }
    
    public Date getDateTo() {
        return this.date_to;
    }
}
