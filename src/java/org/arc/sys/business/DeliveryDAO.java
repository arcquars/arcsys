/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import org.arc.sys.dto.DeliveryBranch;

/**
 *
 * @author angel
 */
public interface DeliveryDAO {
    
    public boolean saveDelivery(DeliveryBranch db);
}
