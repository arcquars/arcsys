package org.arc.sys.hibernate.entities;
// Generated 15-07-2015 06:18:24 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * PaymentSale generated by hbm2java
 */
public class PaymentSale implements java.io.Serializable {


     private Long paymentId;
     private CreditSale creditSale;
     private long userid;
     private double amount;
     private Date createDate;
     private boolean del;

    public PaymentSale() {
    }

    public PaymentSale(long userid, double amount, Date createDate, boolean del) {
       this.userid = userid;
       this.amount = amount;
       this.createDate = createDate;
       this.del = del;
    }
   
    public Long getPaymentId() {
        return this.paymentId;
    }
    
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    
    public long getUserid() {
        return this.userid;
    }
    
    public void setUserid(long userid) {
        this.userid = userid;
    }
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public boolean isDel() {
        return this.del;
    }
    
    public void setDel(boolean del) {
        this.del = del;
    }

    public CreditSale getCreditSale() {
        return creditSale;
    }

    public void setCreditSale(CreditSale creditSale) {
        this.creditSale = creditSale;
    }

}

