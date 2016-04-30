package org.ssr.dao.stripdetails;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "strip_detail_ELISA")
public class StripDetailELISA implements IStripDetail, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="strip_id")
    private Long stripId;
	
	@Column(name="test_type")
    private int test_type;

	@Column(name="parameter_id")
	private Long parameterId;
	
	@Column(name="patient_id")
	private String patientId;

    @Column(name="control_id")
    private Long control_id;
	
	@Column(name="calibration_type")
    private int calibrationType;

	@Column(name="status")
	private int status;

	@Column(name="result_id")
	private Long result_id;
	
	@Column(name="rlu_value")
	private Double rlu;

	public Long getStripId() {
		return stripId;
	}

	public void setStripId(Long stripId) {
		this.stripId = stripId;
	}

	public int getTest_type() {
		return test_type;
	}

	public void setTest_type(int testType) {
		test_type = testType;
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

	public Long getControl_id() {
		return control_id;
	}

	public void setControl_id(Long controlId) {
		control_id = controlId;
	}

	public int getCalibrationType() {
		return calibrationType;
	}

	public void setCalibrationType(int calibrationType) {
		this.calibrationType = calibrationType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getResult_id() {
		return result_id;
	}

	public void setResult_id(Long resultId) {
		result_id = resultId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getRlu() {
		return rlu;
	}

	public void setRlu(Double rlu) {
		this.rlu = rlu;
	}
}
