package org.ssr.converthelper;

import java.util.ArrayList;
import java.util.List;

import org.ssr.bo.StripDetailBO;
import org.ssr.components.ReaderComponents;
import org.ssr.dao.CommonQueries;
import org.ssr.dao.stripdetails.IStripDetail;
import org.ssr.dao.stripdetails.StripDetailCLIA;
import org.ssr.dao.stripdetails.StripDetailELISA;
import org.ssr.enums.CalibrationTypeEnum;
import org.ssr.enums.StripStatusEnum;
import org.ssr.enums.TestTypeEnum;
import org.ssr.view.MainWindow;

public class StripDetailHelper {
	
	static CommonQueries<IStripDetail> commonQueries = new CommonQueries<IStripDetail>();
	
	public static void addStrip(StripDetailBO stripDetailBO){
		CommonQueries<IStripDetail> commonQueries = new CommonQueries<IStripDetail>();
		commonQueries.deleteDetailForId(ReaderComponents.getTableNameForMode(IStripDetail.class), stripDetailBO.getStripId());
		IStripDetail stripDetail = convertFromBOtoDAO(stripDetailBO);
		commonQueries.createEntry(stripDetail);
	}

	public static StripDetailBO convertFromDAOtoBO(IStripDetail stripDetail){
		if(stripDetail == null){
			return null;
		}
		StripDetailBO stripDetailBO = new StripDetailBO(false);
		stripDetailBO.setCalibrationType(CalibrationTypeEnum.getEnumByKey(stripDetail.getCalibrationType()));
		stripDetailBO.setControlId(stripDetail.getControl_id());
		stripDetailBO.setParameterId(stripDetail.getParameterId());
		stripDetailBO.setPatientId(stripDetail.getPatientId());
		stripDetailBO.setResultId(stripDetail.getResult_id());
		stripDetailBO.setStripId(stripDetail.getStripId());
		stripDetailBO.setStripStatus(StripStatusEnum.getEnumByKey(stripDetail.getStatus()));
		stripDetailBO.setTestType(TestTypeEnum.getEnumByKey(stripDetail.getTest_type()));
		stripDetailBO.setParameterDetail(ParameterDetailHelper.getParameterDetail(stripDetail.getParameterId(), false));
		return stripDetailBO;
	}

	public static IStripDetail convertFromBOtoDAO(StripDetailBO stripDetailBO){
		if(stripDetailBO == null){
			return null;
		}
		IStripDetail stripDetail = null;
		if(MainWindow.mode.equals("ELISA")){
			stripDetail = new StripDetailELISA();
		} else {
			stripDetail = new StripDetailCLIA();
		}
		
		stripDetail.setStripId(stripDetailBO.getStripId());
		stripDetail.setCalibrationType(stripDetailBO.getCalibrationType() != null ? stripDetailBO.getCalibrationType().getKey() : 0);
		stripDetail.setControl_id(stripDetailBO.getControlId());
		stripDetail.setParameterId(stripDetailBO.getParameterId());
		stripDetail.setPatientId(stripDetailBO.getPatientId());
		stripDetail.setResult_id(stripDetailBO.getResultId());
		stripDetail.setStatus(stripDetailBO.getStripStatus() != null ? stripDetailBO.getStripStatus().getKey() : 0);
		stripDetail.setTest_type(stripDetailBO.getTestType().getKey());
		stripDetail.setRlu(stripDetailBO.getRlu());
		return stripDetail;
	}
	
	public static StripDetailBO getStripDetail(Long stripId){
		IStripDetail stripDetail = commonQueries.getDetailForId(ReaderComponents.getTableNameForMode(IStripDetail.class), stripId);
		StripDetailBO stripDetailBO = convertFromDAOtoBO(stripDetail);
		return stripDetailBO;
	}
	
	public static List<StripDetailBO> getAllStripDetails(){
		List<IStripDetail> stripDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IStripDetail.class).getSimpleName());
		List<StripDetailBO> stripDetailBO = getStripDetails(stripDetail);
		return stripDetailBO;
	}
	
	public static List<StripDetailBO> getAllStripDetailsForWhere(String whereCond){
		List<IStripDetail> stripDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IStripDetail.class).getSimpleName()+ " " + whereCond);
		List<StripDetailBO> stripDetailBO = getStripDetails(stripDetail);
		return stripDetailBO;
	}
	
	public static List<StripDetailBO> getStripDetails(List<IStripDetail> stripDetails){
		List<StripDetailBO> stripDetailsBOList = new ArrayList<StripDetailBO>();
		for(IStripDetail strip : stripDetails){
			stripDetailsBOList.add(convertFromDAOtoBO(strip));
		}
		return stripDetailsBOList;
	}
	
	public static void deleteAllCalibrationRecords(){
		List<IStripDetail> stripDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IStripDetail.class).getSimpleName()+ " where test_type = 1");
		if(stripDetail != null && stripDetail.size() > 0){
			commonQueries.deleteDetails(stripDetail);
		}
	}
	
	public static void deleteAllRecords(){
		commonQueries.deleteDetails("from "+ ReaderComponents.getTableNameForMode(IStripDetail.class).getSimpleName());
	}
	
	public static void updateStrip(StripDetailBO stripDetailBO){
		IStripDetail stripDetail = convertFromBOtoDAO(stripDetailBO);
		commonQueries.update(stripDetail);
	}
	
	public static int getTotalCount(){
		Long count = commonQueries.getCountValue("select count(*) from "+ ReaderComponents.getTableNameForMode(IStripDetail.class).getSimpleName());
		return count == null ? 0 : count.intValue();
	}
	
}
