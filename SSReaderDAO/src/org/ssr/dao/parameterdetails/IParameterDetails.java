package org.ssr.dao.parameterdetails;

import java.util.Date;

public interface IParameterDetails {
	
	public Long getParameterId();
	public void setParameterId(Long parameterId);

	public String getParameterName();
	public void setParameterName(String parameterName);

	public String getParameterUnit();
	public void setParameterUnit(String parameterUnit);

	public int getNoOfStd();
	public void setNoOfStd(int noOfStd);

	public String getFormula();
	public void setFormula(String formula);

	public int getIsCalibrate();
	public void setIsCalibrate(int isCalibrate);

	public int getIsActive();
	public void setIsActive(int isActive);
	
	public int getFilterWheel();
	public void setFilterWheel(int filterWheel);

	public Date getCreationDate();
	public void setCreationDate(Date creationDate);

	public Date getLastModifiedDate();
	public void setLastModifiedDate(Date lastModifiedDate);
}
