/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.arc.sys.dto.credit;

import java.util.List;

/**
 *
 * @author angel
 */
public class ListPaymentDto {

    private String providerName;
    private String dateBuy;
    private double totalPayment;
    private double totalBuy;
    
    private List<PaymentDto> list;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDateBuy() {
        return dateBuy;
    }

    public void setDateBuy(String dateBuy) {
        this.dateBuy = dateBuy;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getTotalBuy() {
        return totalBuy;
    }

    public void setTotalBuy(double totalBuy) {
        this.totalBuy = totalBuy;
    }

    public List<PaymentDto> getList() {
        return list;
    }

    public void setList(List<PaymentDto> list) {
        this.list = list;
    }
}
