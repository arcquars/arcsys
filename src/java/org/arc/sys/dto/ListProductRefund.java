/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angel
 */
public class ListProductRefund {
    private int branchId;
    private String dateRefund;
    private int userId;
    private List<ProductRefund> list;
    private int total;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getDateRefund() {
        return dateRefund;
    }

    public void setDateRefund(String dateRefund) {
        this.dateRefund = dateRefund;
    }

    public List<ProductRefund> getList() {
        return list;
    }

    public void setList(List<ProductRefund> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    
    
    public static ListProductRefund getStaticList(){
        ListProductRefund lpr = new ListProductRefund();
        lpr.setBranchId(2);
        lpr.setDateRefund("2012-06-18");
        
        List<ProductRefund> list = new ArrayList<ProductRefund>();
        ProductRefund pr = new ProductRefund();
        pr.setAmount(0);
        pr.setCategoryName("Televisores");
        pr.setFatoryName("Sony");
        pr.setLimitedAmount(4);
        pr.setProductCodOrigin("ssdff33-33");
        pr.setProductId(1);
        pr.setProductName("tv 21 ''");
        
        list.add(pr);
        
        pr = new ProductRefund();
        
        pr.setAmount(0);
        pr.setCategoryName("Mini componente");
        pr.setFatoryName("Sony");
        pr.setLimitedAmount(6);
        pr.setProductCodOrigin("mini-33");
        pr.setProductId(1);
        pr.setProductName("mini mini");
        
        list.add(pr);
        
        lpr.setList(list);
        
        return lpr;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
