/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

/**
 *
 * @author angel
 */
public class RecordProductBranchDto {
    
    private int branchId;
    private int productId;
    private String productName;
    private String productCodOrigin;
    private String category;
    private String factoryName;
    private double total;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
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

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    
    
}
