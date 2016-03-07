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
public class PageNavigator {

    private BankClient client;
    public PageNavigator(BankClient client) {
        this.client = client;
    }

    public void showPage(Page page) {
        page.run();
    }
}
