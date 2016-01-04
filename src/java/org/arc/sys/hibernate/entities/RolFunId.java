package org.arc.sys.hibernate.entities;
// Generated Jun 12, 2012 4:50:53 PM by Hibernate Tools 3.2.1.GA



/**
 * RolFunId generated by hbm2java
 */
public class RolFunId  implements java.io.Serializable {


     private String rolId;
     private int functionId;

    public RolFunId() {
    }

    public RolFunId(String rolId, int functionId) {
       this.rolId = rolId;
       this.functionId = functionId;
    }
   
    public String getRolId() {
        return this.rolId;
    }
    
    public void setRolId(String rolId) {
        this.rolId = rolId;
    }
    public int getFunctionId() {
        return this.functionId;
    }
    
    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof RolFunId) ) return false;
		 RolFunId castOther = ( RolFunId ) other; 
         
		 return ( (this.getRolId()==castOther.getRolId()) || ( this.getRolId()!=null && castOther.getRolId()!=null && this.getRolId().equals(castOther.getRolId()) ) )
 && (this.getFunctionId()==castOther.getFunctionId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getRolId() == null ? 0 : this.getRolId().hashCode() );
         result = 37 * result + this.getFunctionId();
         return result;
   }   


}

