package org.ssr.dao.qcmaterial;

import java.util.Date;

public interface IQCMaterial {

	public Long getQcId();
	public void setQcId(Long qcId);

	public String getBarcode();
	public void setBarcode(String barcode);
	
	public Long getParameterId();
	public void setParameterId(Long parameterId);

	public String getAnalyte();
	public void setAnalyte(String analyte);

	public long getQclot();
	public void setQclot(long qclot);

	public Double getReference();
	public void setReference(Double reference);
	
	public Double getRefPlusOrMinus();
	public void setRefPlusOrMinus(Double refPlusOrMinus);

	public int getIsDeleted();
	public void setIsDeleted(int isDeleted);
	
	public Date getCreationDate();
	public void setCreationDate(Date creationDate);

	public Date getLastModifiedDate();
	public void setLastModifiedDate(Date lastModifiedDate) ;
	
}
