package org.arc.sys.hibernate.entities;
// Generated Jun 12, 2012 4:50:53 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * RecordProductBranch generated by hbm2java
 */
public class RecordProductBranch  implements java.io.Serializable {


     private Integer rpbId;
     private Stock stock;
     private String active;
     private Integer amount;
     private Integer deliveryId;
     private Integer detailSaleId;
     private Integer refundId;
     private Date fecha;
     private double total;
     private Boolean del;

    public RecordProductBranch() {
    }

    public RecordProductBranch(Stock stock, String active, Integer amount, Integer deliveryId, Integer detailSaleId, Date fecha, double total, Boolean del) {
       this.stock = stock;
       this.active = active;
       this.amount = amount;
       this.deliveryId = deliveryId;
       this.detailSaleId = detailSaleId;
       this.fecha = fecha;
       this.total = total;
       this.del = del;
    }
   
    public Integer getRpbId() {
        return this.rpbId;
    }
    
    public void setRpbId(Integer rpbId) {
        this.rpbId = rpbId;
    }
    public String getActive() {
        return this.active;
    }
    
    public void setActive(String active) {
        this.active = active;
    }
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Integer getDeliveryId() {
        return this.deliveryId;
    }
    
    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }
    public Integer getDetailSaleId() {
        return this.detailSaleId;
    }
    
    public void setDetailSaleId(Integer detailSaleId) {
        this.detailSaleId = detailSaleId;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public double getTotal() {
        return this.total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    public Boolean getDel() {
        return this.del;
    }
    
    public void setDel(Boolean del) {
        this.del = del;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }
}


