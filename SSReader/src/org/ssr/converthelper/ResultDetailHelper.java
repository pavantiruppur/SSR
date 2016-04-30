package org.ssr.converthelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.ResultDetailBO;
import org.ssr.components.ReaderComponents;
import org.ssr.dao.CommonQueries;
import org.ssr.dao.resultsdetails.IResultDetail;
import org.ssr.dao.resultsdetails.ResultDetailCLIA;
import org.ssr.dao.resultsdetails.ResultDetailELISA;
import org.ssr.enums.TestTypeEnum;
import org.ssr.view.MainWindow;

public class ResultDetailHelper {
	
	static CommonQueries<IResultDetail> commonQueries = new CommonQueries<IResultDetail>();
	
	public static void addResult(ResultDetailBO ResultDetailBO){
		IResultDetail stripDetail = convertFromBOtoDAO(ResultDetailBO);
		commonQueries.createEntry(stripDetail);
	}

	public static ResultDetailBO convertFromDAOtoBO(IResultDetail resultDetail){
		if(resultDetail == null){
			return null;
		}
		ResultDetailBO resultDetailBO = new ResultDetailBO();
		resultDetailBO.setConc(resultDetail.getConc());
		resultDetailBO.setParameterId(resultDetail.getParameterId());
		resultDetailBO.setPatientId(resultDetail.getPatientId());
		resultDetailBO.setResultId(resultDetail.getResultId());
		resultDetailBO.setRlu(resultDetail.getRluValue());
		resultDetailBO.setSampleType(TestTypeEnum.getEnumByKey(resultDetail.getSample_type()));
		
		return resultDetailBO;
	}

	public static IResultDetail convertFromBOtoDAO(ResultDetailBO resultDetailBO){
		if(resultDetailBO == null){
			return null;
		}
		IResultDetail resultDetail = null;
		if(MainWindow.mode.equals("ELISA")){
			resultDetail = new ResultDetailELISA();
		} else {
			resultDetail = new ResultDetailCLIA();
		}
		resultDetail.setConc(resultDetailBO.getConc());
		resultDetail.setParameterId(resultDetailBO.getParameterId());
		resultDetail.setPatientId(resultDetailBO.getPatientId());
		resultDetail.setRluValue(resultDetailBO.getRlu());
		resultDetail.setSample_type(resultDetailBO.getSampleType().getKey());
		return resultDetail;
	}
	
	public static ResultDetailBO getResultDetail(Long resultId){
		IResultDetail resultDetail = commonQueries.getDetailForId(ReaderComponents.getTableNameForMode(IResultDetail.class), resultId);
		ResultDetailBO resultDetailBO = convertFromDAOtoBO(resultDetail);
		return resultDetailBO;
	}
	
	public static List<ResultDetailBO> getAllResultDetails(){
		List<IResultDetail> resultDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IResultDetail.class).getSimpleName());
		List<ResultDetailBO> resultDetailBO = getResultDetails(resultDetail);
		return resultDetailBO;
	}
	
	public static List<ResultDetailBO> getAllResultDetailsForWhere(String whereCond){
		List<IResultDetail> resultDetail = commonQueries.getAllDetails("from "+ ReaderComponents.getTableNameForMode(IResultDetail.class).getSimpleName()+ " " + whereCond);
		List<ResultDetailBO> resultDetailBO = getResultDetails(resultDetail);
		return resultDetailBO;
	}
	
	public static List<ResultDetailBO> getResultDetails(List<IResultDetail> resultDetails){
		List<ResultDetailBO> resultDetailsBOList = new ArrayList<ResultDetailBO>();
		Map<Long, ParameterDetailsBO> parameterDetailsMap = ParameterDetailHelper.convertListToMap(ParameterDetailHelper.getAllParameterDetails(null, true, false));
		for(IResultDetail strip : resultDetails){
			resultDetailsBOList.add(convertFromDAOtoBO(strip, parameterDetailsMap.get(strip.getParameterId())));
		}
		return resultDetailsBOList;
	}
	
	private static ResultDetailBO convertFromDAOtoBO(IResultDetail strip,
			ParameterDetailsBO parameterDetailsBO) {
		ResultDetailBO resultDetailBO = convertFromDAOtoBO(strip);
		if(resultDetailBO == null){
			return null;
		}
		resultDetailBO.setParameterName(parameterDetailsBO.getParameterName());
		return resultDetailBO;
	}

	public static void updateStrip(ResultDetailBO ResultDetailBO){
		IResultDetail resultDetail = convertFromBOtoDAO(ResultDetailBO);
		commonQueries.update(resultDetail);
	}
	
	public static List<ResultDetailBO> getAllResultDetails(String keyWord){
		String query = "from "+ ReaderComponents.getTableNameForMode(IResultDetail.class).getSimpleName();
		if(keyWord != null && !keyWord.trim().equals("")){
			query = query + " where patientId like '%" + keyWord +"%' or parameterId like '%" + keyWord +"%'";
		}
		query = query  + " order by resultId desc";
		List<IResultDetail> resultList = commonQueries.getAllDetails(query);
		return getResultDetails(resultList);
	}
	
}
