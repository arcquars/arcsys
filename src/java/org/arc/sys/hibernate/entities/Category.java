package org.arc.sys.hibernate.entities;
// Generated Jun 12, 2012 4:50:53 PM by Hibernate Tools 3.2.1.GA



/**
 * Category generated by hbm2java
 */
public class Category  implements java.io.Serializable {


     private Integer categoryId;
     private String categoryName;
     private Boolean del;

    public Category() {
    }

	
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
    public Category(String categoryName, Boolean del) {
       this.categoryName = categoryName;
       this.del = del;
    }
   
    public Integer getCategoryId() {
        return this.categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public Boolean getDel() {
        return this.del;
    }
    
    public void setDel(Boolean del) {
        this.del = del;
    }




}

