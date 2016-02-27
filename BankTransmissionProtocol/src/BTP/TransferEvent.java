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
public abstract class TransferEvent {
    private final BTPAccount account_from;
    private final BTPAccount account_to;
    private final double amount;
    
    public TransferEvent(BTPAccount account_from, BTPAccount account_to, double amount) {
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }
    
    public BTPAccount getAccountToTransferFrom() {
        return this.account_from;
    }
    
    public BTPAccount getAccountToTransferTo() {
        return this.account_to;
    }
    
    public double getAmountToTransfer() {
        return this.amount;
    }
}
