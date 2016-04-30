package org.ssr.dao.stripdetails;

public interface IStripDetail {

	public Long getStripId();
	public void setStripId(Long stripId);

	public int getTest_type();
	public void setTest_type(int testType);

	public Long getParameterId();
	public void setParameterId(Long parameterId);
	
	public String getPatientId();
	public void setPatientId(String patientId);

	public Long getControl_id();
	public void setControl_id(Long controlId);

	public int getCalibrationType();
	public void setCalibrationType(int calibrationType);

	public int getStatus();
	public void setStatus(int status);

	public Long getResult_id();
	public void setResult_id(Long resultId);

	public Double getRlu();
	public void setRlu(Double rlu);
	
}
