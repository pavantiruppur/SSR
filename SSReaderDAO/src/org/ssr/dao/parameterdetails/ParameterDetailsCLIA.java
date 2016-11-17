package org.ssr.dao.parameterdetails;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parameter_details_CLIA")
public class ParameterDetailsCLIA implements IParameterDetails,Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue	
    @Column(name="parameter_id")
    private Long parameterId;
	
	@Column(name="parameter_name")
    private String parameterName;

	@Column(name="parameter_unit")
	private String parameterUnit;
	
	@Column(name="no_of_std")
	private int noOfStd;

	@Column(name="formula")
	private String formula;

	@Column(name="is_calibrate")
	private int isCalibrate;

	@Column(name="is_active")
	private int isActive;
	
	@Column(name="filterwheel")
	private int filterWheel;

	@Column(name="formula_id")
	private int formulaId;

	@Column(name="formula_json")
	private String formulaJson;

	@Column(name="creation_date")
	private Date creationDate;

	@Column(name="last_modified_date")
	private Date lastModifiedDate;

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

	public int getIsCalibrate() {
		return isCalibrate;
	}

	public void setIsCalibrate(int isCalibrate) {
		this.isCalibrate = isCalibrate;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
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

	@Override
	public int getFilterWheel() {
		return filterWheel;
	}

	@Override
	public void setFilterWheel(int filterWheel) {
		this.filterWheel = filterWheel;
	}

	public int getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(int formulaId) {
		this.formulaId = formulaId;
	}

	public String getFormulaJson() {
		return formulaJson;
	}

	public void setFormulaJson(String formulaJson) {
		this.formulaJson = formulaJson;
	}
}
