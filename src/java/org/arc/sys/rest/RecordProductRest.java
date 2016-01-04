/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.arc.sys.business.RecordProductHelper;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.dto.buy.RecordProductList;

/**
 *
 * @author angel
 */
@Path("recordProduct")
public class RecordProductRest {

    @Context
    private UriInfo context;
    private RecordProductHelper recordHelper;

    public RecordProductRest() {
        this.recordHelper = new RecordProductHelper();
    }

    @GET
    @Path("findByCriteria")
    @Produces(MediaType.APPLICATION_JSON)
    public RecordProductList findByCriteria(@QueryParam(value = "type") int type, @QueryParam(value = "criteria") String criteria) {
        return recordHelper.getListProduct(type, criteria);
    }

    @GET
    @Path("findByCriteriaPrint")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RecordProductDto> findByCriteriaPrint(@QueryParam(value = "type") int type, @QueryParam(value = "criteria") String criteria) {
        return recordHelper.getListProductPrint(type, criteria);
    }

    @GET
    @Path("findByCriteriaForExport")
    @Produces("application/vnd.ms-excel")
    public Response findByCriteriaForExcel(@QueryParam(value = "type") int type, @QueryParam(value = "criteria") String criteria) {
        List<RecordProductDto> list = recordHelper.getListProduct(type, criteria).getList();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sample sheet");

        Map<String, Object[]> data = new HashMap<String, Object[]>();
        data.put("1", new Object[]{"#", "Producto", "Codigo", "Cantidad"});

        int rownum = 0;
        Row row = sheet.createRow(rownum++);

        int cellnum = 0;
        Cell cell = row.createCell(0);
        cell.setCellValue("#");
        cell = row.createCell(1);
        cell.setCellValue("Producto");
        cell = row.createCell(2);
        cell.setCellValue("Codigo");
        cell = row.createCell(3);
        cell.setCellValue("Cantidad");

        Iterator<RecordProductDto> iterable = list.iterator();

        while (iterable.hasNext()) {
            RecordProductDto product = iterable.next();

            row = sheet.createRow(rownum++);

            cell = row.createCell(0);
            cell.setCellValue((rownum-1));
            cell = row.createCell(1);
            cell.setCellValue(product.getProductName());
            cell = row.createCell(2);
            cell.setCellValue(product.getCodOrigin());
            cell = row.createCell(3);
            cell.setCellValue(product.getTotal());
        }

        /*
         Set<String> keyset = data.keySet();
         int rownum = 0;
         for(String key: keyset){
         Row row = sheet.createRow(rownum++);
         Object[] objArr = data.get(key);
         int cellnum = 0;
         for(Object obj: objArr){
         Cell cell = row.createCell(cellnum++);
         if(obj instanceof Date)
         cell.setCellValue((Date)obj);
         else if(obj instanceof Boolean)
         cell.setCellValue((Boolean)obj);
         else if(obj instanceof String)
         cell.setCellValue((String)obj);
         else if(obj instanceof Double)
         cell.setCellValue((Double)obj);
         }
         }
         */
        try {
            FileOutputStream out = new FileOutputStream(new File("/home/angel/mio.xls"));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

            File file = new File("/home/angel/mio.xls");
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=\"test_excel_file.xls\"");

            return response.build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
