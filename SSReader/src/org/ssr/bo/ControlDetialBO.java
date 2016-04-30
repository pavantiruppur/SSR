package org.ssr.bo;

public class ControlDetialBO {
	String barcode, analyte;
	int qcId,qcLot;
	double reference, refPlusOrMinus;
	
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
	public int getQcId() {
		return qcId;
	}
	public void setQcId(int qcId) {
		this.qcId = qcId;
	}
	public int getQcLot() {
		return qcLot;
	}
	public void setQcLot(int qcLot) {
		this.qcLot = qcLot;
	}
	public double getReference() {
		return reference;
	}
	public void setReference(double reference) {
		this.reference = reference;
	}
	public double getRefPlusOrMinus() {
		return refPlusOrMinus;
	}
	public void setRefPlusOrMinus(double refPlusOrMinus) {
		this.refPlusOrMinus = refPlusOrMinus;
	}
}
