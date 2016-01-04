/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import org.arc.sys.dto.ListProductRefund;

/**
 *
 * @author angel
 */
public interface RefundDAO {
    
    public ListProductRefund getListProductRefund(String ids, int branchId);
    
    public boolean saveListProductRefund(ListProductRefund lps);
}
