/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import org.arc.sys.dto.sale.SaleRevoiceDto;

/**
 *
 * @author angel
 */
public interface SaleDetailDAO {
    
    public SaleRevoiceDto getSaleRevoice(long salId, long creditId);
    
    public SaleRevoiceDto getSaleReport(long salId, long creditId);
    
    public boolean savePaymentSale(long creditId, double payment, int userId);
}
