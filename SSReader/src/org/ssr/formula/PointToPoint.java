package org.ssr.formula;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.ssr.components.ReaderComponents;
import org.ssr.enums.FormulaSectionEnum;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class PointToPoint implements FormulaBase {
	
	private static final int TEXTFIELD_WIDTH = 180;
	public static final int LABEL_WIDTH = 100;

	private JPanel panel;
	private JComboBox<FormulaSectionEnum> sectionCombo;
	private FormulaSectionEnum[] sections = new FormulaSectionEnum[]{FormulaSectionEnum.EQUATION_1, 
			FormulaSectionEnum.EQUATION_2,
			FormulaSectionEnum.EQUATION_3,
			FormulaSectionEnum.EQUATION_4,
			FormulaSectionEnum.EQUATION_5,
			FormulaSectionEnum.EQUATION_6,
			FormulaSectionEnum.EQUATION_7};
	
	public PointToPoint() {
		panel = ReaderComponents.getContentPanel();
		ReaderComponents.setFlowLayoutPadding(FlowLayout.LEFT,panel, 5,1);
		panel.setPreferredSize(new Dimension(290,(30*(int)Math.ceil((double)2))+10));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		ReaderComponents.getLabel("Section ",panel,LABEL_WIDTH);
		sectionCombo = ReaderComponents.getComboBox(sections ,panel, TEXTFIELD_WIDTH + 100);
		sectionCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {}
		});
		
	}
	
	@Override
	public String getJSON() {
		String json = "{\"sec\":\""+ ((FormulaSectionEnum)sectionCombo.getSelectedItem()).getId() +"\"}";
		return json;
	}

	@Override
	public JPanel getJPanel() {
		return panel;
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueByJSON(String json) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public FormulaSectionBase getFormulaSectionByJSON(String json) {
		Gson gson = new Gson();
		JsonElement element = gson.fromJson (json, JsonElement.class);
		FormulaSectionEnum sectionEnum = FormulaSectionEnum.getEnumByKey(Integer.valueOf(element.getAsJsonObject().get("sec").getAsString()));
		return sectionEnum.getFormulaSection();
	}
}
