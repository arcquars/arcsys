/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

import java.io.Serializable;

/**
 *
 * @author angel
 */
public class ProviderPersonDto implements Serializable{
    private String companyName;
    private int perId;
    private String agentNames;
    private String firstname;
    private String lastname;
    private Integer ci;
    private String address;
    private Integer phoneHome;
    private Integer phoneMobil;
    private String email;
    private boolean del;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAgentNames() {
        return agentNames;
    }

    public void setAgentNames(String agentNames) {
        this.agentNames = agentNames;
    }

    public Integer getCi() {
        return ci;
    }

    public void setCi(Integer ci) {
        this.ci = ci;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneHome(Integer phoneHome) {
        this.phoneHome = phoneHome;
    }

    public Integer getPhoneMobil() {
        return phoneMobil;
    }

    public void setPhoneMobil(Integer phoneMobil) {
        this.phoneMobil = phoneMobil;
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }
}
