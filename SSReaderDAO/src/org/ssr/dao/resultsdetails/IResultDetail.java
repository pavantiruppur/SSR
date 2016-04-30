package org.ssr.dao.resultsdetails;

import java.util.Date;

public interface IResultDetail {

	public Long getResultId();
	public void setResultId(Long resultId);

	public Long getParameterId();
	public void setParameterId(Long parameterId);

	public String getPatientId();
	public void setPatientId(String patientId);

	public Double getRluValue();
	public void setRluValue(Double rluValue);

	public Double getConc();
	public void setConc(Double conc);

	public int getSample_type();
	public void setSample_type(int sampleType);

	public Date getCreationDate();
	public void setCreationDate(Date creationDate);

	public Date getLastModifiedDate();
	public void setLastModifiedDate(Date lastModifiedDate);
	
}
