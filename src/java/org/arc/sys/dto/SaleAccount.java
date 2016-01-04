/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

/**
 *
 * @author angel
 */
public class SaleAccount {

    private String productName;
    private String productCategory;
    private String productFactory;
    private String dateSale;
    private double costeBuy;
    private double priceSale;
    private double amountSale;
    private double gain;
    private double account;
    private double total;

    public double getAmountSale() {
        return amountSale;
    }

    public void setAmountSale(double amountSale) {
        this.amountSale = amountSale;
    }

    public double getCosteBuy() {
        return costeBuy;
    }

    public void setCosteBuy(double costeBuy) {
        this.costeBuy = costeBuy;
    }

    public double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(double priceSale) {
        this.priceSale = priceSale;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductFactory() {
        return productFactory;
    }

    public void setProductFactory(String productFactory) {
        this.productFactory = productFactory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public String getDateSale() {
        return dateSale;
    }

    public void setDateSale(String dateSale) {
        this.dateSale = dateSale;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }
}
