package org.arc.sys.hibernate.entities;
// Generated Feb 11, 2014 5:08:44 PM by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * Reserve generated by hbm2java
 */
public class Reserve  implements java.io.Serializable {


     private Integer reserveId;
     private double onAccount;
     private Date dateReserve;
     private Date dateClose;
     private short stateReserve;
     private Integer userid;
     private boolean del;

    public Reserve() {
    }

	
    public Reserve(long onAccount, Date dateReserve, short stateReserve, boolean del) {
        this.onAccount = onAccount;
        this.dateReserve = dateReserve;
        this.stateReserve = stateReserve;
        this.del = del;
    }
    
    public Reserve(long onAccount, Date dateReserve, Date dateClose, short stateReserve, Integer userid, boolean del) {
       this.onAccount = onAccount;
       this.dateReserve = dateReserve;
       this.dateClose = dateClose;
       this.stateReserve = stateReserve;
       this.userid = userid;
       this.del = del;
    }
   
    public Integer getReserveId() {
        return this.reserveId;
    }
    
    public void setReserveId(Integer reserveId) {
        this.reserveId = reserveId;
    }
    public double getOnAccount() {
        return this.onAccount;
    }
    
    public void setOnAccount(double onAccount) {
        this.onAccount = onAccount;
    }
    public Date getDateReserve() {
        return this.dateReserve;
    }
    
    public void setDateReserve(Date dateReserve) {
        this.dateReserve = dateReserve;
    }
    public Date getDateClose() {
        return this.dateClose;
    }
    
    public void setDateClose(Date dateClose) {
        this.dateClose = dateClose;
    }
    public short getStateReserve() {
        return this.stateReserve;
    }
    
    public void setStateReserve(short stateReserve) {
        this.stateReserve = stateReserve;
    }
    public Integer getUserid() {
        return this.userid;
    }
    
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    public boolean isDel() {
        return this.del;
    }
    
    public void setDel(boolean del) {
        this.del = del;
    }




}

