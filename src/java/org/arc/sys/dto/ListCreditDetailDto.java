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
public class ListCreditDetailDto {
    private List<CreditDetailDto> aaData;

    public List<CreditDetailDto> getaaData() {
        return aaData;
    }

    public void setaaData(List<CreditDetailDto> aaData) {
        this.aaData = aaData;
    }
}
