/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

/**
 *
 * @author angel
 */
public class ProductRefund {
    
    private int productId;
    private String productName;
    private String productCodOrigin;
    private String categoryName;
    private String fatoryName;
    private double limitedAmount;
    private double amount;

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

    public double getLimitedAmount() {
        return limitedAmount;
    }

    public void setLimitedAmount(double limitedAmount) {
        this.limitedAmount = limitedAmount;
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
}
