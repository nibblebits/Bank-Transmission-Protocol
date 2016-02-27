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
public class BTPKey {
    private final String index_name;
    private final String value;
    public BTPKey(String index_name, String value) {
        this.index_name = index_name;
        this.value = value;
    }
    
    public String getIndexName() {
        return this.index_name;
    }
    
    public String getValue() {
        return this.value;
    }
}
