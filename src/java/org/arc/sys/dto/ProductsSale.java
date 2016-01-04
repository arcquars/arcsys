/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

import org.arc.sys.dto.buy.RecordProductDto;
import java.util.List;

/**
 *
 * @author angel
 */
public class ProductsSale {
    
    private List<RecordProductDto> lisRpd;
    private int providerId;
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
    
    

    
    
}
