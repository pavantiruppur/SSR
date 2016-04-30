package org.ssr.dao.standarddetails;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "standard_details_CLIA")
public class StandardDetailsCLIA implements IStandardDetails, Serializable {

	private static final long serialVersionUID = 1L;
	
    @Column(name="parameter_id")
    private Long parameterId;
	
	@Id
	@Column(name="std_id")
    private Long stdId;

	@Column(name="std_value")
	private Double stdValue;
	
	@Column(name="rlu_value")
	private Double rluValue;

	public Long getParameterId() {
		return parameterId;
	}


	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}


	public Long getStdId() {
		return stdId;
	}


	public void setStdId(Long stdId) {
		this.stdId = stdId;
	}


	public Double getStdValue() {
		return stdValue;
	}


	public void setStdValue(Double stdValue) {
		this.stdValue = stdValue;
	}


	public Double getRluValue() {
		return rluValue;
	}


	public void setRluValue(Double rluValue) {
		this.rluValue = rluValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
