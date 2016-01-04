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
public class ListSaleDetailGain {
    
    public List<SaleDetailGain> list;
    public double totalGain;
    public double totalSale;

    public List<SaleDetailGain> getList() {
        return list;
    }

    public void setList(List<SaleDetailGain> list) {
        this.list = list;
    }

    public double getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(double totalGain) {
        this.totalGain = totalGain;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }
    
    
}
