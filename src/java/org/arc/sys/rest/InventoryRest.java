/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.BranchHelper;
import org.arc.sys.dto.ProductInventoryDto;

/**
 *
 * @author angel
 */
@Path("inventory")
public class InventoryRest {
    
    @Context
    private UriInfo context;
    private BranchHelper branchHelper;

    public InventoryRest() {
        this.branchHelper = new BranchHelper();
    }
    
    @GET
    @Path("findByCriteria")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductInventoryDto> findByCriteria(@QueryParam(value="type") int type, @QueryParam(value = "criteria") String criteria) {
        return getListProductInventori();
    }
    
    private List<ProductInventoryDto> getListProductInventori(){
        List<ProductInventoryDto> list = new ArrayList<ProductInventoryDto>();
        ProductInventoryDto pid = new ProductInventoryDto();
        pid.setAmount(12);
        pid.setCategory("Video juegos");
        pid.setName("ps vita");
        pid.setPrice(250);
        pid.setProdId(2);
        list.add(pid);
        pid = new ProductInventoryDto();
        pid.setAmount(5);
        pid.setCategory("Video juegos");
        pid.setName("ps 2");
        pid.setPrice(180);
        pid.setProdId(3);
        list.add(pid);
        pid = new ProductInventoryDto();
        pid.setAmount(7);
        pid.setCategory("Televisores");
        pid.setName("televisor LG 11'");
        pid.setPrice(350);
        pid.setProdId(4);
        list.add(pid);
        pid = new ProductInventoryDto();
        pid.setAmount(2);
        pid.setCategory("Televisores");
        pid.setName("Televisor Sony 35'");
        pid.setPrice(520);
        pid.setProdId(5);
        list.add(pid);
        pid = new ProductInventoryDto();
        pid.setAmount(6);
        pid.setCategory("Mini componente");
        pid.setName("radio lg");
        pid.setPrice(80);
        pid.setProdId(6);
        list.add(pid);
        return list;
    }
    
    
}
