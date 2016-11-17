package org.ssr.bo;

import java.util.List;

import org.ssr.enums.FilterWheelEnum;
import org.ssr.enums.FormulaEnum;

public class ParameterDetailsBO {
	Long parameterId;
	String parameterName;
	String parameterUnit;
	int noOfStd;
	String formula;
	FormulaEnum formulaEnum;
	String formulaJson;
	FilterWheelEnum filterWheel;
	List<StandardDetailsBO> stdDetailList;
	boolean isCalibrated;
	
	public Long getParameterId() {
		return parameterId;
	}
	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterUnit() {
		return parameterUnit;
	}
	public void setParameterUnit(String parameterUnit) {
		this.parameterUnit = parameterUnit;
	}
	public int getNoOfStd() {
		return noOfStd;
	}
	public void setNoOfStd(int noOfStd) {
		this.noOfStd = noOfStd;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public FilterWheelEnum getFilterWheel() {
		return filterWheel;
	}
	public void setFilterWheel(FilterWheelEnum filterWheel) {
		this.filterWheel = filterWheel;
	}
	public List<StandardDetailsBO> getStdDetailList() {
		return stdDetailList;
	}
	public void setStdDetailList(List<StandardDetailsBO> stdDetailList) {
		this.stdDetailList = stdDetailList;
	}
	public boolean isCalibrated() {
		return isCalibrated;
	}
	public void setCalibrated(boolean isCalibrated) {
		this.isCalibrated = isCalibrated;
	}
	public FormulaEnum getFormulaEnum() {
		return formulaEnum;
	}
	public void setFormulaEnum(FormulaEnum formulaEnum) {
		this.formulaEnum = formulaEnum;
	}
	public String getFormulaJson() {
		return formulaJson;
	}
	public void setFormulaJson(String formulaJson) {
		this.formulaJson = formulaJson;
	}
}
