package org.arc.sys.hibernate.entities;
// Generated Jun 12, 2012 4:50:53 PM by Hibernate Tools 3.2.1.GA



/**
 * Rol generated by hbm2java
 */
public class Rol  implements java.io.Serializable {


     private String rolId;
     private String nameRol;
     private Boolean del;

    public Rol() {
    }

	
    public Rol(String rolId, String nameRol) {
        this.rolId = rolId;
        this.nameRol = nameRol;
    }
    public Rol(String rolId, String nameRol, Boolean del) {
       this.rolId = rolId;
       this.nameRol = nameRol;
       this.del = del;
    }
   
    public String getRolId() {
        return this.rolId;
    }
    
    public void setRolId(String rolId) {
        this.rolId = rolId;
    }
    public String getNameRol() {
        return this.nameRol;
    }
    
    public void setNameRol(String nameRol) {
        this.nameRol = nameRol;
    }
    public Boolean getDel() {
        return this.del;
    }
    
    public void setDel(Boolean del) {
        this.del = del;
    }




}


