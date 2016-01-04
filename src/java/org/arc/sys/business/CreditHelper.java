/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.CreditAmountDto;
import org.arc.sys.dto.CreditDetailDto;
import org.arc.sys.dto.CreditDto;
import org.arc.sys.dto.ListCreditDetailDto;
import org.arc.sys.dto.credit.ListPaymentDto;
import org.arc.sys.dto.credit.PaymentDto;
import org.arc.sys.hibernate.entities.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class CreditHelper implements CreditDAO {

    @Override
    public List<CreditDto> getListByProviderId(int providerId) {
        List<CreditDto> readCreditList = new ArrayList<CreditDto>();
        CreditDto ct = null;
        String queryString = "select buy, credit.creditId from Credit as credit inner join credit.buy as buy where credit.del=0 and credit.canceled=0 and credit.buy.provider.providerId=" + providerId;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Query q = session.createQuery(queryString);
            //q.setParameter("criteria", criteria);

            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            Buy buy = null;
            while (iterator.hasNext()) {
                Object[] listString = (Object[]) iterator.next();
                buy = (Buy) listString[0];
                ct = new CreditDto();
                ct.setCreditId(Integer.parseInt(listString[1].toString()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ct.setDateBuy(buy.getDateBuy().toString());
                ct.setTotal(buy.getTotal());

                q = session.createQuery("select sum(payment.amount) from Payment as payment where payment.del=0 and payment.credit.creditId=" + listString[1].toString());
                Double sumPayment = (Double) q.uniqueResult();
                ct.setPayment(sumPayment);
                ct.setBalance(buy.getTotal() - sumPayment);

                readCreditList.add(ct);

            }
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return readCreditList;
    }

    @Override
    public boolean savePaymentByCreditIdAndAmount(CreditAmountDto cad) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Credit creditUpdate = null;
        //inicio
        Query q = session.createQuery("select credit from Credit as credit where credit.creditId=:creditId and credit.del=0");
        q.setParameter("creditId", cad.getCreditId());
        creditUpdate = (Credit) q.uniqueResult();
        //fin

        Payment payment = new Payment(creditUpdate, cad.getAmount(), new Date(), false);

        if (validPaymentByCreditId(cad.getCreditId(), cad.getAmount(), session)) {
            try {
                tx = session.getTransaction();
                tx.begin();
                System.out.println("1");
                session.save(payment);
                System.out.println("2");
                session.flush();

                // inicio
                Query q14 = session.createQuery("select sum(payment.amount) from Payment as payment where payment.del=0 and payment.credit.creditId=:creditId");
                q14.setParameter("creditId", cad.getCreditId());

                System.out.println("3");
                Double cree = (Double) q14.uniqueResult();
                double totalAmount = cree.doubleValue();
                //fin
                System.out.println("4");
                if (creditUpdate.getTotal() == totalAmount) {
                    System.out.println("5");
                    creditUpdate.setCanceled(true);
                    session.update(creditUpdate);
                    System.out.println("6");
                    session.flush();
                }
                tx.commit();
                inserValid = true;
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            }
        }
        return inserValid;
    }

    @Override
    public boolean validPaymentByCreditId(int creditId, double amount, Session session) {
        boolean validPayment = false;
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select sum(payment.amount), payment.credit.total from Payment as payment where payment.del=0 and payment.credit.creditId=" + creditId);

            List list = q.list();
            Iterator i = list.iterator();
            Double total = new Double("0");
            Double payments = new Double("0");
            while (i.hasNext()) {
                Object[] object = (Object[]) i.next();
                total = (Double) object[1];
                payments = (Double) object[0];
            }
            tx.commit();
            System.out.println("valid:::: " + total + "   " + payments + " " + amount);
            if (total.doubleValue() >= payments.doubleValue() && (total.doubleValue() >= payments.doubleValue() + amount)) {
                validPayment = true;
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        System.out.println("valid:::::::::: " + validPayment);
        return validPayment;
    }

    @Override
    public Credit getCreditById(int creditId) {
        Credit credit = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select credit from Credit as credit where credit.creditId=:creditId and credit.del=0");
            q.setParameter("creditId", creditId);

            credit = (Credit) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return credit;
    }

    @Override
    public double getSumPayments(int creditId) {
        double credit = 0;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select sum(payment.amount) from Payment as payment where payment.del=0 and payment.credit.creditId=:creditId");
            q.setParameter("creditId", creditId);

            Double cree = (Double) q.uniqueResult();
            credit = cree.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return credit;
    }

    @Override
    public ListCreditDetailDto getListByCriteria(int providerId, int buyCancel, String dateStart, String dateEnd) {
        ListCreditDetailDto readCreditList = new ListCreditDetailDto();
        readCreditList.setaaData(new ArrayList<CreditDetailDto>());
        CreditDetailDto ct = null;
        String queryString = "select buy, credit.creditId from Credit as credit inner join credit.buy as buy where credit.del=0 ";
        if (providerId != -1) {
            queryString += "and credit.buy.provider.providerId=" + providerId + " ";
        }
        if (buyCancel == 1) {
            queryString += "and credit.canceled = 1";
        }
        if (buyCancel == 2) {
            queryString += "and credit.canceled = 0";
        }
        if (dateStart.compareTo("") != 0) {
            queryString += " and buy.dateBuy BETWEEN :dateStart  and :dateEnd";
        }
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Query q = session.createQuery(queryString);
            if (dateStart.compareTo("") != 0) {
                q.setParameter("dateStart", dateStart + " 00:00:00");
                q.setParameter("dateEnd", dateEnd + " 23:59:59");
            }
            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            Buy buy = null;
            while (iterator.hasNext()) {
                Object[] listString = (Object[]) iterator.next();
                buy = (Buy) listString[0];
                ct = new CreditDetailDto();
                ct.setNameProvider(buy.getProvider().getBusinessName());
                ct.setCreditId(Integer.parseInt(listString[1].toString()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ct.setDateBuy(buy.getDateBuy().toString());
                //System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqq:: "+buy.getDateBuy());
                ct.setTotal(buy.getTotal());

                q = session.createQuery("select sum(payment.amount) from Payment as payment where payment.del=0 and payment.credit.creditId=" + listString[1].toString());
                Double sumPayment = (Double) q.uniqueResult();
                ct.setPayment(sumPayment);
                ct.setBalance(buy.getTotal() - sumPayment);

                readCreditList.getaaData().add(ct);

            }
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return readCreditList;
    }

    @Override
    public ListPaymentDto getListPayment(int creditId) {
        ListPaymentDto list = new ListPaymentDto();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query q = session.createQuery("select payment from Payment as payment where payment.credit.creditId=:creditId");
        q.setParameter("creditId", creditId);

        List<Payment> listResult = q.list();

        List<PaymentDto> listPaymentDto = new ArrayList<PaymentDto>();
        PaymentDto pDto = null;
        Iterator iterator = listResult.iterator();
        double paymentTotal = 0;
        while (iterator.hasNext()) {
            Payment p = (Payment) iterator.next();
            pDto = new PaymentDto();
            pDto.setAmount(p.getAmount());
            pDto.setDatePayment(p.getDatePayment().toString());
            paymentTotal += p.getAmount();
            listPaymentDto.add(pDto);
        }
        
        String queryString = "select buy.provider.businessName, buy.dateBuy, buy.total from Credit as credit inner join credit.buy as buy ";
        queryString += "where credit.del=0 AND credit.creditId = :creditId";
        Query q1 = session.createQuery(queryString);
        q1.setParameter("creditId", creditId);
        
        Object[] payDetail = (Object[]) q1.uniqueResult();
        list.setTotalPayment(paymentTotal);
        list.setDateBuy(payDetail[1].toString());
        list.setProviderName(payDetail[0].toString());
        list.setTotalBuy(Double.parseDouble(payDetail[2].toString()));
        list.setList(listPaymentDto);
        
        return list;
    }
}
