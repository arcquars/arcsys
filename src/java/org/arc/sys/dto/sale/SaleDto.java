/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.sale;

import java.util.Date;

/**
 *
 * @author angel
 */
public class SaleDto {
    
    private long saleId;
    private String createDate;
    private String razonSocial;
    private long reserve;
    private long credit;
    private boolean creditCanceled;
    private double total;

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public long getReserve() {
        return reserve;
    }

    public void setReserve(long reserve) {
        this.reserve = reserve;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isCreditCanceled() {
        return creditCanceled;
    }

    public void setCreditCanceled(boolean creditCanceled) {
        this.creditCanceled = creditCanceled;
    }
    
    
}
