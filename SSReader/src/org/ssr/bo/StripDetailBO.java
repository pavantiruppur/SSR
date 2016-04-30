package org.ssr.bo;

import org.ssr.enums.CalibrationTypeEnum;
import org.ssr.enums.StripStatusEnum;
import org.ssr.enums.TestTypeEnum;

public class StripDetailBO {
	Long stripId;
	TestTypeEnum testType;
	Long parameterId;
	String parameterName;
	String patientId;
	String patientName;
	Long controlId;
	CalibrationTypeEnum calibrationType;
	StripStatusEnum stripStatus;
	Long resultId;
	double rlu;
	double conc;
	ParameterDetailsBO parameterDetail;
	boolean selfObject;
	
	public StripDetailBO(boolean selfObject) {
		this.selfObject = selfObject;
	}
	
	public StripDetailBO(Long stripId, TestTypeEnum testType, boolean selfObject) {
		this.stripId = stripId;
		this.testType = testType;
		this.parameterName = "";
		this.calibrationType = CalibrationTypeEnum.MASTER;
		this.selfObject = true;
	}
	
	public Long getStripId() {
		return stripId;
	}
	public void setStripId(Long stripId) {
		this.stripId = stripId;
	}
	public TestTypeEnum getTestType() {
		return testType;
	}
	public void setTestType(TestTypeEnum testType) {
		this.testType = testType;
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
	public Long getControlId() {
		return controlId;
	}
	public void setControlId(Long controlId) {
		this.controlId = controlId;
	}
	public CalibrationTypeEnum getCalibrationType() {
		return calibrationType;
	}
	public void setCalibrationType(CalibrationTypeEnum calibrationType) {
		this.calibrationType = calibrationType;
	}
	public StripStatusEnum getStripStatus() {
		return stripStatus;
	}
	public void setStripStatus(StripStatusEnum stripStatus) {
		this.stripStatus = stripStatus;
	}
	public Long getResultId() {
		return resultId;
	}
	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public double getRlu() {
		return rlu;
	}
	public void setRlu(double rlu) {
		this.rlu = rlu;
	}
	public double getConc() {
		return conc;
	}
	public void setConc(double conc) {
		this.conc = conc;
	}
	public ParameterDetailsBO getParameterDetail() {
		return parameterDetail;
	}
	public void setParameterDetail(ParameterDetailsBO parameterDetail) {
		this.parameterDetail = parameterDetail;
	}
	public boolean isSelfObject() {
		return selfObject;
	}
	public void setSelfObject(boolean selfObject) {
		this.selfObject = selfObject;
	}
}
