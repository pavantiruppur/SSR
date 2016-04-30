package org.ssr.converthelper;

import java.util.ArrayList;
import java.util.List;

import org.ssr.bo.ControlDetialBO;
import org.ssr.components.ReaderComponents;
import org.ssr.dao.CommonQueries;
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
		controlDetailBO.setQcId(controlDetail.getQcId().intValue());
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
		controlDetail.setQcId(Long.valueOf(controlDetailBO.getQcId()));
		controlDetail.setQclot(controlDetailBO.getQcLot());
		controlDetail.setReference(controlDetailBO.getReference());
		controlDetail.setRefPlusOrMinus(controlDetailBO.getRefPlusOrMinus());
		
		return controlDetail;
	}
	
	public static ControlDetialBO getStripDetail(Long stripId){
		IQCMaterial controlDetail = commonQueries.getDetailForId(ReaderComponents.getTableNameForMode(IQCMaterial.class), stripId);
		ControlDetialBO controlDetailBO = convertFromDAOtoBO(controlDetail);
		return controlDetailBO;
	}
	
	public static List<ControlDetialBO> getAllStripDetails(){
		List<IQCMaterial> controlDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IQCMaterial.class).getSimpleName());
		List<ControlDetialBO> controlDetailBO = getStripDetails(controlDetail);
		return controlDetailBO;
	}
	
	public static List<ControlDetialBO> getAllStripDetailsForWhere(String whereCond){
		List<IQCMaterial> controlDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IQCMaterial.class).getSimpleName()+ " " + whereCond);
		List<ControlDetialBO> controlDetailBO = getStripDetails(controlDetail);
		return controlDetailBO;
	}
	
	public static List<ControlDetialBO> getStripDetails(List<IQCMaterial> controlDetails){
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
	
}
