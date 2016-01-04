/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

/**
 *
 * @author angel
 */
public class ProductRegisterInventaryDto {
    
    private String nameproduct;
    private String codigoProduct;
    private int categoryId;
    private String descriptionProduct;
    private int providerId;
    private int amountBuy;
    private double priceunitBuy;
    private double priceTotalBuy;
    private boolean typeBuy;
    private double payment;

    public int getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(int amountBuy) {
        this.amountBuy = amountBuy;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCodigoProduct() {
        return codigoProduct;
    }

    public void setCodigoProduct(String codigoProduct) {
        this.codigoProduct = codigoProduct;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getPriceTotalBuy() {
        return priceTotalBuy;
    }

    public void setPriceTotalBuy(double priceTotalBuy) {
        this.priceTotalBuy = priceTotalBuy;
    }

    public double getPriceunitBuy() {
        return priceunitBuy;
    }

    public void setPriceunitBuy(double priceunitBuy) {
        this.priceunitBuy = priceunitBuy;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public boolean isTypeBuy() {
        return typeBuy;
    }

    public void setTypeBuy(boolean typeBuy) {
        this.typeBuy = typeBuy;
    }
}
