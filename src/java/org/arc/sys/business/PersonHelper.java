/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.arc.sys.dto.EmployeeDetailtDto;
import org.arc.sys.dto.EmployeeDto;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.translaters.PersonTranslater;
import org.arc.sys.hibernate.entities.Employee;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.Person;
import org.arc.sys.hibernate.entities.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class PersonHelper implements PersonDAO {

    public PersonHelper() {
    }

    @Override
    public List<Person> getListPersonByName(Criteria type, String criteria) {
        List<Person> readPersonList = null;
        StringBuilder queryString = new StringBuilder("from Person where del = 0 and ");
        try {
            switch (type) {
                case names:
                    queryString.append("names like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case lastname:
                    queryString.append(" lastname like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case ci:
                    queryString.append(" ci like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                default:
                    queryString.append("names like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;

            }
            Session session = HibernateUtil.getSessionFactory().openSession();

            System.out.println("PersonHelper.getListPersonByCriteria:::::::::::::::::::::: " + session.isOpen() + " conn ::: " + session.isConnected());
            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);

            readPersonList = q.list();

            session.close();

            List<EmployeeDetailtDto> empll = getListPersonDetallByName(1, "pe");
            EmployeeDetailtDto edto = empll.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return readPersonList;
    }

    @Override
    public EmployeeDto getEmployeeByPerId(int perId) {
        User user = null;
        Employee employee = null;
        EmployeeDto eDto = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select user from User as user where user.person.perId=:perId and user.person.del=0");
            q.setParameter("perId", perId);
            user = (User) q.uniqueResult();

            Query q1 = session.createQuery("select employee from Employee as employee where employee.person.perId=:perId and employee.del=0");
            q1.setParameter("perId", perId);
            employee = (Employee) q1.uniqueResult();

            eDto = PersonTranslater.translatePersonEmployee(user, employee);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return eDto;

    }

    @Override
    public void updatePerson(Person person) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("PersonHelper.updatePerson:::::::::::::::::::::: " + session.isOpen() + " conn ::: " + session.isConnected());
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            person = (Person) session.merge(person);
            session.saveOrUpdate(person);

            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        //session.close();
        //session.close();

    }

    @Override
    public void deletePerson(int perId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select person from Person as person where person.perId=:perId and person.del=0");
            q.setParameter("perId", perId);

            Person person = (Person) q.uniqueResult();

            if (person != null) {
                person.setDel(Boolean.TRUE);
                person = (Person) session.merge(person);
                session.saveOrUpdate(person);
            }

            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public boolean createPerson(Person person) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean valSave;
        try {
            tx = session.getTransaction();
            tx.begin();

            session.save(person);
            session.flush();
            valSave = true;
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            valSave = false;
        } finally {
            session.close();
        }
        return valSave;
    }

    @Override
    public boolean ciValid(int newCi) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select person from Person as person where person.ci=:ci and person.del=0");
            q.setParameter("ci", newCi);

            Person person = (Person) q.uniqueResult();
            tx.commit();
            if (person != null) {
                return true;
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    public String firstNameByCi(int ci){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select person from Person as person where person.ci=:ci and person.del=0");
            q.setParameter("ci", ci);

            Person person = (Person) q.uniqueResult();
            tx.commit();
            if (person != null) {
                return person.getFirstname();
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return "";
    }
    
    
            
    @Override
    public List<EmployeeDetailtDto> getListPersonDetallByName(int type, String criteria) {
        List<EmployeeDetailtDto> readEmployeeList = new ArrayList<EmployeeDetailtDto>();
        StringBuffer queryString = new StringBuffer("Select person.perId, employee.empId, person.names, person.firstname, employee.position, branch.name from Employee employee right join employee.branch as branch inner join employee.person as person where employee.del = 0 and ");
        try {
            switch (type) {
                case 1:
                    queryString.append("person.names like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 2:
                    queryString.append(" person.firstname like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 3:
                    queryString.append(" person.ci like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 4:
                    queryString.append(" employee.position like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 5:
                    queryString.append(" branch.name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                default:
                    queryString.append("person.names like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;

            }
            Session session = HibernateUtil.getSessionFactory().openSession();

            System.out.println("QUERY::: " + queryString.toString());
            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);

            List list = q.list();
            System.out.println("yyyyyyyyyyyyyyyyyyyyyyy:: "+list.size());
            Iterator it = list.iterator();
            EmployeeDetailtDto empDto = null;
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                empDto = new EmployeeDetailtDto();
                empDto.setPerId(Integer.parseInt(listString[0].toString()));
                empDto.setEmpID(Integer.parseInt(listString[1].toString()));
                empDto.setNames(listString[2].toString());
                empDto.setFirstname(listString[3].toString());
                empDto.setPosition(listString[4].toString());
                empDto.setBranch(listString[5].toString());
                readEmployeeList.add(empDto);
            }

            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return readEmployeeList;
    }

    @Override
    public Map<String, String> getAllBranch() {
        Map<String, String> map = new HashMap<String, String>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("Select branch.branchId, branch.name from Branch branch where branch.del=0");

            List list = q.list();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                map.put(listString[0].toString(), listString[1].toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return map;
    }

    @Override
    public Map<String, String> getAllEmployeePosition() {
        Map<String, String> map = new HashMap<String, String>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("Select employee.empId, employee.position from Employee employee where employee.del=0");

            List list = q.list();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                map.put(listString[0].toString(), listString[1].toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return map;
    }

    @Override
    public Person getPersonByUsernamePass(String username, String password) {
        Person person = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select user.person from User as user where user.login=:username and user.password=:pass and user.del=0");
            q.setParameter("username", username);
            q.setParameter("pass", password);

            person = (Person) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return person;
    }

    @Override
    public List<KeyValue> getBranchFree(int branchId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<KeyValue> allBranch = new ArrayList<KeyValue>();
        List<KeyValue> asignBranch = new ArrayList<KeyValue>();
        try {
            Query q = session.createQuery("Select branch.branchId, branch.name from Branch branch where branch.del=0");

            List list = q.list();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                allBranch.add(new KeyValue(listString[0].toString(), listString[1].toString()));

            }

            Query q1 = session.createQuery("Select person.branch.branchId, person.branch.name from Employee person where person.del=0 and person.branch.del=0 and person.branch.branchId != "+branchId+" and  person.position like 'Administrador'");
            System.out.println("query::::::  "+q1.getQueryString());
            List list1 = q1.list();
            Iterator it1 = list1.iterator();
            while (it1.hasNext()) {
                Object[] listString = (Object[]) it1.next();
                //asignBranch.add(new KeyValue(listString[0].toString(), listString[1].toString()));
                for (int i = 0; i < allBranch.size(); i++) {
                    KeyValue keyValue = allBranch.get(i);
                    if (keyValue.getValue().equals(listString[1].toString()) == true) {
                        System.out.println("ifffffffffffffffffffffffF: "+listString[1].toString());
                        allBranch.remove(i);
                        break;
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return allBranch;

    }

    @Override
    public List<KeyValue> getAllBranchList() {
        List<KeyValue> map = new ArrayList<KeyValue>();
        map.add(new KeyValue("0", "Principal"));
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("Select branch.branchId, branch.name from Branch branch where branch.del=0");

            List list = q.list();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                map.add(new KeyValue(listString[0].toString(), listString[1].toString()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return map;
    }

    @Override
    public Person getPerIdByCi(int ci) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select person from Person as person where person.ci=:ci and person.del=0");
            q.setParameter("ci", ci);

            Person person = (Person) q.uniqueResult();
            tx.commit();
            if (person != null) {
                return person;
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public Person getPersonByCi(int ci) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        Person person = null;
        try {
            tx.begin();

            Query q = session.createQuery("select person from Person as person where person.ci=:ci and person.del=0");
            q.setParameter("ci", ci);

            person = (Person) q.uniqueResult();
            tx.commit();
            if (person != null) {
                return person;
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return person;
    }

    @Override
    public Person updateP(Person person) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            person = (Person) session.merge(person);
            session.saveOrUpdate(person);

            session.flush();
            System.out.println("PersonHelper.updatePerson:::::::::::::::::::::: " + person.getPerId());
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            //session.close();
        }
        System.out.println("1PersonHelper.updatePerson:::::::::::::::::::::: " + person.getPerId());
        return person;
    }
}
