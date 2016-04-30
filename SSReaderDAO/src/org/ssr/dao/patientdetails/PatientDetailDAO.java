package org.ssr.dao.patientdetails;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ssr.dao.HibernateUtil;

public class PatientDetailDAO {

	public Long createPatient(PatientDetails patient){
		Long id = 0l;
		try{
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction tr = session.beginTransaction();
			id = (Long)session.save(patient);
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
	
	@SuppressWarnings("unchecked")
	public List<PatientDetails> getAllDetails(){
		//Get All Employees
		Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from PatientDetails ");
        List<PatientDetails> empList = query.list();
        for(PatientDetails emp : empList){
            System.out.println("List of Employees::"+emp.getPatientId()+","+emp.getFirstName());
        }
        return null;
	}
}
