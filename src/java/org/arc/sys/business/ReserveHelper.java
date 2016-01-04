/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author angel
 */
public class ReserveHelper implements ReserveDAO {

    @Override
    public List<RecordProductDto> getListProductReserve(int branchId) {
        List<RecordProductDto> readProductList = new ArrayList<RecordProductDto>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            StringBuffer query = new StringBuffer("SELECT distinct product.productId, "
                    + "product.name, product.codOrigin, "
                    + "product.category.categoryId, product.category.categoryName, "
                    + "product.factory.factoryId, product.factory.name "
                    + "FROM DetailSale ds inner join ds.product as product "
                    + "WHERE ds.sale.reserve.stateReserve=1 and ");
            if(branchId == 0)
                query.append("ds.branch.branchId IS NULL ");
            else
                query.append("ds.branch.branchId="+branchId);
            Query q = session.createQuery(query.toString());
            //q.setParameter("criteria", criteria);

            List listResult = q.list();
            Iterator it = listResult.iterator();
            RecordProductDto recordDto = null;
            Query q1 = null;
            StringBuffer query1 = new StringBuffer("SELECT SUM(ds.amount) "
                    + "FROM DetailSale ds "
                    + "WHERE ds.sale.reserve.stateReserve=1 and ");
            if(branchId == 0)
                query1.append("ds.branch.branchId IS NULL and ");
            else
                query1.append("ds.branch.branchId="+branchId+" and ");
            query1.append("ds.product.productId=");
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                recordDto = new RecordProductDto();
                recordDto.setProductId(Integer.parseInt(listString[0].toString()));
                recordDto.setProductName(listString[1].toString());
                recordDto.setCodOrigin(listString[2].toString());
                recordDto.setCategoryId(Integer.parseInt(listString[3].toString()));
                recordDto.setCategoryName(listString[4].toString());
                recordDto.setFactoryId(Integer.parseInt(listString[5].toString()));
                recordDto.setFactoryName(listString[6].toString());

                readProductList.add(recordDto);
                
                q = session.createQuery(query1.toString()+listString[0].toString());
                Double resultSum = (Double)q.uniqueResult();
                recordDto.setTotal(resultSum);
            }
        } catch (HibernateException he) {
            //System.out.println(he.getMessage());
            he.printStackTrace();
        }

        return readProductList;
    }

}
