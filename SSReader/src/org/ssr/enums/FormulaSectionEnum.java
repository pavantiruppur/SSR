package org.ssr.enums;

import org.ssr.formula.FormulaSectionBase;
import org.ssr.formula.FormulaSectionBase.Section1;

public enum FormulaSectionEnum {

	EQUATION_1(1, "Sec-1: Y=CONC; X=LN(1000*ABSORBANCE)", new Section1()),
	EQUATION_2(2, "Sec-2: Y=LN(CONC); X=ABSORBANCE", new Section1()),
	EQUATION_3(3, "Sec-3: Y=LN(CONC); X=LN(1000*ABSORBANCE)", new Section1()),
	EQUATION_4(4, "Sec-4: Y=LOG(CONC); X=LOGIT(ABSORBANCE)", new Section1()),
	EQUATION_5(5, "Sec-5: Y=CONC; X=LOG(1000*ABSORBANCE)", new Section1()),
	EQUATION_6(6, "Sec-6: Y=LOG(CONC); X=ABSORBANCE", new Section1()),
	EQUATION_7(7, "Sec-7: Y=LOG(CONC); X=LOG(1000*ABSORBANCE)", new Section1()),
	EQUATION_8(8, "Sec-8: Y=CONC; X=ABSORBANCE", new Section1());
	
	private int id;
	private String text;
	private FormulaSectionBase formulaSection;
	
	private FormulaSectionEnum(int id, String text, FormulaSectionBase formulaSection) {
		this.id = id;
		this.text = text;
		this.formulaSection = formulaSection;
	}
	
	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public FormulaSectionBase getFormulaSection() {
		return formulaSection;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public static FormulaSectionEnum getEnumByKey(int id){
		if(id==0){
			return null;
		}
		for(FormulaSectionEnum formula : FormulaSectionEnum.values()){
			if(formula.id == id){
				return formula;
			}
		}
		return null;
	}
}
