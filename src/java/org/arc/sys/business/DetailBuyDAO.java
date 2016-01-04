/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;

/**
 *
 * @author angel
 */
public interface DetailBuyDAO {
    
    public List<Double> getDatesBuyByProductId(int productId);
    
}
