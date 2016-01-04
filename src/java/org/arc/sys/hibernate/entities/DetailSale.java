package org.arc.sys.hibernate.entities;
// Generated Jun 17, 2012 4:00:50 PM by Hibernate Tools 3.2.1.GA



/**
 * DetailSale generated by hbm2java
 */
public class DetailSale  implements java.io.Serializable {


     private Integer detailSaleId;
     private Sale sale;
     private Product product;
     private Branch branch;
     private double amount;
     private double coste;
     private double price;
     private double gain;
     private double total;
     private boolean del;

    public DetailSale() {
    }

	
    public DetailSale(int amount, float price) {
        this.amount = amount;
        this.price = price;
    }
    public DetailSale(Sale sal, Product product, Branch branch, int amount, double price, double total, boolean del) {
       this.sale = sale;
       this.product = product;
       this.branch = branch;
       this.amount = amount;
       this.price = price;
       this.total = total;
       this.del = del;
    }
   
    public Integer getDetailSaleId() {
        return this.detailSaleId;
    }
    
    public void setDetailSaleId(Integer detailSaleId) {
        this.detailSaleId = detailSaleId;
    }
    public Sale getSale() {
        return this.sale;
    }
    
    public void setSale(Sale sale) {
        this.sale = sale;
    }
    public Product getProduct() {
        return this.product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    public Branch getBranch() {
        return this.branch;
    }
    
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public double getPrice() {
        return this.price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    public double getTotal() {
        return this.total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    public boolean getDel() {
        return this.del;
    }
    
    public void setDel(boolean del) {
        this.del = del;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }
    
    
}

