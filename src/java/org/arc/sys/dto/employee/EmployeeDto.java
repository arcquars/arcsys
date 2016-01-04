/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.employee;

/**
 *
 * @author angel
 */
public class EmployeeDto {

    private int ci;
    private String names;
    private String firstname;
    private String lastname;
    private String password;
    private String address;
    private String phone_address;
    private String phone_mobil;
    private String email;
    private String position;
    private int branch;

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_address() {
        return phone_address;
    }

    public void setPhone_address(String phone_address) {
        this.phone_address = phone_address;
    }

    public String getPhone_mobil() {
        return phone_mobil;
    }

    public void setPhone_mobil(String phone_mobil) {
        this.phone_mobil = phone_mobil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }
    
    
}
