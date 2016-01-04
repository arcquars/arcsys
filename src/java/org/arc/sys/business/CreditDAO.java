/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.CreditAmountDto;
import org.arc.sys.dto.CreditDto;
import org.arc.sys.dto.ListCreditDetailDto;
import org.arc.sys.dto.credit.ListPaymentDto;
import org.arc.sys.hibernate.entities.Credit;
import org.hibernate.Session;

/**
 *
 * @author angel
 */
public interface CreditDAO {
    
    public List<CreditDto> getListByProviderId(int providerId);
    
    public boolean savePaymentByCreditIdAndAmount(CreditAmountDto cad);
    
    public boolean validPaymentByCreditId(int creditId, double amount, Session session);
    
    public Credit getCreditById(int creditId);
    
    public double getSumPayments(int creditId);
    
    public ListCreditDetailDto getListByCriteria(int providerId, int buyCancel, String dateStart, String dateEnd);
    
    public ListPaymentDto getListPayment(int creditId);
}
