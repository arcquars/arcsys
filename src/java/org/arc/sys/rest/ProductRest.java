/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.ClientHelper;
import org.arc.sys.business.ProductHelper;
import org.arc.sys.dto.DeliveryBranch;
import org.arc.sys.dto.ProductDetailDto;
import org.arc.sys.dto.ProductDto;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.hibernate.entities.Client;
import org.arc.sys.hibernate.entities.Product;

/**
 * REST Web Service
 *
 * @author angel
 */
@Path("product")
public class ProductRest {

    @Context
    private UriInfo context;
    private ProductHelper productHelper;

    @Context
    private ServletContext context1;

    /**
     * Creates a new instance of UserRest
     */
    public ProductRest() {
        productHelper = new ProductHelper();
    }

    @POST
    @Consumes({"application/json", "application/x-www-form-urlencoded"})
    public String postProduct(ProductDto productDto) {
        return "" + productHelper.productCreate(productDto);
    }

    @GET
    @Path("productbyId")
    @Produces(MediaType.APPLICATION_JSON)
    public Product productById(@QueryParam(value = "proId") int proId) {
        Product result = null;
        try {
            result = productHelper.getProductById(proId);
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Path("findByCriteria")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDetailDto> findByCriteria(@QueryParam(value = "type") int type, @QueryParam(value = "criteria") String criteria) {
        List<ProductDetailDto> lproduct = productHelper.getListPersonByName(type, criteria);
        return lproduct;
    }

    @GET
    @Path("getAllCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> findAllCategory() {
        return productHelper.getCategoryAll();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void removeProduct(@QueryParam("id") int id) {
        System.out.println("Entor a delete product!!!");
        productHelper.deleteProduct(id);
    }

    @POST
    @Path("updateProduct")
    @Consumes({"application/json", "application/x-www-form-urlencoded"})
    public void updateProduct(Product product) {
        productHelper.updateProduct(product);
    }

    @GET
    @Path("validName")
    @Produces(MediaType.APPLICATION_JSON)
    public String validName(@QueryParam(value = "newName") String newName) {
        return "" + productHelper.validName(newName);
    }

    @GET
    @Path("validNameEdit")
    @Produces(MediaType.APPLICATION_JSON)
    public String validNameEdit(@QueryParam(value = "newName") String newName, @QueryParam(value = "prodId") int prodId) {
        return "" + productHelper.validNameEdit(newName, prodId);
    }

    @GET
    @Path("getListByIds")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RecordProductDto> getListByIds(@QueryParam(value = "ids") String ids) {
        return productHelper.getListProductBuy(ids);
    }

    @GET
    @Path("getDeliveryBranch")
    @Produces(MediaType.APPLICATION_JSON)
    public DeliveryBranch getDeliveryBranch(@QueryParam(value = "ids") String ids) {
        return productHelper.getListProductDelivery(ids);
    }

    @GET
    @Path("getProductDtoById")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductDto getProductDtoById(@QueryParam(value = "id") int id) {
        return productHelper.getProductDtoById(id);
    }

    @POST
    @Path("updateProductPrice")
    @Consumes({"application/json", "application/x-www-form-urlencoded"})
    public void updateProduct(ProductDto product) {
        productHelper.updateProductDto(product);
    }

    @GET
    @Path("getDatesPriceSaleByProductid")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM})
    @Produces(MediaType.APPLICATION_JSON)
    public String getDatesBuyByProductId(@QueryParam(value = "proId") int productId) {
        return productHelper.getDatesPriceByProductId(productId).toString();
    }

    @GET
    @Path("loadfile")
    @Produces(MediaType.APPLICATION_JSON)
    public String validName() {
        //return ""+productHelper.validName(newName);
        String splitBy = ";";
        String line = "";
        ProductDto pDto = null;
        try {
            //BufferedReader br = new BufferedReader(new FileReader("/home/angel/arcsys/sillas1.csv"));
            //BufferedReader br = new BufferedReader(new FileReader("/home/angel/arcsys/oficina.csv"));
            //BufferedReader br = new BufferedReader(new FileReader("/home/angel/arcsys/hogar.csv"));
            BufferedReader br = new BufferedReader(new FileReader("/home/angel/arcsys/hogaraaaaa.csv"));
            while ((line = br.readLine()) != null) {
                String[] b = line.split(splitBy);
                pDto = new ProductDto();
                pDto.setName(b[1]);
                pDto.setCodOrigin(b[0]);
                pDto.setCategoryId(Integer.parseInt(b[2]));
                pDto.setFactoryId(Integer.parseInt(b[3]));
                pDto.setPriceSale(Double.parseDouble(b[4]));
                pDto.setDescription("");
                pDto.setDel(false);
                productHelper.productCreate(pDto);
                //System.out.println(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProductRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException exio){
            Logger.getLogger(ProductRest.class.getName()).log(Level.SEVERE, null, exio);
        }

        return "xxxxxx";
    }
    
    @GET
    @Path("loadClientDinamic")
    @Produces(MediaType.APPLICATION_JSON)
    public String validClientDinamic() {
        //return ""+productHelper.validName(newName);
        String splitBy = ";";
        String line = "";
        ClientHelper ch = new ClientHelper();
        try {
            //BufferedReader br = new BufferedReader(new FileReader("/home/angel/arcsys/sillas1.csv"));
            //BufferedReader br = new BufferedReader(new FileReader("/home/angel/arcsys/oficina.csv"));
            //BufferedReader br = new BufferedReader(new FileReader("/home/angel/arcsys/hogar.csv"));
            BufferedReader br = new BufferedReader(new FileReader("c:/dinamic-client.csv"));
            Client client = null;
            while ((line = br.readLine()) != null) {
                String[] b = line.split(splitBy);
                client = new Client();
                client.setAddress(b[1]);
                client.setAttendant(b[2]);
                client.setClientId(null);
                client.setEmail("");
                client.setFax("");
                client.setLatitud("-17.403717917993816");
                client.setLongitud("-66.15366569555664");
                client.setNameInvoice(b[4]);
                client.setNit(b[5]);
                client.setPerson(null);
                client.setPhone(b[3]);
                client.setRazonSocial(b[0]);
                client.setUrl("");
                client.setZone(b[6]);
                
                ch.saveClient(client);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProductRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException exio){
            Logger.getLogger(ProductRest.class.getName()).log(Level.SEVERE, null, exio);
        }

        return "true";
    }
}
