package org.ssr.formula;

import javax.swing.JPanel;

public interface FormulaBase {

	public String getJSON();
	
	public JPanel getJPanel();
	
	public void clean();
	
	public void setValueByJSON(String json);
	
	public FormulaSectionBase getFormulaSectionByJSON(String json);
}
