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
public class DeliveryBranch {
    
    private List<DeliveryProducts> deliveryProduct;
    private int branchId;
    private int userId;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public List<DeliveryProducts> getDeliveryProduct() {
        return deliveryProduct;
    }

    public void setDeliveryProduct(List<DeliveryProducts> deliveryProduct) {
        this.deliveryProduct = deliveryProduct;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
