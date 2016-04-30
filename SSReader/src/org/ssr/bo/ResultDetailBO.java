package org.ssr.bo;

import java.text.DecimalFormat;

import org.ssr.components.ReaderComponents;
import org.ssr.enums.TestTypeEnum;

public class ResultDetailBO extends ParameterDetailsBO {
	DecimalFormat dc_rlu = new DecimalFormat("#");
	DecimalFormat dc_conc = new DecimalFormat("#.000");
	
	long resultId;
	String patientId;
	double rlu;
	double conc;
	TestTypeEnum sampleType;
	
	public long getResultId() {
		return resultId;
	}
	public void setResultId(long resultId) {
		this.resultId = resultId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public double getRlu() {
		return rlu;
	}
	public String getRluAsString() {
		return String.valueOf(ReaderComponents.getMode().equals("ELISA") ? rlu:(dc_rlu.format(rlu)));
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
	public double getConc3Digit(){
		return Double.parseDouble(dc_conc.format(conc));
	}
	public TestTypeEnum getSampleType() {
		return sampleType;
	}
	public void setSampleType(TestTypeEnum sampleType) {
		this.sampleType = sampleType;
	}
}
