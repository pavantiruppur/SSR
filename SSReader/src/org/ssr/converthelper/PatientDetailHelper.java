package org.ssr.converthelper;

import java.util.ArrayList;
import java.util.List;

import org.ssr.bo.PatientDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.dao.CommonQueries;
import org.ssr.dao.patientdetails.PatientDetails;

public class PatientDetailHelper {

	static CommonQueries<PatientDetails> commonQueries = new CommonQueries<PatientDetails>();
	static Long nextValue = null;
	
	static{
		nextValue = commonQueries.getMaxValue("select max(patientId) from PatientDetails");
		nextValue = nextValue == null ? 1000l : nextValue + 1;
	}
	
	public static Long getNextIdNumber(){
		return nextValue;
	}
	
	public static void incrementNextId(){
		nextValue++;
	}
	
	public static PatientDetailsBO convertFromDAOtoBO(PatientDetails patientDetail){
		if(patientDetail == null){
			return null;
		}
		PatientDetailsBO patientDetailBO = new PatientDetailsBO();
		patientDetailBO.setAge(patientDetail.getAge());
		patientDetailBO.setEmailId(patientDetail.getMailId());
		patientDetailBO.setFirstName(patientDetail.getFirstName());
		patientDetailBO.setGender(patientDetail.getGender());
		patientDetailBO.setLastName(patientDetail.getLastName());
		patientDetailBO.setPatientId(patientDetail.getPatientId());
		patientDetailBO.setPhoneNo(patientDetail.getPhoneNo());
		return patientDetailBO;
	}

	public static PatientDetails convertFromBOtoDAO(PatientDetailsBO patientDetailBO){
		if(patientDetailBO == null){
			return null;
		}
		PatientDetails patientDetail = new PatientDetails();
		patientDetail.setAge(patientDetailBO.getAge());
		patientDetail.setMailId(patientDetailBO.getEmailId());
		patientDetail.setFirstName(patientDetailBO.getFirstName());
		patientDetail.setGender(patientDetailBO.getGender());
		patientDetail.setLastName(patientDetailBO.getLastName());
		patientDetail.setPatientId(patientDetailBO.getPatientId());
		patientDetail.setPhoneNo(patientDetailBO.getPhoneNo());
		return patientDetail;
	}
	
	public static List<PatientDetailsBO> getPatientDetails(List<PatientDetails> patientDetail){
		List<PatientDetailsBO> patientDetailBOList = new ArrayList<PatientDetailsBO>();
		for(PatientDetails patient : patientDetail){
			patientDetailBOList.add(convertFromDAOtoBO(patient));
		}
		return patientDetailBOList;
	}
	
	public static List<PatientDetailsBO> getAllPatientDetails(String keyWord){
		String query = "from PatientDetails";
		if(keyWord != null && !keyWord.trim().equals("")){
			query += " where patientId like '%" + keyWord +"%' or firstName like '%" 
			+ keyWord +"%'";
		}
		List<PatientDetails> patientList = commonQueries.getAllDetails(query);
		return getPatientDetails(patientList);
	}
	
	public static String[][] getDataStrAry(List<PatientDetailsBO> qcBOList){
		String data[][] = new String[qcBOList.size()][5];
		for(int i=0;i<qcBOList.size();i++){
			PatientDetailsBO qcBO = qcBOList.get(i);
			data[i][0] = String.valueOf(qcBO.getPatientId());
			data[i][1] = qcBO.getFirstName();
			data[i][2] = qcBO.getLastName();
			data[i][3] = String.valueOf(qcBO.getAge());
			data[i][4] = String.valueOf(qcBO.getGender());
		}
		return data;
	}
	
	public static PatientDetailsBO getPatientDetail(Long parameterId){
		PatientDetails parameterDetail = commonQueries.getDetailForId(PatientDetails.class, parameterId);
		return convertFromDAOtoBO(parameterDetail);
	}
	
	public static void addPatient(PatientDetailsBO patientDetailBO){
		CommonQueries<PatientDetails> commonQueries = new CommonQueries<PatientDetails>();
		PatientDetails parameterDetail = convertFromBOtoDAO(patientDetailBO);
		commonQueries.createEntry(parameterDetail);
		incrementNextId();
	}
}
