package org.arc.sys.hibernate.entities;
// Generated Jun 12, 2012 4:50:53 PM by Hibernate Tools 3.2.1.GA



/**
 * User generated by hbm2java
 */
public class User  implements java.io.Serializable {


     private Integer userId;
     private Person person;
     private String login;
     private String password;
     private boolean del;

    public User() {
    }

	
    public User(String login, String password, boolean del) {
        this.login = login;
        this.password = password;
        this.del = del;
    }
    public User(Person person, String login, String password, boolean del) {
       this.person = person;
       this.login = login;
       this.password = password;
       this.del = del;
    }
   
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isDel() {
        return this.del;
    }
    
    public void setDel(boolean del) {
        this.del = del;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }




}


