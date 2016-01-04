/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.ListProductRefund;
import org.arc.sys.dto.ProductRefund;
import org.arc.sys.hibernate.entities.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class RefundHelper implements RefundDAO {

    @Override
    public ListProductRefund getListProductRefund(String ids, int branchId) {
        String[] idsArray = ids.split(",");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ListProductRefund lps = new ListProductRefund();
        lps.setBranchId(branchId);
        List<ProductRefund> listProductRefund = new ArrayList<ProductRefund>();
        ProductRefund ps = null;
        Product product = null;
        Query q = null;
        for (int i = 0; i < idsArray.length; i++) {
            String string = idsArray[i];

            tx = session.getTransaction();
            tx.begin();

            q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
            q.setParameter("proId", string);
            product = (Product) q.uniqueResult();

            if (product != null) {
                ps = new ProductRefund();
                ps.setAmount(1);
                ps.setProductName(product.getName());
                ps.setFatoryName(product.getFactory().getName());
                ps.setProductId(product.getProductId());
                ps.setCategoryName(product.getCategory().getCategoryName());
                ps.setProductCodOrigin(product.getCodOrigin());
                ps.setLimitedAmount(RecordProductBranchHelper.getRpbTotalByProductIdAndBranchId(product.getProductId(), branchId,session));
                listProductRefund.add(ps);
            }
            session.flush();
            tx.commit();
        }
        session.close();
        lps.setList(listProductRefund);
        return lps;
    }

    @Override
    public boolean saveListProductRefund(ListProductRefund lps) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        Refund newRefund = null;
        try {
            newRefund = new Refund(new Date(), lps.getTotal(), false);
            newRefund.setUserId(lps.getUserId());
            tx.begin();

            session.save(newRefund);
            session.flush();

            Iterator<ProductRefund> productRefund = lps.getList().iterator();
            DetailRefund detailRefund = null;
            RecordProductBranch newRpb = null;
            RecordProducto rp = null;
            Branch branch = BranchHelper.getBranchByIdStatic(lps.getBranchId());
            Product productAux = null;
            while (productRefund.hasNext()) {
                ProductRefund productRefundResult = productRefund.next();
                productAux = ProductHelper.getProductByIdStatic(productRefundResult.getProductId());

                detailRefund = new DetailRefund();
                detailRefund.setAmount((int) productRefundResult.getAmount());
                detailRefund.setBranch(branch);
                detailRefund.setDel(false);
                detailRefund.setProduct(productAux);
                detailRefund.setRefund(newRefund);

                session.save(detailRefund);
                session.flush();

                RecordProductBranch rpbMax = null;

                // inicio
                Query q = session.createQuery("select MAX(rpb.rpbId) from RecordProductBranch as rpb where rpb.stock.product.productId=:productId and rpb.stock.branch.branchId=:branchId and rpb.del=0");
                q.setParameter("productId", productRefundResult.getProductId());
                q.setParameter("branchId", lps.getBranchId());

                Integer rpbIdMax = (Integer) q.uniqueResult();

                Query q1 = session.createQuery("select rpb from RecordProductBranch as rpb where rpb.rpbId=:rpbIdMax and rpb.del=0");
                q1.setParameter("rpbIdMax", rpbIdMax.toString());

                rpbMax = (RecordProductBranch) q1.uniqueResult();
                // fin

                rpbMax.setActive("no active");
                session.update(rpbMax);
                session.flush();

                newRpb = new RecordProductBranch();
                newRpb.setActive("active");
                newRpb.setAmount(detailRefund.getAmount());
                newRpb.setDel(false);
                newRpb.setFecha(new Date());
                newRpb.setRefundId(detailRefund.getDetailRefundId());
                newRpb.setStock(StockHelper.getStockByProductIdAndBranchId(productRefundResult.getProductId(), lps.getBranchId(),session));
                newRpb.setTotal(rpbMax.getTotal() - detailRefund.getAmount());

                session.save(newRpb);
                session.flush();

                RecordProducto rpMax = null;

                // inicio
                Query q12 = session.createQuery("select MAX(rp.rpId) from RecordProducto as rp where rp.del=0 and rp.product.productId= :productId");
                q12.setParameter("productId", productRefundResult.getProductId());

                Integer rpIdMAx = (Integer) q12.uniqueResult();
                session.flush();
                System.out.println("max::::::::::::::::::::::: rpIdMax :: " + rpIdMAx);

                Query q13 = session.createQuery("select rp from RecordProducto as rp where rp.rpId=" + rpIdMAx);

                rpMax = (RecordProducto) q13.uniqueResult();
                session.flush();
                // fin

                rpMax.setActive("no active");

                session.update(rpMax);
                session.flush();

                RecordProducto newRp = new RecordProducto();
                newRp.setActive("active");
                newRp.setAmount(detailRefund.getAmount());
                newRp.setDate(new Date());
                newRp.setDel(false);
                newRp.setProduct(productAux);
                newRp.setDetailRefund(detailRefund);
                newRp.setTotal(rpMax.getTotal() + detailRefund.getAmount());

                session.save(newRp);
                session.flush();
            }

            tx.commit();
            inserValid = true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return inserValid;
    }

    public RecordProducto getRpMaxByProductId(int productId) {
        RecordProducto rp = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            Query q = session.createQuery("select MAX(rp.rpId) from RecordProducto as rp where rp.del=0 and rp.product.productId= :productId");
            q.setParameter("productId", productId);

            Integer rpIdMAx = (Integer) q.uniqueResult();
            session.flush();

            Query q1 = session.createQuery("select rp from RecordProducto as rp where rp.rpId=" + rpIdMAx);

            rp = (RecordProducto) q1.uniqueResult();
            session.flush();

            if (rp != null) {
                System.out.println("max111111111111111::::::::::::: rpId:: " + rp.getRpId());
                System.out.println("max::::::::::::: rpTotal:: " + rp.getTotal());
                System.out.println("max::::::::::::: rpActive:: " + rp.getActive());
                System.out.println("max::::::::::::: rpAmount:: " + rp.getAmount());
            } else {
                System.out.println("max::::::::::::: NULOOOOOOOOOOOOOOOOOO");
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rp;
    }
}
