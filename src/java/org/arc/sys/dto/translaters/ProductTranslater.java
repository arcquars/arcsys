/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.translaters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.ProductDetailDto;
import org.arc.sys.dto.ProductDto;
import org.arc.sys.hibernate.entities.Product;

/**
 *
 * @author angel
 */
public class ProductTranslater {
    
    public static Product translateDtoToEntities(ProductDto productoDto){
        Product productEntity = new Product();
        productEntity.setCategory(null);
        productEntity.setCodOrigin(productoDto.getCodOrigin());
        productEntity.setDel(productoDto.getDel());
        productEntity.setDescription(productoDto.getDescription());
        productEntity.setName(productoDto.getName());
        productEntity.setProductId(productoDto.getProductId());
        productEntity.setUrlPhoto(productoDto.getUrlPhoto());
        return productEntity;
    }
    
    public static ProductDto translateEntitiesToDto(Product producto){
        ProductDto productEntity = new ProductDto();
        productEntity.setCategoryId(producto.getCategory().getCategoryId());
        productEntity.setCodOrigin(producto.getCodOrigin());
        productEntity.setDel(producto.getDel());
        productEntity.setDescription(producto.getDescription());
        productEntity.setFactoryId(producto.getFactory().getFactoryId());
        productEntity.setName(producto.getName());
        productEntity.setProductId(producto.getProductId());
        productEntity.setUrlPhoto(producto.getUrlPhoto());
        return productEntity;
    }
    
    public static ProductDetailDto translateEntitiesToDto1(Product producto){
        ProductDetailDto productEntity = new ProductDetailDto();
        if(producto == null)
            return null;
        productEntity.setCategoryId(producto.getCategory().getCategoryId());
        productEntity.setCategoryName(producto.getCategory().getCategoryName());
        productEntity.setCodOrigin(producto.getCodOrigin());
        productEntity.setDel(producto.getDel());
        productEntity.setDescription(producto.getDescription());
        productEntity.setFactoryId(producto.getFactory().getFactoryId());
        productEntity.setFactoryName(producto.getFactory().getName());
        productEntity.setName(producto.getName());
        productEntity.setProductId(producto.getProductId());
        productEntity.setUrlPhoto(producto.getUrlPhoto());
        return productEntity;
    }
    
    
    public static List<ProductDetailDto> translateListEntityToDto(List<Product> lProduct){
        List<ProductDetailDto> lPdDto = new ArrayList<ProductDetailDto>();
        ProductDetailDto pdDto = null;
        Iterator<Product> iProduct = lProduct.iterator();
        
        while (iProduct.hasNext()) {
            Product product = iProduct.next();
            pdDto = translateEntitiesToDto1(product);
            lPdDto.add(pdDto);            
        }
        
        return lPdDto;
    }
}
