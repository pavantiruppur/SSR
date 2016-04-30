package org.ssr.dao;

import java.util.Date;
import java.util.List;

import org.ssr.dao.parameterdetails.IParameterDetails;
import org.ssr.dao.parameterdetails.ParameterDetailsCLIA;
import org.ssr.dao.patientdetails.PatientDetails;
import org.ssr.dao.standarddetails.IStandardDetails;

public class Test {

	public static void main(String[] args) {
		Test test = new Test();
		test.testParameterDetails();
//		System.out.println(getTableNameForMode(IParameterDetails.class).getSimpleName());
//		CommonQueries<ResultDetailELISA> patientDetails = new CommonQueries<ResultDetailELISA>();
//		List<ResultDetailELISA> empList = patientDetails.getAllDetails("from ResultDetailELISA");
	}
	
	public void testPatientDetails(){
		PatientDetails patient = new PatientDetails();
		patient.setAge(24);
		patient.setCreationDate(new Date());
		patient.setFirstName("Pavan");
		patient.setGender("Male");
		patient.setLastModifiedDate(new Date());
		patient.setLastName("Jagadeeswaran");
		patient.setMailId("pavantiruppur@gmail.com");
		patient.setPhoneNo("9952343479");
		
//		PatientDetailDAO patientDAO = new PatientDetailDAO();
//		Long id = patientDAO.createPatient(patient);
		CommonQueries<PatientDetails> patientDetails = new CommonQueries<PatientDetails>();
//		patientDetails.createPatient(patient);
		List<PatientDetails> empList = patientDetails.getAllDetails("from PatientDetails");
        for(PatientDetails emp : empList){
            System.out.println("List of Employees::"+emp.getPatientId()+","+emp.getFirstName());
        }
	}
	
	public void testParameterDetails(){
		/*ParameterDetailsELISA parameterDetails = new ParameterDetailsELISA();
		parameterDetails.setCreationDate(new Date());
		parameterDetails.setFormula("y=mx+c");
		parameterDetails.setIsActive(1);
		parameterDetails.setIsCalibrate(0);
		parameterDetails.setLastModifiedDate(new Date());
		parameterDetails.setNoOfStd(6);
		parameterDetails.setParameterName("T4");
		parameterDetails.setParameterUnit("ml");
		
		CommonQueries<StandardDetailsELISA> query = new CommonQueries<StandardDetailsELISA>();
		for(int i = 1; i <= 6; i++){
			StandardDetailsELISA stdDetail = new StandardDetailsELISA();
			stdDetail.setParameterId(1020l);
			stdDetail.setStdId((long)i);
			stdDetail.setRluValue(0.34 * i);
			stdDetail.setStdValue(100.00 * i );
			stdDetail.setParameterDetail(parameterDetails);
//			query.createEntry(stdDetail);
		}*/
		
		
		
		CommonQueries<IParameterDetails> commonQueries = new CommonQueries<IParameterDetails>();
		CommonQueries<IStandardDetails> commonQueries1 = new CommonQueries<IStandardDetails>();
//		commonQueries.createEntry(parameterDetails);
		
		List<IParameterDetails> empList = commonQueries.getAllDetails("from ParameterDetailsELISA");
        for(IParameterDetails emp : empList){
            System.out.println("Parameter Id : "+ emp.getParameterId());
            System.out.println("Parameter Name : "+ emp.getParameterName());
            List<IStandardDetails> standardDetail = commonQueries1.getAllDetails("from StandardDetailsELISA where parameterId="+ String.valueOf(emp.getParameterId()));
            for(IStandardDetails std : standardDetail){
            	System.out.println(std.getStdId() + " " + std.getParameterId() + " " + std.getRluValue() + " " + std.getStdValue());
            }
            System.out.println();
        }
        List<IStandardDetails> standardDetail = commonQueries1.getAllDetails("from StandardDetailsELISA where parameterId=1038");
        for(IStandardDetails std : standardDetail){
        	System.out.println(std.getStdId() + " " + std.getParameterId() + " " + std.getRluValue() + " " + std.getStdValue());
        }
	}
	
	public void getDetailsForId(){
		CommonQueries<IParameterDetails> commonQueries = new CommonQueries<IParameterDetails>();
		IParameterDetails detail = commonQueries.getDetailForId(ParameterDetailsCLIA.class, 1000l);
		System.out.println(detail.getParameterId() + " " + detail.getFormula() + " " + detail.getNoOfStd());
	}

	
	@SuppressWarnings("unchecked")
	public static Class getTableNameForMode(Class cls){
		String[] pacName = cls.getName().split("\\.");
		String iName = pacName[pacName.length - 1];
		
		String clsName = iName.substring(1,iName.length());
		clsName = clsName + "ELISA";
		pacName[pacName.length - 1] = clsName;
		iName = "";
		for(int i = 0; i < pacName.length ; i++){
			iName = iName + pacName[i] + ((i == pacName.length - 1) ? "" : ".");
		}
		try {
			return Class.forName(iName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
