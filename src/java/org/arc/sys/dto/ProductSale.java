/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

import java.util.Date;

/**
 *
 * @author angel
 */
public class ProductSale{
    
    private int productId;
    private String productName;
    private String detail;
    private String productCodOrigin;
    private String categoryName;
    private String fatoryName;
    private double higherCost;
    private double limitedAmount;
    private double amount;
    private double price;
    private double total;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getFatoryName() {
        return fatoryName;
    }

    public void setFatoryName(String fatoryName) {
        this.fatoryName = fatoryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductCodOrigin() {
        return productCodOrigin;
    }

    public void setProductCodOrigin(String productCodOrigin) {
        this.productCodOrigin = productCodOrigin;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public double getHigherCost() {
        return higherCost;
    }

    public void setHigherCost(double higherCost) {
        this.higherCost = higherCost;
    }

    public double getLimitedAmount() {
        return limitedAmount;
    }

    public void setLimitedAmount(double limitedAmount) {
        this.limitedAmount = limitedAmount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
