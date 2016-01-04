/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.arc.sys.business.SaleDetailHelper;
import org.arc.sys.business.SaleHelper;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.ListProductSale;
import org.arc.sys.dto.ListSaleDetailGain;
import org.arc.sys.dto.ListSalesAccount;
import org.arc.sys.dto.ProductReserve;
import org.arc.sys.dto.ReserveDetail;
import org.arc.sys.dto.ReserveDto;
import org.arc.sys.dto.SalesAmountDto;
import org.arc.sys.dto.sale.SaleRevoiceDto;

/**
 *
 * @author angel
 */
@Path("sale")
public class SaleRest {
    
    @Context
    private UriInfo context;
    private SaleHelper saleHelper;
    private SaleDetailHelper saleDetailHelper;

    public SaleRest() {
        this.saleHelper = new SaleHelper();
        this.saleDetailHelper = new SaleDetailHelper();
    }
    
    @GET
    @Path("getListByIds")
    @Produces(MediaType.APPLICATION_JSON)
    public ListProductSale getListProductSale(@QueryParam(value = "ids") String ids, @QueryParam(value = "branchId") int branchId){
        System.out.println("entro a ids list sale!!");
        return saleHelper.getListProductSale(ids, branchId);
    }
    
    @GET
    @Path("getListForReserveByIds")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductReserve getListProductReserve(@QueryParam(value = "ids") String ids, @QueryParam(value = "branchId") int branchId){
        return saleHelper.getListProductReserve(ids, branchId);
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createBranch(ListProductSale lps) {
        Gson gson = new Gson();
//        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwww::: "+lps.getTotal());
//        return "true";
        if(lps.getBranchId()==0){
            return ""+gson.toJson(saleHelper.saveListProductSaleForInventoryGral(lps));
        }
        return ""+gson.toJson(saleHelper.saveListProductSale(lps));
    }
    
    @GET
    @Path("getListByDates")
    @Produces(MediaType.APPLICATION_JSON)
    public ListSaleDetailGain getListSaleDetailGain(@QueryParam(value = "branchId") int branchId, @QueryParam(value = "dateStart") String dateStart,@QueryParam(value = "dateEnd") String dateEnd){
        return saleHelper.getListSDG(branchId, dateStart, dateEnd);
    }
    
    @GET
    @Path("getListAccountByDates")
    @Produces(MediaType.APPLICATION_JSON)
    public ListSalesAccount getListSaleAccount(@QueryParam(value = "branchId") int branchId, @QueryParam(value = "dateStart") String dateStart,@QueryParam(value = "dateEnd") String dateEnd){
        return saleHelper.getListAccount(branchId, dateStart, dateEnd);
    }
    
    @POST
    @Path("saveReserve")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createReserve(ProductReserve pr) {
        if(pr.getBranchId()==0){
            return ""+saleHelper.saveListProductReserveForInventoryGral(pr);
        }
        return ""+saleHelper.saveListProductReserve(pr);
    }
    
    @GET
    @Path("getExcel")
    @Produces("application/vnd.ms-excel")
    public Response getExcel(){
        Workbook wb = new XSSFWorkbook();
        
        Sheet sheet = wb.createSheet("sheet 1");
        
        Row row = sheet.createRow((short) 0);
        
        row.createCell(0).setCellValue(1);
        row.createCell(1).setCellValue("Text");
        
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
        cellStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(CellStyle.BIG_SPOTS);
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
        
        Cell cell = row.createCell(2);
        cell.setCellValue(new Date());
        cell.setCellStyle(cellStyle);
        
        row.createCell(3).setCellValue(true);
        
        FileOutputStream fileOut;
        try{
            fileOut = new FileOutputStream("/home/angel/mio.xls");
            wb.write(fileOut);
            fileOut.close();
            File file = new File("/home/angel/mio.xls");
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=\"test_excel_file.xls\"");
        
            return response.build();
            
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return null;
        
    }
    
    @GET
    @Path("getListReserve")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReserveDto> getListReserve(@QueryParam(value = "branchId") int branchId, @QueryParam(value = "dateReserve") String dateReserve, @QueryParam(value = "all") boolean all) {
        List<ReserveDto> result = new ArrayList<ReserveDto>();
        try {
            result = saleHelper.getListReserve(branchId, dateReserve, all);
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result;
    }
    
    @GET
    @Path("getReserveDetail")
    @Produces(MediaType.APPLICATION_JSON)
    public ReserveDetail getReserveDetail(@QueryParam(value = "reserveId") int reserveId) {
        ReserveDetail result = new ReserveDetail();
        try {
            result = saleHelper.getListReserveDetail(reserveId);
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result;
    }
    
    @GET
    @Path("reserveUpdate")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean reserveUpdate(@QueryParam(value = "reserveId") int reserveId) {
        boolean result;
        try {
            result = saleHelper.reserveClose(reserveId);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result;
    }
    
    @GET
    @Path("getLastSalesAmount")
    @Produces(MediaType.APPLICATION_JSON)
    public SalesAmountDto getLastSalesAmount(@QueryParam(value = "branchId") int branchId, @QueryParam(value = "userId") int userId) {
        SalesAmountDto sa = null;
        try {
            sa = saleHelper.getLastSaleAmount(branchId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return sa;
    }
    
    
    @GET
    @Path("getLastSalesAmountDates")
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getLastSalesAmountDates(@QueryParam(value = "branchId") int branchId) {
        String [] sa = null;
        try {
            sa = saleHelper.getLastSaleAmountDates(branchId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sa;
    }
    
    @POST
    @Path("saveSalesAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createSalesAccount(SalesAmountDto saDto) {
        return ""+saleHelper.reserveSalesAccount(saDto);
    }
    
    @GET
    @Path("listSale")
    @Produces(MediaType.APPLICATION_JSON)
    public String listSale(@QueryParam(value = "dateStart") String dateStart, @QueryParam(value = "dateEnd") String dateEnd, @QueryParam(value = "branchId") int branchId) {
        String result = "";
        try {
            Gson g = new Gson();
            result = g.toJson(saleHelper.listSale(dateStart, dateEnd, branchId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("listSaleDto")
    @Produces(MediaType.APPLICATION_JSON)
    public String listSaleDto(@QueryParam(value = "salId") long salId) {
        String result = "";
        try {
            Gson g = new Gson();
            result = g.toJson(saleHelper.getDetailSale(salId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("listSaleIsCredit")
    @Produces(MediaType.APPLICATION_JSON)
    public String listSaleIsCredit(@QueryParam(value = "branchId") int branchId, @QueryParam(value = "type") int type, @QueryParam(value = "search") String search) {
        String result = "";
        try {
            Gson g = new Gson();
            result = g.toJson(saleHelper.listSaleIsCredit(branchId, type, search));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("listSaleReport")
    @Produces(MediaType.APPLICATION_JSON)
    public String listSaleReport(@QueryParam(value = "branchId") int branchId, @QueryParam(value = "type") int type, @QueryParam(value = "search") String search) {
        String result = "";
        try {
            Gson g = new Gson();
            result = g.toJson(saleHelper.listSaleReport(branchId, type, search));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("salePayments")
    @Produces(MediaType.APPLICATION_JSON)
    public String salePayments(@QueryParam(value = "salId") int salId, @QueryParam(value = "creditId") int creditId) {
        String result = "";
        try {
            Gson g = new Gson();
            result = g.toJson(saleDetailHelper.getSaleRevoice(salId, creditId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("salePaymentsReport")
    @Produces(MediaType.APPLICATION_JSON)
    public String salePaymentsReport(@QueryParam(value = "salId") int salId, @QueryParam(value = "creditId") int creditId) {
        String result = "";
        try {
            Gson g = new Gson();
            result = g.toJson(saleDetailHelper.getSaleReport(salId, creditId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("savePayment")
    @Produces(MediaType.APPLICATION_JSON)
    public String savePayment(@QueryParam(value = "creditId") long creditId, @QueryParam(value = "payment") double payment, @QueryParam(value = "userId") int userId) {
        String result = "";
        try {
            Gson g = new Gson();
            result = saleDetailHelper.savePaymentSale(creditId, payment, userId)+"";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("lastSaleAmountDateByBranchId")
    @Produces(MediaType.APPLICATION_JSON)
    public String lastSaleAmountDateByBranchId(
            @QueryParam(value = "branchId") long branchId
            ) {
        String result = "";
        try {
            Gson g = new Gson();
            result = saleHelper.getLastSaleAmountDateByBranchId(branchId)+"";
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<KeyValue> list = new ArrayList<KeyValue>();
            KeyValue kv1 = new KeyValue("dateStart", result);
            KeyValue kv2 = new KeyValue("dateEnd", dateFormat.format(new Date()));
            list.add(kv1);
            list.add(kv2);
            result = g.toJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("saveSaleAmount")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveSaleAmount(
            @QueryParam(value = "userId") long userId, 
            @QueryParam(value = "branchId") long branchId, 
            @QueryParam(value = "dateStart") String dateStart,
            @QueryParam(value = "dateEnd") String dateEnd,
            @QueryParam(value = "amount") double amount
            ) {
        boolean result = false;
        try {
            Gson g = new Gson();
            result = saleHelper.saveSaleAmount(userId, branchId, dateStart, dateEnd, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result+"";
    }
}
