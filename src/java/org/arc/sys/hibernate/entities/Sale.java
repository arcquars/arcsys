package org.arc.sys.hibernate.entities;
// Generated Jun 17, 2012 4:00:50 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Sale generated by hbm2java
 */
public class Sale  implements java.io.Serializable {


     private Integer salId;
     private Integer vendorId;
     private Integer userId;
     private Date dateSale;
     private double total;
     private boolean del;
     private Reserve reserve;
     private Client client;
     private CreditSale creditSale;
     private Long branchId;

    public Sale() {
    }

    public Sale(Date dateSale, double total, boolean del) {
       this.dateSale = dateSale;
       this.total = total;
       this.del = del;
    }
   
    public Integer getSalId() {
        return this.salId;
    }
    
    public void setSalId(Integer salId) {
        this.salId = salId;
    }
    public Date getDateSale() {
        return this.dateSale;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public void setDateSale(Date dateSale) {
        this.dateSale = dateSale;
    }
    public double getTotal() {
        return this.total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isDel() {
        return this.del;
    }
    
    public void setDel(boolean del) {
        this.del = del;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public CreditSale getCreditSale() {
        return creditSale;
    }

    public void setCreditSale(CreditSale creditSale) {
        this.creditSale = creditSale;
    }
}