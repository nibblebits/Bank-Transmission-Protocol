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
public class BTPUser {
    private final int user_id;
    private final String title;
    private final String firstname;
    private final String middlename;
    private final String surname;
    private String new_password;
    private final BTPKeyContainer extra;
    
    public BTPUser(int user_id, String title, String firstname, String middlename, String surname, BTPKeyContainer extra) {
        this.user_id = user_id;
        this.title = title;
        this.firstname = firstname;
        this.middlename = middlename;
        this.surname = surname;
        this.extra = extra;
    }
    
    public int getId() {
        return this.user_id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getFirstname() {
        return this.firstname;
    }
    
    public String getMiddlename() {
        return this.middlename;
    }
    
    public String getSurname() {
        return this.surname;
    }
    
    public String getFullname() {
        return this.title + " " + this.firstname + " " + this.middlename + " " + this.surname;
    }
    
    public BTPKeyContainer getExtraDetail() {
        return this.extra;
    }
    
    public void setNewPassword(String password) {
        this.new_password = password;
    }
    
    public String getPasswordToBeUpdated() {
        return this.new_password;
    }
}
