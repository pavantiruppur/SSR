package org.ssr.converthelper;

import java.util.ArrayList;
import java.util.List;

import org.ssr.bo.ControlDetialBO;
import org.ssr.bo.ParameterDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.dao.CommonQueries;
import org.ssr.dao.parameterdetails.IParameterDetails;
import org.ssr.dao.qcmaterial.IQCMaterial;
import org.ssr.dao.qcmaterial.QCMaterialCLIA;
import org.ssr.dao.qcmaterial.QCMaterialELISA;
import org.ssr.view.MainWindow;

public class ControlDetailHelper {
	
	static CommonQueries<IQCMaterial> commonQueries = new CommonQueries<IQCMaterial>();
	
	public static void addStrip(ControlDetialBO controlDetailBO){
		IQCMaterial controlDetail = convertFromBOtoDAO(controlDetailBO);
		commonQueries.createEntry(controlDetail);
	}

	public static ControlDetialBO convertFromDAOtoBO(IQCMaterial controlDetail){
		if(controlDetail == null){
			return null;
		}
		ControlDetialBO controlDetailBO = new ControlDetialBO();
		
		controlDetailBO.setAnalyte(controlDetail.getAnalyte());
		controlDetailBO.setBarcode(controlDetail.getBarcode());
		controlDetailBO.setParameterId(controlDetail.getParameterId());
		controlDetailBO.setQcId(controlDetail.getQcId());
		controlDetailBO.setQcLot((int)controlDetail.getQclot());
		controlDetailBO.setReference(controlDetail.getReference());
		controlDetailBO.setRefPlusOrMinus(controlDetail.getRefPlusOrMinus());
		
		return controlDetailBO;
	}

	public static IQCMaterial convertFromBOtoDAO(ControlDetialBO controlDetailBO){
		if(controlDetailBO == null){
			return null;
		}
		IQCMaterial controlDetail = null;
		if(MainWindow.mode.equals("ELISA")){
			controlDetail = new QCMaterialELISA();
		} else {
			controlDetail = new QCMaterialCLIA();
		}
		
		controlDetail.setAnalyte(controlDetailBO.getAnalyte());
		controlDetail.setBarcode(controlDetailBO.getBarcode());
		controlDetail.setParameterId(controlDetailBO.getParameterId());
		controlDetail.setQcId(controlDetailBO.getQcId());
		controlDetail.setQclot(controlDetailBO.getQcLot());
		controlDetail.setReference(controlDetailBO.getReference());
		controlDetail.setRefPlusOrMinus(controlDetailBO.getRefPlusOrMinus());
		
		return controlDetail;
	}
	
	public static ControlDetialBO getControlDetial(Long controlId){
		IQCMaterial controlDetail = commonQueries.getDetailForId(ReaderComponents.getTableNameForMode(IQCMaterial.class), controlId);
		ControlDetialBO controlDetailBO = convertFromDAOtoBO(controlDetail);
		return controlDetailBO;
	}
	
	public static List<ControlDetialBO> getAllControlDetials(){
		List<IQCMaterial> controlDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IQCMaterial.class).getSimpleName());
		List<ControlDetialBO> controlDetailBO = getControlDetials(controlDetail);
		return controlDetailBO;
	}
	
	public static List<ControlDetialBO> getAllControlDetialsForWhere(String whereCond){
		List<IQCMaterial> controlDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IQCMaterial.class).getSimpleName()+ " " + whereCond);
		List<ControlDetialBO> controlDetailBO = getControlDetials(controlDetail);
		return controlDetailBO;
	}
	
	public static List<ControlDetialBO> getControlDetials(List<IQCMaterial> controlDetails){
		List<ControlDetialBO> controlDetailsBOList = new ArrayList<ControlDetialBO>();
		for(IQCMaterial control : controlDetails){
			controlDetailsBOList.add(convertFromDAOtoBO(control));
		}
		return controlDetailsBOList;
	}
	
	public static void deleteAllCalibrationRecords(){
		List<IQCMaterial> controlDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IQCMaterial.class).getSimpleName()+ " where test_type = 1");
		if(controlDetail != null && controlDetail.size() > 0){
			commonQueries.deleteDetails(controlDetail);
		}
	}
	
	public static void deleteAllRecords(){
		commonQueries.deleteDetails("from "+ ReaderComponents.getTableNameForMode(IQCMaterial.class).getSimpleName());
	}
	
	public static void updateStrip(ControlDetialBO controlDetailBO){
		IQCMaterial controlDetail = convertFromBOtoDAO(controlDetailBO);
		commonQueries.update(controlDetail);
	}
	
	public static int getTotalCount(){
		Long count = commonQueries.getCountValue("select count(*) from "+ ReaderComponents.getTableNameForMode(IQCMaterial.class).getSimpleName());
		return count == null ? 0 : count.intValue();
	}

	public static String[][] getControlDataStrAry(List<ControlDetialBO> qcBOList){
		String data[][] = new String[qcBOList.size()][6];
		for(int i=0;i<qcBOList.size();i++){
			ControlDetialBO qcBO = qcBOList.get(i);
			data[i][0] = String.valueOf(qcBO.getQcId());
			data[i][1] = qcBO.getBarcode();
			data[i][2] = qcBO.getAnalyte();
			data[i][3] = String.valueOf(qcBO.getQcLot());
			data[i][4] = String.valueOf(qcBO.getReference());
			data[i][5] = String.valueOf(qcBO.getRefPlusOrMinus());
		}
		return data;
	}
}
