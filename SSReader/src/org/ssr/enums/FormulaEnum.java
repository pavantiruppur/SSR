package org.ssr.enums;

import org.ssr.formula.Absorbance;
import org.ssr.formula.CubicSpline;
import org.ssr.formula.FormulaBase;
import org.ssr.formula.LinearRegression;
import org.ssr.formula.PointToPoint;
import org.ssr.formula.Polynomial;

public enum FormulaEnum {

	POINT_TO_POINT(1, "POINT TO POINT", new PointToPoint()),
	LINEAR_REGRESSION(2, "LINEAR REGRESSION", new LinearRegression()),
	CUBIC_SPLINE(3, "CUBIC SPLINE", new CubicSpline()),
	POLYNOMIAL(4, "POLYNOMIAL", new Polynomial()),
	ABSORBANCE(2, "ABSORBANCE", new Absorbance());
	
	private int id;
	private String text;
	private FormulaBase formula;
	
	private FormulaEnum(int id, String text, FormulaBase formula) {
		this.id = id;
		this.text = text;
		this.formula = formula;
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public FormulaBase getFormula() {
		return formula;
	}
	
	public static FormulaEnum getEnumByKey(int id){
		if(id==0){
			return null;
		}
		for(FormulaEnum formula : FormulaEnum.values()){
			if(formula.id == id){
				return formula;
			}
		}
		return null;
	}
}
