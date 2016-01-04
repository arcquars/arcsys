/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

import java.util.List;
import org.arc.sys.dto.credit.CreditReportDto;

/**
 *
 * @author angel
 */
public class ListSalesAccount {
    
    public List<SaleAccount> listSales;
    public List<ReserveDto> listReserve;
    public List<ReserveDto> listReserveClose;
    public List<CreditReportDto> listCreditPayment;
    
    public double totalSale;
    public double totalReserve;
    public double totalReserveClose;
    public double totalCreditPayment;

    public List<SaleAccount> getListSales() {
        return listSales;
    }

    public void setListSales(List<SaleAccount> listSales) {
        this.listSales = listSales;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public double getTotalReserve() {
        return totalReserve;
    }

    public void setTotalReserve(double totalReserve) {
        this.totalReserve = totalReserve;
    }

    public double getTotalReserveClose() {
        return totalReserveClose;
    }

    public void setTotalReserveClose(double totalReserveClose) {
        this.totalReserveClose = totalReserveClose;
    }

    public List<ReserveDto> getListReserve() {
        return listReserve;
    }

    public void setListReserve(List<ReserveDto> listReserve) {
        this.listReserve = listReserve;
    }

    public List<ReserveDto> getListReserveClose() {
        return listReserveClose;
    }

    public void setListReserveClose(List<ReserveDto> listReserveClose) {
        this.listReserveClose = listReserveClose;
    }

    public List<CreditReportDto> getListCreditPayment() {
        return listCreditPayment;
    }

    public void setListCreditPayment(List<CreditReportDto> listCreditPayment) {
        this.listCreditPayment = listCreditPayment;
    }

    public double getTotalCreditPayment() {
        return totalCreditPayment;
    }

    public void setTotalCreditPayment(double totalCreditPayment) {
        this.totalCreditPayment = totalCreditPayment;
    }

    
}
