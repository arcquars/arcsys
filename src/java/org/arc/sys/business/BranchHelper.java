/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Branch;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class BranchHelper implements BranchDAO {

    @Override
    public List<Branch> getListBranchByName(String criteria) {
        List<Branch> readBranchList = null;
        StringBuffer queryString = new StringBuffer("from Branch where del = 0 and ");
        try {
            queryString.append("name like '%");
            queryString.append(criteria);
            queryString.append("%'");

            Session session = HibernateUtil.getSessionFactory().openSession();

            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);

            readBranchList = q.list();

            session.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return readBranchList;
    }

    @Override
    public Branch getBranchById(int branchId) {
        Branch branch = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select branch from Branch as branch where branch.branchId=:branchId and branch.del=0");
            q.setParameter("branchId", branchId);

            branch = (Branch) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return branch;
    }

    @Override
    public boolean updateBranch(Branch branch) {
        boolean updateValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        if (!validName(branch.getName(), branch.getBranchId())) {
            try {
                tx = session.getTransaction();
                tx.begin();
                branch = (Branch) session.merge(branch);
                session.saveOrUpdate(branch);

                session.flush();
                tx.commit();
                updateValid = true;
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
        return updateValid;
    }

    @Override
    public boolean deleteBranch(int branchId) {
        boolean deleteValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Branch branch = getBranchById(branchId);

            if (branch != null) {
                branch.setDel(Boolean.TRUE);
                session.saveOrUpdate(branch);
            }

            session.flush();
            tx.commit();
            deleteValid = true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return deleteValid;
    }

    @Override
    public boolean createBranch(Branch branch) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        if (!validName(branch.getName())) {
            try {
                tx = session.getTransaction();
                tx.begin();

                session.save(branch);
                session.flush();
                tx.commit();
                inserValid = true;
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
        return inserValid;
    }

    @Override
    public boolean validName(String newName, int branchId) {
        boolean validName = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select branch from Branch as branch where branch.name like '" + newName + "' and branch.branchId != "+branchId+" and branch.del=0");
            Branch branch = (Branch) q.uniqueResult();
            tx.commit();
            if (branch != null) {
                validName = true;
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return validName;
    }

    @Override
    public boolean validName(String newName) {
        boolean validName = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select branch from Branch as branch where branch.name like '" + newName + "' and branch.del=0");

            Branch branch = (Branch) q.uniqueResult();
            tx.commit();
            if (branch != null) {
                validName = true;
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return validName;
    }
    
    @Override
    public List<KeyValue> getAllBranch() {
        List<KeyValue> mapBranch = new ArrayList<KeyValue>();
        KeyValue kv = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select branch from Branch as branch where branch.del=0 order by branch.name");

            KeyValue kvEmpty = new KeyValue("-1", "Seleccione Sucursal");
            mapBranch.add(kvEmpty);
            List<Branch> listBranch = (List<Branch>) q.list();
            Iterator<Branch> iterable = listBranch.iterator();
            while (iterable.hasNext()) {
                Branch branch = iterable.next();
                kv = new KeyValue();
                kv.setKey(branch.getBranchId().toString());
                kv.setValue(branch.getName());
                mapBranch.add(kv);

            }
            //System.out.println("Tamaño de la lista de categorias:::::: "+category.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return mapBranch;
    }

    public static Branch getBranchByIdStatic(int branchId) {
        Branch branch = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select branch from Branch as branch where branch.branchId=:branchId and branch.del=0");
            q.setParameter("branchId", branchId);

            branch = (Branch) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return branch;
    }
    
    public static Branch getBranchByIdStatic(int branchId, Session session) {
        Branch branch = null;
        try {
            Query q = session.createQuery("select branch from Branch as branch where branch.branchId=:branchId and branch.del=0");
            q.setParameter("branchId", branchId);

            branch = (Branch) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branch;
    }

    @Override
    public List<KeyValue> getAllBranchWithMain() {
        List<KeyValue> mapBranch = new ArrayList<KeyValue>();
        KeyValue kv = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select branch from Branch as branch where branch.del=0 order by branch.name");

            KeyValue kvMain = new KeyValue("0", "Principal");
            mapBranch.add(kvMain);
            List<Branch> listBranch = (List<Branch>) q.list();
            Iterator<Branch> iterable = listBranch.iterator();
            while (iterable.hasNext()) {
                Branch branch = iterable.next();
                kv = new KeyValue();
                kv.setKey(branch.getBranchId().toString());
                kv.setValue(branch.getName());
                mapBranch.add(kv);

            }
            //System.out.println("Tamaño de la lista de categorias:::::: "+category.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return mapBranch;
    }
}
