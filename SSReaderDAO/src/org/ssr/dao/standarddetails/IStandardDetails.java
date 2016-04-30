package org.ssr.dao.standarddetails;


public interface IStandardDetails {

	public Long getParameterId();
	public void setParameterId(Long parameterId);

	public Long getStdId();
	public void setStdId(Long stdId);

	public Double getStdValue();
	public void setStdValue(Double stdValue);

	public Double getRluValue();
	public void setRluValue(Double rluValue);
}
