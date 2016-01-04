/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.buy;

import java.util.List;

/**
 *
 * @author angel
 */
public class BuyProducts {
    
    private List<RecordProductDto> lisRpd;
    private int providerId;
    private String providerName;
    private int userId;
    private String dateBuy;
    private double totalBuy;
    private String credit;
    private double payment;

    public List<RecordProductDto> getLisRpd() {
        return lisRpd;
    }

    public void setLisRpd(List<RecordProductDto> lisRpd) {
        this.lisRpd = lisRpd;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public double getTotalBuy() {
        return totalBuy;
    }

    public void setTotalBuy(double totalBuy) {
        this.totalBuy = totalBuy;
    }


    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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
    
}
