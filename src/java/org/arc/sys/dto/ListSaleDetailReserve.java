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
public class ListSaleDetailReserve {
    
    public List<SaleDetailGain> list;
    public double onAccount;
    public double debit;
    public double totalSale;

    public List<SaleDetailGain> getList() {
        return list;
    }

    public void setList(List<SaleDetailGain> list) {
        this.list = list;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public double getOnAccount() {
        return onAccount;
    }

    public void setOnAccount(double onAccount) {
        this.onAccount = onAccount;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }
    
}
