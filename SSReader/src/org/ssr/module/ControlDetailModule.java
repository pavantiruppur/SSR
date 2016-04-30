package org.ssr.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.ssr.bo.ControlDetialBO;
import org.ssr.bo.ParameterDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ControlDetailHelper;
import org.ssr.view.MainWindow;
import org.ssr.view.ParameterModalView;

public class ControlDetailModule implements IDataDetailPopulate {
	
	private static final int TEXTFIELD_WIDTH = 180;
	public static final int LABEL_WIDTH = 100;
	
	JPanel qcPanel = null;
	JPanel tablePanel = null;
	JTextField searchTxt = null;
	JTextField barcodeTxt = null, qcLotTxt = null, referenceTxt = null,  refPlusOrMinulTxt = null;
	JTextField analyteTxt = null;
	JButton addQCbt = null, clearBt = null;
	public JTable qcTable = null;
	JScrollPane scrollPane = null;
	ParameterDetailsBO selectedParameterDetailBO = new ParameterDetailsBO();
	
	List<ControlDetailModule> controlDetailList = null;

	static ControlDetailModule instance = null;
	
	public static ControlDetailModule getInstance(){
		if(instance == null){
			instance = new ControlDetailModule();
		}
		return instance;
	}
	
	@Override
	public String[] getColumnName() {
		String[] col = {"Name","Unit","NoOfStd","Formula"};
		return col;
	}

	@Override
	public String[][] getDataStrAry(String query) {
		String[][] col = {{"Name","Unit","NoOfStd","Formula"}};
		return col;
	}

	@Override
	public String getSearchString() {
		return "Search using Id, Name and Formula";
	}

	@Override
	public JPanel getRightPanel(JPanel rigthPanel, JTable patientDetailTable) {
		return null;
	}
	
	@Override
	public JPanel getAddPanel(JPanel addQCPanel) {
		
		JLabel header = ReaderComponents.getLabel("Control Details",addQCPanel,LABEL_WIDTH + 200);
		header.setFont(new Font(Font.DIALOG,1,16));
		
		ReaderComponents.getLabel("Name",addQCPanel,LABEL_WIDTH);
		barcodeTxt = ReaderComponents.getTextField(addQCPanel,TEXTFIELD_WIDTH);
		
		ReaderComponents.getLabel("Analyte",addQCPanel,LABEL_WIDTH);
		analyteTxt = ReaderComponents.getTextField(addQCPanel,TEXTFIELD_WIDTH*75/100);
		analyteTxt.setEnabled(false);
		
		JButton bt = ReaderComponents.getButton("prmBtn", addQCPanel, 20, 20);
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ParameterModalView.getInstance().showParameterModal(false, analyteTxt, selectedParameterDetailBO);
			}
		});
		
		ReaderComponents.getLabel("QCLot",addQCPanel,LABEL_WIDTH);
		qcLotTxt = ReaderComponents.getTextField(addQCPanel,TEXTFIELD_WIDTH);

		ReaderComponents.getLabel("Reference",addQCPanel,LABEL_WIDTH);
		referenceTxt = ReaderComponents.getTextField(addQCPanel,TEXTFIELD_WIDTH);

		ReaderComponents.getLabel("Reference +/-",addQCPanel,LABEL_WIDTH);
		refPlusOrMinulTxt = ReaderComponents.getTextField(addQCPanel,TEXTFIELD_WIDTH*50/100);
		
		JPanel buttonPanel = ReaderComponents.getContentPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,3));
		buttonPanel.setPreferredSize(new Dimension(280,40));
		
		addQCbt = ReaderComponents.getButton("Add", buttonPanel);
		addQCbt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				insertQCMaterial();
				MainWindow.frame.repaint();
			}
		});
		
		clearBt = ReaderComponents.getButton("Clear", buttonPanel);
		clearBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearAllTextFields();
			}
		});
		addQCPanel.add(buttonPanel);
		return addQCPanel;
	}
	
	@Override
	public String getDefaultBtView() {
		return "default";
	}
	
	public void clearAllTextFields(){
		barcodeTxt.setText("");
		qcLotTxt.setText("");
		referenceTxt.setText("");
		refPlusOrMinulTxt.setText("");
		analyteTxt.setText("");
		
		barcodeTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		qcLotTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		referenceTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		refPlusOrMinulTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}

	@Override
	public String[][] getDataStrAry(List dataList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTableRowSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void insertQCMaterial(){
		ControlDetialBO qcBO = new ControlDetialBO();
		qcBO.setBarcode(barcodeTxt.getText());
		qcBO.setAnalyte(analyteTxt.getText());
		qcBO.setQcLot(Integer.parseInt(qcLotTxt.getText()));
		qcBO.setReference(Double.parseDouble(referenceTxt.getText()));
		qcBO.setRefPlusOrMinus(Double.parseDouble(refPlusOrMinulTxt.getText()));
		ControlDetailHelper.addStrip(qcBO);
//		if(status > 0){
//			JOptionPane.showMessageDialog(null, "QC Data Added Successfully");
//		}
	}
}
