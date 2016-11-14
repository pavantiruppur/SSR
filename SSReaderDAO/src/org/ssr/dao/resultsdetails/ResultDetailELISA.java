package org.ssr.dao.resultsdetails;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "result_detail_ELISA")
public class ResultDetailELISA implements IResultDetail, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue	
    @Column(name="result_id")
    private Long resultId;

    @Column(name="parameter_id")
    private Long parameterId;
	
	@Column(name="patient_id")
	private String patientId;

    @Column(name="rlu_value")
    private Double rluValue;

    @Column(name="conc")
    private Double conc;

    @Column(name="sample_type")
    private int sample_type;

	@Column(name="creation_date")
	private Date creationDate;

	@Column(name="last_modified_date")
	private Date lastModifiedDate;

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getParameterId() {
		return parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public Double getRluValue() {
		return rluValue;
	}

	public void setRluValue(Double rluValue) {
		this.rluValue = rluValue;
	}

	public Double getConc() {
		return conc;
	}

	public void setConc(Double conc) {
		this.conc = conc;
	}

	public int getSample_type() {
		return sample_type;
	}

	public void setSample_type(int sampleType) {
		sample_type = sampleType;
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
