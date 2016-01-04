package org.arc.sys.hibernate.entities;
// Generated 22-04-2015 08:05:08 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Commission generated by hbm2java
 */
public class Commission  implements java.io.Serializable {


     private Integer commissionId;
     private Product product;
     private Sale sale;
     private long amount;
     private Short state;
     private Date createDate;
     private Employee employed;

    public Commission() {
    }

	
    public Commission(Product product, Sale sale, long amount, Employee employed) {
        this.product = product;
        this.sale = sale;
        this.amount = amount;
        this.employed = employed;
    }
    public Commission(Product product, Sale sale, long amount, Short state, Date createDate, Employee employed) {
       this.product = product;
       this.sale = sale;
       this.amount = amount;
       this.state = state;
       this.createDate = createDate;
       this.employed = employed;
    }
   
    public Integer getCommissionId() {
        return this.commissionId;
    }
    
    public void setCommissionId(Integer commissionId) {
        this.commissionId = commissionId;
    }
    public Product getProduct() {
        return this.product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    public Sale getSale() {
        return this.sale;
    }
    
    public void setSale(Sale sale) {
        this.sale = sale;
    }
    public long getAmount() {
        return this.amount;
    }
    
    public void setAmount(long amount) {
        this.amount = amount;
    }
    public Short getState() {
        return this.state;
    }
    
    public void setState(Short state) {
        this.state = state;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Employee getEmployed() {
        return this.employed;
    }
    
    public void setEmployed(Employee employed) {
        this.employed = employed;
    }
}

