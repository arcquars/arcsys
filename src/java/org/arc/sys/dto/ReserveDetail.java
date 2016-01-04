/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.arc.sys.dto;

import java.util.List;

/**
 *
 * @author angel
 */
public class ReserveDetail {

    public String nameClient;
    private String nameCi;
    private String dateReserve;
    private String branchName;
    public double onAmount;
    public double debit;
    public double total;
    
    public List<SaleDetail> listSaleDetail;

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public double getOnAmount() {
        return onAmount;
    }

    public void setOnAmount(double onAmount) {
        this.onAmount = onAmount;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<SaleDetail> getListSaleDetail() {
        return listSaleDetail;
    }

    public void setListSaleDetail(List<SaleDetail> listSaleDetail) {
        this.listSaleDetail = listSaleDetail;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getNameCi() {
        return nameCi;
    }

    public void setNameCi(String nameCi) {
        this.nameCi = nameCi;
    }

    public String getDateReserve() {
        return dateReserve;
    }

    public void setDateReserve(String dateReserve) {
        this.dateReserve = dateReserve;
    }
}
