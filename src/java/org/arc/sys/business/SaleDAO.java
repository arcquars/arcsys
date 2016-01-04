/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.HashMap;
import org.arc.sys.dto.ListProductSale;
import org.arc.sys.dto.ListProductSaleDto;
import org.arc.sys.dto.ListSaleDetailGain;
import org.arc.sys.dto.ListSalesAccount;
import org.arc.sys.dto.ProductReserve;
import org.arc.sys.dto.ReserveDetail;
import org.arc.sys.dto.ReserveDto;
import org.arc.sys.dto.SalesAmountDto;
import org.arc.sys.dto.sale.ListSaleDto;

/**
 *
 * @author angel
 */
public interface SaleDAO {
    
    public ListProductSale getListProductSale(String ids, int branchId);
    
    public HashMap saveListProductSale(ListProductSale lps);
    
    public ListSaleDetailGain getListSDG(int branchId, String dateStart, String dateEnd);
    
    public ListSalesAccount getListAccount(int branchId, String dateStart, String dateEnd);
    
    public HashMap<String, String> saveListProductSaleForInventoryGral(ListProductSale lps);
    
    public boolean saveListProductReserveForInventoryGral(ProductReserve pr);
    
    public ProductReserve getListProductReserve(String ids, int branchId);
    
    public ArrayList<ReserveDto> getListReserve(int branchId, String dateReserve, boolean all);
    
    public ListSaleDetailGain getListSDGReserve(int branchId, String dateStart, String dateEnd);
    
    public boolean saveListProductReserve(ProductReserve pr);
    
    public ReserveDetail getListReserveDetail(int idReserve);
    
    public boolean reserveClose(int reserveId);
    
    public SalesAmountDto getLastSaleAmount(int branchId, int userId);
    
    public String[] getLastSaleAmountDates(int branchId);
    
    public boolean reserveSalesAccount(SalesAmountDto saDto);
    
    public ListSaleDto listSale(String dateStart, String dateEnd, int branchId);
    
    public ListProductSaleDto getDetailSale(long salId);
    
    public ListSaleDto listSaleIsCredit(int branchId, int type, String search);
    
    public ListSaleDto listSaleReport(int branchId, int type, String search);
    
    public String getLastSaleAmountDateByBranchId(long branchId);
    
    public boolean saveSaleAmount(long userId, long branchId, String dateStart, String dateEnd, double amount);
}
