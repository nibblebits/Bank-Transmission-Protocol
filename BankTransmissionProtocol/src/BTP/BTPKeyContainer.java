/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class BTPKeyContainer {
    public ArrayList<BTPKey> keys;
    public BTPKeyContainer() {
        this.keys = new ArrayList<BTPKey>();
    }
    
    public void addKey(BTPKey key) {
        this.keys.add(key);
    }
    
    public BTPKey getKey(String index_name) {
        for (BTPKey key : keys) {
            if(key.getIndexName().equals(index_name)) {
                return key;
            }
        }
        return null;
    }
}
