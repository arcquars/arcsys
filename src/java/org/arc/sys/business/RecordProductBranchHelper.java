/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.RecordProductBranchDto;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.RecordProductBranch;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author angel
 */
public class RecordProductBranchHelper implements RecordProductBranchDAO {

    public static RecordProductBranch getRPBByProductIdAndBranchId(int productId, int branchId) {
        RecordProductBranch rpb = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select MAX(rpb.rpbId) from RecordProductBranch as rpb where rpb.stock.product.productId=:productId and rpb.stock.branch.branchId=:branchId and rpb.del=0");
            q.setParameter("productId", productId);
            q.setParameter("branchId", branchId);

            Integer rpbIdMax = (Integer) q.uniqueResult();

            Query q1 = session.createQuery("select rpb from RecordProductBranch as rpb where rpb.rpbId=:rpbIdMax and rpb.del=0");
            q1.setParameter("rpbIdMax", rpbIdMax.toString());

            rpb = (RecordProductBranch) q1.uniqueResult();
            if (rpb != null) {
                System.out.println("rpb::: id:: " + rpb.getRpbId());
                System.out.println("rpb::: total:: " + rpb.getTotal());
            } else {
                System.out.println("rpb::: NULLLLLLLLLLLLLLLLLLLL ::");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rpb;
    }

    @Override
    public List<RecordProductBranchDto> getProductInBranchByCriteria(int branchId, int type, String criteria) {
        System.out.println("criteria__::::::::::::::::::::" + criteria);
        List<RecordProductBranchDto> readProductList = new ArrayList<RecordProductBranchDto>();
        StringBuffer queryString = new StringBuffer("select  "
                + "rpb.stock.branch.branchId, "
                + "rpb.stock.product.productId, "
                + "rpb.stock.product.name, "
                + "rpb.stock.product.codOrigin, "
                + "rpb.stock.product.category.categoryName, "
                + "rpb.stock.product.factory.name, "
                + "rpb.total "
                + "from "
                + "RecordProductBranch as rpb inner join rpb.stock as stock  inner join stock.product as product "
                + "where "
                + "rpb.del=0 and "
                + "rpb.stock.product.del=0 and "
                //+ "rpb.active like 'active' and "
                + "rpb.rpbId in (select max(rpb1.rpbId) from RecordProductBranch as rpb1 where rpb1.stock.product.productId=product.productId "
                + "and rpb1.stock.branch.branchId="+branchId+") and "
                + "rpb.stock.branch.branchId=" + branchId + " and "
                + "rpb.total>0 and ");
        
        try {
            switch (type) {
                case 1:
                    queryString.append("rpb.stock.product.name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 2:
                    queryString.append("rpb.stock.product.codOrigin like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 3:
                    queryString.append("rpb.stock.product.category.categoryId = ");
                    queryString.append(criteria);
                    break;
                default:
                    queryString.append("rpb.stock.product.name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;

            }
            
            queryString.append(" order by rpb.stock.product.name ");
            System.out.println("query:::: " + queryString.toString());
            Session session = HibernateUtil.getSessionFactory().openSession();

            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);

            List listResult = q.list();

            Iterator it = listResult.iterator();
            RecordProductBranchDto recordDto = null;
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                recordDto = new RecordProductBranchDto();
                recordDto.setBranchId(Integer.parseInt(listString[0].toString()));
                recordDto.setProductId(Integer.parseInt(listString[1].toString()));
                recordDto.setProductName(listString[2].toString());
                recordDto.setProductCodOrigin(listString[3].toString());
                recordDto.setCategory(listString[4].toString());
                recordDto.setFactoryName(listString[5].toString());
                recordDto.setTotal(Double.parseDouble(listString[6].toString()));

                readProductList.add(recordDto);
            }
            session.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return readProductList;
    }

    public static double getRpbTotalByProductIdAndBranchId(int productId, int branchId, Session session) {
        RecordProductBranch rp = null;
        try {
            Query q = session.createQuery("select MAX(rp) from RecordProductBranch as rp where rp.del=0 and rp.stock.product.del=0 and rp.stock.product.productId=:productId and rp.stock.branch.branchId=:branchId ");
            q.setParameter("productId", productId);
            q.setParameter("branchId", branchId);

            rp = (RecordProductBranch) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rp.getTotal();
    }

    @Override
    public boolean validDeleteBranch(int branchId) {
        StringBuffer queryString = new StringBuffer("select  "
                + "COUNT(rpb) "
//                + "rpb.stock.product.productId, "
//                + "rpb.stock.product.name, "
//                + "rpb.stock.product.codOrigin, "
//                + "rpb.stock.product.category.categoryName, "
//                + "rpb.stock.product.factory.name, "
//                + "rpb.total "
                + "from "
                + "RecordProductBranch as rpb inner join rpb.stock as stock  inner join stock.product as product "
                + "where "
                + "rpb.del=0 and "
                + "rpb.stock.product.del=0 and "
                //+ "rpb.active like 'active' and "
                + "rpb.rpbId in (select max(rpb1.rpbId) from RecordProductBranch as rpb1 where rpb1.stock.product.productId=product.productId "
                + "and rpb1.stock.branch.branchId="+branchId+") and "
                + "rpb.stock.branch.branchId=" + branchId + " and "
                + "rpb.total>0 ");
        
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Query q = session.createQuery(queryString.toString());
            
            Long listResult = (Long) q.uniqueResult();
            session.close();

            if(listResult > 0){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
