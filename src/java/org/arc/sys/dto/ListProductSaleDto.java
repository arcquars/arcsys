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
public class ListProductSaleDto {
    
    private String branchName;
    private String vendorName;
    
    private String dateSale;
    private List<ProductSale> listProductSale;
    private double total;
    private String clientRazonSocial;
    private String clientNit;

    private long credit;
    private long reserve;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public String getClientRazonSocial() {
        return clientRazonSocial;
    }

    public void setClientRazonSocial(String clientRazonSocial) {
        this.clientRazonSocial = clientRazonSocial;
    }

    public String getClientNit() {
        return clientNit;
    }

    public void setClientNit(String clientNit) {
        this.clientNit = clientNit;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public long getReserve() {
        return reserve;
    }

    public void setReserve(long reserve) {
        this.reserve = reserve;
    }
    
    
}
