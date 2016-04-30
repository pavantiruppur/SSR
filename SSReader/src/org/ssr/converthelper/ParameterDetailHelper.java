package org.ssr.converthelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.StripDetailBO;
import org.ssr.components.ReaderComponents;
import org.ssr.dao.CommonQueries;
import org.ssr.dao.parameterdetails.IParameterDetails;
import org.ssr.dao.parameterdetails.ParameterDetailsCLIA;
import org.ssr.dao.parameterdetails.ParameterDetailsELISA;
import org.ssr.dao.standarddetails.IStandardDetails;
import org.ssr.dao.stripdetails.IStripDetail;
import org.ssr.enums.ActiveCheckEnum;
import org.ssr.enums.FilterWheelEnum;
import org.ssr.view.MainWindow;

public class ParameterDetailHelper {

	static CommonQueries<IParameterDetails> commonQueries = new CommonQueries<IParameterDetails>();
	static Long cliaNextValue = null, elisaNextValue = null;
	
	static{
		cliaNextValue = commonQueries.getMaxValue("select max(parameterId) from ParameterDetailsCLIA");
		cliaNextValue = cliaNextValue == null ? 1000l : cliaNextValue + 1;
		elisaNextValue = commonQueries.getMaxValue("select max(parameterId) from ParameterDetailsELISA");
		elisaNextValue = elisaNextValue == null ? 1000l : elisaNextValue + 1;
	}
	
	public static Long getNextIdNumber(){
		if(ReaderComponents.getMode().equals("ELISA")){
			return elisaNextValue;
		} else {
			return cliaNextValue;
		}
	}
	
	public static void incrementNextId(){
		if(ReaderComponents.getMode().equals("ELISA")){
			elisaNextValue++;
		} else {
			cliaNextValue++;
		}
	}
	
	public static ParameterDetailsBO convertFromDAOtoBO(IParameterDetails parameterDetail, boolean isStdReq){
		if(parameterDetail == null){
			return null;
		}
		ParameterDetailsBO parameterDetailBO = new ParameterDetailsBO();
		parameterDetailBO.setCalibrated(ActiveCheckEnum.getEnumByKey(parameterDetail.getIsCalibrate()).getValue());
		parameterDetailBO.setFormula(parameterDetail.getFormula());
		parameterDetailBO.setNoOfStd(parameterDetail.getNoOfStd());
		parameterDetailBO.setParameterId(parameterDetail.getParameterId());
		parameterDetailBO.setParameterName(parameterDetail.getParameterName());
		parameterDetailBO.setParameterUnit(parameterDetail.getParameterUnit());
		if(isStdReq){
			parameterDetailBO.setStdDetailList(StandardDetailHelper.getStandardDetailForParameterId(parameterDetail.getParameterId()));
		}
		parameterDetailBO.setFilterWheel(FilterWheelEnum.getEnumByKey(parameterDetail.getFilterWheel()));
		return parameterDetailBO;
	}

	public static IParameterDetails convertFromBOtoDAO(ParameterDetailsBO parameterDetailBO){
		if(parameterDetailBO == null){
			return null;
		}
		IParameterDetails parameterDetail = null;
		if(MainWindow.mode.equals("ELISA")){
			parameterDetail = new ParameterDetailsELISA();
			parameterDetail.setFilterWheel(parameterDetailBO.getFilterWheel().getKey());
		} else {
			parameterDetail = new ParameterDetailsCLIA();
		}
		
		parameterDetail.setFormula(parameterDetailBO.getFormula());
		parameterDetail.setIsCalibrate(ActiveCheckEnum.getEnumByValue(parameterDetailBO.isCalibrated()).getKey());
		parameterDetail.setNoOfStd(parameterDetailBO.getNoOfStd());
		parameterDetail.setParameterId(parameterDetailBO.getParameterId());
		parameterDetail.setParameterName(parameterDetailBO.getParameterName());
		parameterDetail.setParameterUnit(parameterDetailBO.getParameterUnit());
		
		return parameterDetail;
	}
	
	public static List<ParameterDetailsBO> getParameterDetails(List<IParameterDetails> parameterDetails, boolean isStdReq){
		List<ParameterDetailsBO> parameterDetailsBOList = new ArrayList<ParameterDetailsBO>();
		for(IParameterDetails parameter : parameterDetails){
			parameterDetailsBOList.add(convertFromDAOtoBO(parameter, isStdReq));
		}
		return parameterDetailsBOList;
	}
	
	public static List<ParameterDetailsBO> getAllParameterDetails(String keyWord, boolean loadOnlyCalibrated, boolean isStdReq){
		String query = "from ParameterDetails"+ReaderComponents.getMode()+" "+ (loadOnlyCalibrated?"":" where isCalibrate = 1 ");
		if(keyWord != null && !keyWord.trim().equals("")){
			query += (loadOnlyCalibrated?" where ":" and ")+"  (parameterId like '%" + keyWord +"%' or parameterName like '%" + keyWord +"%' or formula like '%" + keyWord +"%')";
		}
		List<IParameterDetails> parameterList = commonQueries.getAllDetails(query);
		return getParameterDetails(parameterList, isStdReq);
	}
	
	public static Map<Long, ParameterDetailsBO> convertListToMap(List<ParameterDetailsBO> parameterDetails){
		Map<Long, ParameterDetailsBO> parameterDetailsMap = new HashMap<Long, ParameterDetailsBO>();
		for(ParameterDetailsBO parameterDetail : parameterDetails){
			parameterDetailsMap.put(parameterDetail.getParameterId(), parameterDetail);
		}
		return parameterDetailsMap;
	}
	
	public static List<ParameterDetailsBO> getAllParameterDetailsOfStripDetails(){
		String query = "select p from ParameterDetails"+ReaderComponents.getMode()+" p,"+ ReaderComponents.getTableNameForMode(IStripDetail.class).getSimpleName() + " s where p.parameterId = s.parameterId ";
		List<IParameterDetails> parameterList = commonQueries.getAllDetails(query);
		return getParameterDetails(parameterList, false);
	}
	
	public static String[][] getPatientDataStrAry(List<ParameterDetailsBO> qcBOList){
		String data[][] = new String[qcBOList.size()][4];
		for(int i=0;i<qcBOList.size();i++){
			ParameterDetailsBO qcBO = qcBOList.get(i);
//			data[i][0] = String.valueOf(qcBO.getParameterId());
			data[i][0] = qcBO.getParameterName();
			data[i][1] = qcBO.getParameterUnit();
			data[i][2] = String.valueOf(qcBO.getNoOfStd());
			data[i][3] = String.valueOf(qcBO.getFormula());
		}
		return data;
	}
	
	public static ParameterDetailsBO getParameterDetail(Long parameterId, boolean isStdReq){
		if(parameterId == null){
			return null;
		}
		IParameterDetails parameterDetail = commonQueries.getDetailForId(ReaderComponents.getTableNameForMode(IParameterDetails.class), parameterId);
		return convertFromDAOtoBO(parameterDetail, isStdReq);
	}
	
	public static void addParameter(ParameterDetailsBO parameterDetailBO){
		CommonQueries<IParameterDetails> commonQueries = new CommonQueries<IParameterDetails>();
		IParameterDetails parameterDetail = convertFromBOtoDAO(parameterDetailBO);
		Long id = commonQueries.createEntry(parameterDetail);
		List<IStandardDetails> stdDetail = null;
		stdDetail = StandardDetailHelper.getStandardDetailsFromBO(parameterDetailBO.getStdDetailList(), id);
		StandardDetailHelper.addStdDetails(stdDetail, parameterDetail);
	}
	
	public static void updateParameter(ParameterDetailsBO parameterDetailBO){
		IParameterDetails parameterDetail = convertFromBOtoDAO(parameterDetailBO);
		commonQueries.update(parameterDetail);
	}
}
