/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

/**
 *
 * @author Daniel
 */
public abstract class Page {
    private Page parent;
    private BankClient client;
    public Page(BankClient client) {
        this.parent = null;
        this.client = client;
    }
    
    public void setParent(Page page) {
        this.parent = page;
    }
    
    public Page getParentPage() {
        return this.parent;
    }
    
    public BankClient getBankClient() {
        return this.client;
    }
    
    public abstract void run();
}
