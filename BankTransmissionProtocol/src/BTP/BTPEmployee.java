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
class BTPEmployee {
    private int employee_id;
    private String firstname;
    private String middle_name;
    private String surname;
    private String updated_password;
    
    public BTPEmployee() {
        
    }
    
    public void setId(int employee_id) {
        this.employee_id = employee_id;
    }
    
    public void setFirstname(String name) {
        this.firstname = name;
    }
    
    public void setMiddlename(String name) {
        this.middle_name = name;
    }
    
    public void setSurname(String name) {
        this.surname = name;
    }
    
    public void setPassword(String password) {
        this.updated_password = password;
    }
    
    public int getId() {
        return this.employee_id;
    }
    
    public String getFirstname() {
        return this.firstname;
    }
    
    public String getMiddlename() {
        return this.middle_name;
    }
    
    public String getSurname() {
        return this.surname;
    }
}
