/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.html.HTML;
import org.arc.sys.dto.sale.PaymentSaleDto;
import org.arc.sys.dto.sale.SaleRevoiceDto;
import org.arc.sys.hibernate.entities.CreditSale;
import org.arc.sys.hibernate.entities.Employee;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.PaymentSale;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class SaleDetailHelper implements SaleDetailDAO{

    @Override
    public SaleRevoiceDto getSaleRevoice(long salId, long creditId) {
        SaleRevoiceDto srDto = new SaleRevoiceDto();
        List<PaymentSale> listPayments = new ArrayList<PaymentSale>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            
            String query = "from CreditSale cs "
                    + "where cs.creditId =:creditId AND "
                    + "cs.canceled =:cancel ";
            Query q = session.createQuery(query);
            q.setParameter("creditId", creditId);
            q.setParameter("cancel", 0);
            
            CreditSale cs = (CreditSale) q.uniqueResult();
            
            String queryS = "select "
                    + "sale.salId, sale.dateSale, sale.client.razonSocial, sale.client.nit, sale.reserve.reserveId, sale.creditSale.creditId, sale.total, sale.vendorId, sale.branchId "
                    + "from Sale as sale "
                    + "where sale.salId=:salId ";
            Query qSale = session.createQuery(queryS);
            qSale.setParameter("salId", salId);
            Object[] obj = (Object[])qSale.uniqueResult();
            
            srDto.setDateCreation(obj[1].toString());
            srDto.setNit(Long.valueOf(obj[3].toString()));
            srDto.setRazonSocial(obj[2].toString());
            srDto.setTotal(Double.valueOf(obj[6].toString()));
            srDto.setCreditId(cs.getCreditId());
            
            if(obj[7] != null){
                String queryVendor ="from Employee employee "
                        +"where employee.empId=:empId "
                        +"";
                Query qEmp = session.createQuery(queryVendor);
                qEmp.setParameter("empId", obj[7].toString());
                
                Employee employee = (Employee) qEmp.uniqueResult();
                
                String names = "";
                if(employee != null){
                    names += employee.getPerson().getFirstname() + " ";
                    names += employee.getPerson().getLastname() + " ";
                    names += employee.getPerson().getNames();
                }
                
                srDto.setNameVendor(names);
            }
            
            ArrayList<PaymentSale> dd = new ArrayList<PaymentSale>(cs.getPaymentSales());
            PaymentSale ps = null;
            PaymentSaleDto psDto = null;
            List<PaymentSaleDto> ddDto = new ArrayList<PaymentSaleDto>();
            for (int i = 0; i < dd.size(); i++) {
                ps = dd.get(i);
                psDto = new PaymentSaleDto();
                    psDto.setAmount(ps.getAmount());
                    psDto.setCanceled(cs.isCanceled());
                psDto.setCreateDate(ps.getCreateDate());
                psDto.setCreditId(cs.getCreditId());
                psDto.setDel(cs.isDel());
                psDto.setPaymentId(ps.getPaymentId());
                psDto.setTotal(cs.getTotal());
                psDto.setUserid(ps.getUserid());
                
                ddDto.add(psDto);
            }
            srDto.setListPayment(ddDto);

            
            
        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in ListSale: ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        return srDto;
    }
    
    @Override
    public SaleRevoiceDto getSaleReport(long salId, long creditId) {
        SaleRevoiceDto srDto = new SaleRevoiceDto();
        List<PaymentSale> listPayments = new ArrayList<PaymentSale>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            
            String query = "from CreditSale cs "
                    + "where cs.creditId =:creditId ";
            Query q = session.createQuery(query);
            q.setParameter("creditId", creditId);
            
            CreditSale cs = (CreditSale) q.uniqueResult();
            
            String queryS = "select "
                    + "sale.salId, sale.dateSale, sale.client.razonSocial, sale.client.nit, sale.reserve.reserveId, sale.creditSale.creditId, sale.total, sale.vendorId, sale.branchId "
                    + "from Sale as sale "
                    + "where sale.salId=:salId ";
            Query qSale = session.createQuery(queryS);
            qSale.setParameter("salId", salId);
            Object[] obj = (Object[])qSale.uniqueResult();
            
            srDto.setDateCreation(obj[1].toString());
            srDto.setNit(Long.valueOf(obj[3].toString()));
            srDto.setRazonSocial(obj[2].toString());
            srDto.setTotal(Double.valueOf(obj[6].toString()));
            srDto.setCreditId(cs.getCreditId());
            
            if(obj[7] != null){
                String queryVendor ="from Employee employee "
                        +"where employee.empId=:empId "
                        +"";
                Query qEmp = session.createQuery(queryVendor);
                qEmp.setParameter("empId", obj[7].toString());
                
                Employee employee = (Employee) qEmp.uniqueResult();
                
                String names = "";
                if(employee != null){
                    names += employee.getPerson().getFirstname() + " ";
                    names += employee.getPerson().getLastname() + " ";
                    names += employee.getPerson().getNames();
                }
                
                srDto.setNameVendor(names);
            }
            
            ArrayList<PaymentSale> dd = new ArrayList<PaymentSale>(cs.getPaymentSales());
            PaymentSale ps = null;
            PaymentSaleDto psDto = null;
            List<PaymentSaleDto> ddDto = new ArrayList<PaymentSaleDto>();
            for (int i = 0; i < dd.size(); i++) {
                ps = dd.get(i);
                psDto = new PaymentSaleDto();
                    psDto.setAmount(ps.getAmount());
                    psDto.setCanceled(cs.isCanceled());
                psDto.setCreateDate(ps.getCreateDate());
                psDto.setCreditId(cs.getCreditId());
                psDto.setDel(cs.isDel());
                psDto.setPaymentId(ps.getPaymentId());
                psDto.setTotal(cs.getTotal());
                psDto.setUserid(ps.getUserid());
                
                ddDto.add(psDto);
            }
            srDto.setListPayment(ddDto);

            
            
        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in ListSale: ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        return srDto;
    }

    @Override
    public boolean savePaymentSale(long creditId, double payment, int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean valSave;
        try {
            tx = session.getTransaction();
            tx.begin();

            PaymentSale ps = new PaymentSale();
            ps.setAmount(payment);
//            ps.setPaymentId(Long.MIN_VALUE);
            ps.setCreditSale(null);
            ps.setUserid(userId);
            
            String query = "from CreditSale cs "
                    + "where cs.creditId =:creditId";
            Query q = session.createQuery(query);
            q.setParameter("creditId", creditId);
            
            CreditSale cs = (CreditSale) q.uniqueResult();
            
            double total = payment;
            for (PaymentSale s : cs.getPaymentSales()) {
                total += s.getAmount();
            }
            
            if(cs.getTotal() <= total){
                cs.setCanceled(true);
                session.update(cs);
            }
            
            ps.setCreditSale(cs);
            session.save(ps);
            valSave = true;
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            valSave = false;
        } finally {
            session.close();
        }
        return valSave;
    }
    
}
