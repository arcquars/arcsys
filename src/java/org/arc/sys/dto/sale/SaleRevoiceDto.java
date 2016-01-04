package org.arc.sys.dto.sale;

import java.util.List;
import org.arc.sys.hibernate.entities.PaymentSale;

/**
 *
 * @author angel
 */
public class SaleRevoiceDto implements java.io.Serializable{
    
    private String razonSocial;
    private long nit;
    private long creditId;
    private String nameVendor;
    private String dateCreation;
    private double total;
    
    private List<PaymentSaleDto> listPayment;

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public long getNit() {
        return nit;
    }

    public void setNit(long nit) {
        this.nit = nit;
    }

    public String getNameVendor() {
        return nameVendor;
    }

    public void setNameVendor(String nameVendor) {
        this.nameVendor = nameVendor;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<PaymentSaleDto> getListPayment() {
        return listPayment;
    }

    public void setListPayment(List<PaymentSaleDto> listPayment) {
        this.listPayment = listPayment;
    }

    public long getCreditId() {
        return creditId;
    }

    public void setCreditId(long creditId) {
        this.creditId = creditId;
    }
}
