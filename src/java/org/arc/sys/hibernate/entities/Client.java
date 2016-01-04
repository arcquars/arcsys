package org.arc.sys.hibernate.entities;
// Generated 22-04-2015 08:05:08 PM by Hibernate Tools 4.3.1
/**
 * Client generated by hbm2java
 */
public class Client  implements java.io.Serializable {


     private Long clientId;
     private String razonSocial;
     private String nameInvoice;
     private String address;
     private String latitud;
     private String longitud;
     private String phone;
     private String fax;
     private String url;
     private String email;
     private String nit;
     private String attendant;
     private String zone;
     private Person person;

    public Client() {
    }

	
    public Client(String nit) {
        this.nit = nit;
    }
    public Client(String address, String latitud, String longitud, String phone, String fax, String url, String email, String nit, Person person) {
       this.address = address;
       this.latitud = latitud;
       this.longitud = longitud;
       this.phone = phone;
       this.fax = fax;
       this.url = url;
       this.email = email;
       this.nit = nit;
       this.person = person;
    }
   
    public Long getClientId() {
        return this.clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLatitud() {
        return this.latitud;
    }
    
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
    public String getLongitud() {
        return this.longitud;
    }
    
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNit() {
        return this.nit;
    }
    
    public void setNit(String nit) {
        this.nit = nit;
    }
    public Person getPerson() {
        return this.person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getAttendant() {
        return attendant;
    }

    public void setAttendant(String attendant) {
        this.attendant = attendant;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getNameInvoice() {
        return nameInvoice;
    }

    public void setNameInvoice(String nameInvoice) {
        this.nameInvoice = nameInvoice;
    }

}


