/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

import java.util.List;

/**
 *
 * @author angel
 */
public class ProductReserve {
    
    private int branchId;
    private int userId;
    private int clientId;
    private String branchName;
    private String dateSale;
    private String dateClose;
    private double onAccount;
    private List<ProductSale> listProductSale;
    private double total;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getDateSale() {
        return dateSale;
    }

    public void setDateSale(String dateSale) {
        this.dateSale = dateSale;
    }

    public List<ProductSale> getListProductSale() {
        return listProductSale;
    }

    public void setListProductSale(List<ProductSale> listProductSale) {
        this.listProductSale = listProductSale;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getOnAccount() {
        return onAccount;
    }

    public void setOnAccount(double onAccount) {
        this.onAccount = onAccount;
    }

    public String getDateClose() {
        return dateClose;
    }

    public void setDateClose(String dateClose) {
        this.dateClose = dateClose;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    
}
