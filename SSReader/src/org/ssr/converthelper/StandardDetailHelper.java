package org.ssr.converthelper;

import java.util.ArrayList;
import java.util.List;

import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.StandardDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.dao.CommonQueries;
import org.ssr.dao.parameterdetails.IParameterDetails;
import org.ssr.dao.standarddetails.IStandardDetails;
import org.ssr.dao.standarddetails.StandardDetailsCLIA;
import org.ssr.dao.standarddetails.StandardDetailsELISA;
import org.ssr.view.MainWindow;

public class StandardDetailHelper {
	
	static CommonQueries<IStandardDetails> commonQueries = new CommonQueries<IStandardDetails>();
	
	public static StandardDetailsBO convertFromDAOtoBO(IStandardDetails stripDetail){
		StandardDetailsBO stripDetailBO = new StandardDetailsBO();
		stripDetailBO.setRluValue(stripDetail.getRluValue());
		stripDetailBO.setStdId(stripDetail.getStdId());
		stripDetailBO.setStdValue(stripDetail.getStdValue());
		return stripDetailBO;
	}

	public static IStandardDetails convertFromBOtoDAO(StandardDetailsBO stdDetailBO, Long parameterId){
		IStandardDetails c = null;
		if(MainWindow.mode.equals("ELISA")){
			c = new StandardDetailsELISA();
		} else {
			c = new StandardDetailsCLIA();
		}
		c.setParameterId(new Long(parameterId));
		c.setStdId(stdDetailBO.getStdId());
		c.setRluValue(stdDetailBO.getRluValue() == null ? 0 : stdDetailBO.getRluValue());
		c.setStdValue(stdDetailBO.getStdValue() == null ? 0 : stdDetailBO.getStdValue());
		
		return c;
	}
	
	public static List<StandardDetailsBO> getStandardDetailsFromDAO(List<IStandardDetails> standardDetails){
		List<StandardDetailsBO> standardList = new ArrayList<StandardDetailsBO>();
		for(IStandardDetails stdDetail : standardDetails){
			standardList.add(convertFromDAOtoBO(stdDetail));
		}
		return standardList;
	}
	
	public static List<IStandardDetails> getStandardDetailsFromBO(List<StandardDetailsBO> standardDetails, Long parameterId){
		List<IStandardDetails> standardList = new ArrayList<IStandardDetails>();
		for(StandardDetailsBO stdDetail : standardDetails){
			standardList.add(convertFromBOtoDAO(stdDetail, parameterId));
		}
		return standardList;
	}
	
	public static void addStdDetails(List<IStandardDetails> stdDetailsList, IParameterDetails parameterDetail){
		for(IStandardDetails stdDetail : stdDetailsList){
			commonQueries.createEntry(stdDetail);
		}
	}
	
	public static List<StandardDetailsBO> getStandardDetailForParameterId(Long parameterId){
		List<IStandardDetails> parameterDetail = commonQueries.getAllDetails("from "+ReaderComponents.getTableNameForMode(IStandardDetails.class).getSimpleName() +" where parameterId = "+ parameterId);
		return getStandardDetailsFromDAO(parameterDetail);
	}
	
	public static void updateStdDetails(List<StandardDetailsBO> stdDetailsList, long parameterId){
		for(StandardDetailsBO stdDetail : stdDetailsList){
//			IStandardDetails std = convertFromBOtoDAO(stdDetail, parameterId);
			commonQueries.updateStdRlu("update "+ReaderComponents.getTableNameForMode(IStandardDetails.class).getSimpleName() +" set rlu_value = "+ stdDetail.getRluValue() +" where parameterId = "+ parameterId +" and stdId = "+ stdDetail.getStdId());
		}
	}
}
