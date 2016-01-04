/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import org.arc.sys.dto.buy.BuyProducts;

/**
 *
 * @author angel
 */
public interface BuyDAO {
    
    public boolean createBuy(BuyProducts buyProducts);
    
    public BuyProducts getListBuyProductByCreditId(int creditId);
}
