/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author angel
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.arc.sys.rest.BranchRest.class);
        resources.add(org.arc.sys.rest.BuyRest.class);
        resources.add(org.arc.sys.rest.CategoryRest.class);
        resources.add(org.arc.sys.rest.ClientRest.class);
        resources.add(org.arc.sys.rest.CreditRest.class);
        resources.add(org.arc.sys.rest.DeliveryRest.class);
        resources.add(org.arc.sys.rest.DetailBuyRest.class);
        resources.add(org.arc.sys.rest.EmployeeRest.class);
        resources.add(org.arc.sys.rest.FactoryRest.class);
        resources.add(org.arc.sys.rest.InventoryRest.class);
        resources.add(org.arc.sys.rest.PersonRest.class);
        resources.add(org.arc.sys.rest.ProductRest.class);
        resources.add(org.arc.sys.rest.ProviderRest.class);
        resources.add(org.arc.sys.rest.RecordProductBranchRest.class);
        resources.add(org.arc.sys.rest.RecordProductRest.class);
        resources.add(org.arc.sys.rest.RefundRest.class);
        resources.add(org.arc.sys.rest.ReserveRest.class);
        resources.add(org.arc.sys.rest.SaleRest.class);
        resources.add(org.arc.sys.rest.UserRest.class);
        resources.add(org.arc.sys.rest.auth.Product.class);
    }
    
}
