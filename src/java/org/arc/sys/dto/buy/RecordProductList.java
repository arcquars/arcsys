/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.buy;

import java.util.List;

/**
 *
 * @author angel
 */
public class RecordProductList {

    private List<RecordProductDto> list;
    private double granTotal;

    public List<RecordProductDto> getList() {
        return list;
    }

    public void setList(List<RecordProductDto> list) {
        this.list = list;
    }

    public double getGranTotal() {
        return granTotal;
    }

    public void setGranTotal(double granTotal) {
        this.granTotal = granTotal;
    }
    
    
}
