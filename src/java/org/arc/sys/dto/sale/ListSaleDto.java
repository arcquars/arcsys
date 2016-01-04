/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.sale;

import java.util.List;

/**
 *
 * @author angel
 */
public class ListSaleDto {

    private List<SaleDto> data;

    public List<SaleDto> getData() {
        return data;
    }

    public void setData(List<SaleDto> data) {
        this.data = data;
    }
}
