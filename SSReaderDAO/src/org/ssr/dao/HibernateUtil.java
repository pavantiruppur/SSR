package org.ssr.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

	 private static SessionFactory sessionFactory = null;  
	 private static Session session = null;
	  
	static {
		try {
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
		}
	}  
	  
	 public static SessionFactory getSessionFactory() {  
	  return sessionFactory;  
	 }  
	 
	 public static void closeSession() {  
//	  getSessionFactory().close();  
	 }  
	 
	 public static Session getSession() {
		 if(session == null || !session.isOpen()){
			 session = sessionFactory.openSession();
		 }
		return session;
	}
}
