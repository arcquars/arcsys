package org.arc.sys.hibernate.entities;
// Generated Jun 12, 2012 4:50:53 PM by Hibernate Tools 3.2.1.GA



/**
 * Product generated by hbm2java
 */
public class Product  implements java.io.Serializable {


     private Integer productId;
     private Category category;
     private Factory factory;
     private String name;
     private String codOrigin;
     private String description;
     private String urlPhoto;
     private Boolean del;

    public Product() {
    }

    public Product(Category category, Factory factory, String name, String codOrigin, String description, String urlPhoto, Boolean del) {
       this.category = category;
       this.factory = factory;
       this.name = name;
       this.codOrigin = codOrigin;
       this.description = description;
       this.urlPhoto = urlPhoto;
       this.del = del;
    }
   
    public Integer getProductId() {
        return this.productId;
    }
    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getCodOrigin() {
        return this.codOrigin;
    }
    
    public void setCodOrigin(String codOrigin) {
        this.codOrigin = codOrigin;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrlPhoto() {
        return this.urlPhoto;
    }
    
    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
    public Boolean getDel() {
        return this.del;
    }
    
    public void setDel(Boolean del) {
        this.del = del;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }




}


