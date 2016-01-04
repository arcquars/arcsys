/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import java.util.Map;
import org.arc.sys.dto.ProductDetailDto;
import org.arc.sys.dto.ProductDto;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.hibernate.entities.Category;
import org.arc.sys.hibernate.entities.Factory;
import org.arc.sys.hibernate.entities.PriceSale;
import org.arc.sys.hibernate.entities.Product;

/**
 *
 * @author angel
 */
public interface ProductDAO {
    public boolean productCreate(ProductDto product);
    
    public Product getProductById(int proId);
    
    public List<ProductDetailDto> getListPersonByName(int type, String criteria);
    
    public int getCategoryByName(String nameCategory);
    
    public Map<String, String> getCategoryAll();
    
    public boolean deleteProduct(int pro_id);
    
    public void updateProduct(Product product);
    
    public boolean validName(String newName);
    
    public Category getCategoryById(int categoryId);
    
    public Factory getFactoryById(int factoryId);
    
    public List<RecordProductDto> getListProductBuy(String ids);
    
    public ProductDto getProductDtoById(int prodId);
    
    public boolean validNameEdit(String newName, int prodId);
    
    public void updateProductDto(ProductDto productDto);
    
    public PriceSale getPriceByProdId(int productId);
    
    public List<Double> getDatesPriceByProductId(int productId);
}
