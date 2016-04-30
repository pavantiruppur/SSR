package org.ssr.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CommonQueries<T> {

	public Long createEntry(T t){
		Long id = 0l;
		try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
			id = (Long)session.save(t);
			tr.commit();
			System.out.println("Successfully inserted");
		} catch (HibernateException e) {
			// TODO: handle exception
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
		return id;
	}

	public void createEntry(List<T> tList){
		try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
			for(T t : tList){
				session.save(t);
			}
			tr.commit();
			System.out.println("Successfully inserted");
		} catch (HibernateException e) {
			// TODO: handle exception
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllDetails(String queryString){
		//Get All Employees
		List<T> resultList = new ArrayList<T>();
        try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
            Query query = session.createQuery(queryString);
            resultList = query.list();
            tr.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return resultList;
	}

	@SuppressWarnings("unchecked")
	public T getDetailForId(Class cls, Long id){
		//Get All Employees
		T result = null;
        try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
    		result = (T)session.get(cls , id); 
			tr.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return result;
	}

	@SuppressWarnings("unchecked")
	public T deleteDetailForId(Class cls, Long id){
		//Get All Employees
		T result = null;
        try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
    		result = (T)session.get(cls , id); 
    		if(result != null){
    			session.delete(result);
    			tr.commit();
    		}
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> deleteDetails(String queryString){
		//Get All Employees
		List<T> resultList = null;
        try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
            Query query = session.createQuery(queryString);
            resultList = query.list();
            for(T result : resultList){
    			session.delete(result);
            }
			tr.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return resultList;
	}

	public List<T> deleteDetails(List<T> resultList){
		//Get All Employees
        try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
            for(T result : resultList){
    			session.delete(result);
            }
			tr.commit();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return resultList;
	}
	
	public Long getMaxValue(String queryString){
		try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
            Query query = session.createQuery(queryString);
			return (Long)query.list().get(0);
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return null;
	}
	public void update(T t){
		try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
			session.update(t);
			tr.commit();
		} catch (HibernateException e) {
			// TODO: handle exception
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
	}
	
	public Long getCountValue(String queryString){
		try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
            Query query = session.createQuery(queryString);
			return (Long)query.list().get(0);
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return null;
	}
	
	public int updateStdRlu(String queryString){
		try{
    		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
    		Query query = session.createQuery(queryString);
    		return query.executeUpdate();
		} catch (HibernateException e) {
			System.out.println(e);
		} finally{
			HibernateUtil.closeSession();
		}
        return 0;
	}
	
}
