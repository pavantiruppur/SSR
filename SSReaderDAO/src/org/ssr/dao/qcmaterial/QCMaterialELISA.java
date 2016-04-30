package org.ssr.dao.qcmaterial;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "qcmaterial_CLIA")
public class QCMaterialELISA implements IQCMaterial, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue	
    @Column(name="qc_id")
    private Long qcId;
	
	@Column(name="barcode")
    private String barcode;

	@Column(name="analyte")
	private String analyte;
	
	@Column(name="qclot")
	private long qclot;

    @Column(name="reference")
    private Double reference;
	
	@Column(name="refPlusOrMinus")
    private Double refPlusOrMinus;

	@Column(name="is_deleted")
	private int isDeleted;

	@Column(name="creation_date")
	private Date creationDate;

	@Column(name="last_modified_date")
	private Date lastModifiedDate;

	public Long getQcId() {
		return qcId;
	}

	public void setQcId(Long qcId) {
		this.qcId = qcId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getAnalyte() {
		return analyte;
	}

	public void setAnalyte(String analyte) {
		this.analyte = analyte;
	}

	public long getQclot() {
		return qclot;
	}

	public void setQclot(long qclot) {
		this.qclot = qclot;
	}

	public Double getReference() {
		return reference;
	}

	public void setReference(Double reference) {
		this.reference = reference;
	}

	public Double getRefPlusOrMinus() {
		return refPlusOrMinus;
	}

	public void setRefPlusOrMinus(Double refPlusOrMinus) {
		this.refPlusOrMinus = refPlusOrMinus;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
