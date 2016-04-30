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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.ssr.bo.PatientDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.PatientDetailHelper;
import org.ssr.view.MainWindow;

import sun.applet.Main;

public class PatientDetailModule implements IDataDetailPopulate {


	private static final int TEXTFIELD_WIDTH = 180;
	public static final int LABEL_WIDTH = 100;
	JTextField patIdTxt = null, patFirstNameTxt = null, patLastNameTxt = null,  patAgeTxt = null, phoneNoTxt = null,
		emailIdTxt = null;
	JComboBox patSexCmbo = null;
	JButton addPatientbt = null, clearBt = null;
	
	List<PatientDetailsBO> patientDetailList = null;

	static PatientDetailModule instance = null;
	
	public static PatientDetailModule getInstance(){
		if(instance == null){
			instance = new PatientDetailModule();
		}
		return instance;
	}
	
	@Override
	public String[] getColumnName() {
		String[] col = {"<html><center>Patient<br>ID","<html><center>First<br>Name","<html><center>Last<br>Name","Age","Gender"};
		return col;
	}

	@Override
	public String[][] getDataStrAry(String searchString) {
		patientDetailList = PatientDetailHelper.getAllPatientDetails(searchString);
		String[][] data = getDataStrAry(patientDetailList);
		return data;
	}

	@Override
	public String getSearchString() {
		return "Search using Id, Name and Place";
	}
	
	@Override
	public JPanel getRightPanel(JPanel rightPanel, JTable detailTable) {
		int row = detailTable.getSelectedRow();
		PatientDetailsBO patientDetail = null;
		if(patientDetailList != null && patientDetailList.size() > 0){
			patientDetail = patientDetailList.get(row);
		}
		if(patientDetail == null){
			return null;
		}
		
		JLabel header = ReaderComponents.getLabel("Patient Details",rightPanel,LABEL_WIDTH + 200);
		header.setFont(new Font(Font.DIALOG,1,16));
		
		ReaderComponents.getLabel("Patient ID",rightPanel,LABEL_WIDTH);
		ReaderComponents.getLabel(String.valueOf(patientDetail.getPatientId()),rightPanel,LABEL_WIDTH);
		
		ReaderComponents.getLabel("First Name",rightPanel,LABEL_WIDTH);
		ReaderComponents.getLabel(patientDetail.getFirstName(),rightPanel,LABEL_WIDTH);

		ReaderComponents.getLabel("Last Name",rightPanel,LABEL_WIDTH);
		ReaderComponents.getLabel(patientDetail.getLastName(),rightPanel,LABEL_WIDTH);

		ReaderComponents.getLabel("Sex",rightPanel,LABEL_WIDTH);
		ReaderComponents.getLabel(patientDetail.getGender(),rightPanel,LABEL_WIDTH);

		ReaderComponents.getLabel("Age",rightPanel,LABEL_WIDTH);
		ReaderComponents.getLabel(String.valueOf(patientDetail.getAge()),rightPanel,LABEL_WIDTH);

		ReaderComponents.getLabel("Phone No",rightPanel,LABEL_WIDTH);
		ReaderComponents.getLabel(patientDetail.getPhoneNo(),rightPanel,LABEL_WIDTH);

		ReaderComponents.getLabel("Email ID",rightPanel,LABEL_WIDTH);
		ReaderComponents.getLabel(patientDetail.getEmailId(),rightPanel,LABEL_WIDTH);
		
		JPanel buttonPanel = ReaderComponents.getContentPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,3));
		return rightPanel;
	}
	
	@Override
	public JPanel getAddPanel(JPanel contentPanel) {
		
		JLabel header = ReaderComponents.getLabel("Patient Details",contentPanel,LABEL_WIDTH + 200);
		header.setFont(new Font(Font.DIALOG,1,16));
		
		ReaderComponents.getLabel("Patient ID",contentPanel,LABEL_WIDTH);
		patIdTxt = ReaderComponents.getTextField(contentPanel,TEXTFIELD_WIDTH);
		patIdTxt.setText(String.valueOf(PatientDetailHelper.getNextIdNumber()));
		patIdTxt.setEnabled(false);
		
		ReaderComponents.getLabel("First Name",contentPanel,LABEL_WIDTH);
		patFirstNameTxt = ReaderComponents.getTextField(contentPanel,TEXTFIELD_WIDTH);

		ReaderComponents.getLabel("Last Name",contentPanel,LABEL_WIDTH);
		patLastNameTxt = ReaderComponents.getTextField(contentPanel,TEXTFIELD_WIDTH);

		ReaderComponents.getLabel("Sex",contentPanel,LABEL_WIDTH);
		String data[] = {"Unknown","Male", "Female"};
		patSexCmbo = ReaderComponents.getComboBox(data ,contentPanel, TEXTFIELD_WIDTH*75/100);
//		ReaderComponents.getLabel(" ",patientDetailPanel,LABEL_WIDTH);

		ReaderComponents.getLabel("Age",contentPanel,LABEL_WIDTH);
		patAgeTxt = ReaderComponents.getTextField(contentPanel,TEXTFIELD_WIDTH*60/100);
//		ReaderComponents.getLabel("",patientDetailPanel,LABEL_WIDTH);

		ReaderComponents.getLabel("Phone No",contentPanel,LABEL_WIDTH);
		phoneNoTxt = ReaderComponents.getTextField(contentPanel,TEXTFIELD_WIDTH);

		ReaderComponents.getLabel("Email ID",contentPanel,LABEL_WIDTH);
		emailIdTxt = ReaderComponents.getTextField(contentPanel,TEXTFIELD_WIDTH);
		
		JPanel buttonPanel = ReaderComponents.getContentPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,3));
		buttonPanel.setPreferredSize(new Dimension(280,40));
		
		addPatientbt = ReaderComponents.getButton("Add", buttonPanel);
		addPatientbt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(validateAddPatient()){
					insertPatientDetails();
					MainWindow.getInstance().patientTab.getPatientDetailTable("");
					MainWindow.getInstance().patientTab.getFirstRowData();
					MainWindow.frame.repaint();
					MainWindow.frame.validate();
				}
			}
		});
		
		clearBt = ReaderComponents.getButton("Clear", buttonPanel);
		clearBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearAllTextFields();
			}
		});
		contentPanel.add(buttonPanel);
		return contentPanel;
	}
	
	@Override
	public String getDefaultBtView() {
		return "default";
	}
	
	public boolean validateAddPatient(){
		if(patIdTxt.getText() == null || patIdTxt.getText().isEmpty()){
			patIdTxt.setBorder(BorderFactory.createLineBorder(Color.RED));
			JOptionPane.showMessageDialog(null, "Enter Patient ID");
			return false;
		}
		patIdTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(patFirstNameTxt.getText() == null || patFirstNameTxt.getText().isEmpty()){
			patFirstNameTxt.setBorder(BorderFactory.createLineBorder(Color.RED));
			JOptionPane.showMessageDialog(null, "Enter Patient Name");
			return false;
		}
		patFirstNameTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(patLastNameTxt.getText() == null || patLastNameTxt.getText().isEmpty()){
			patLastNameTxt.setBorder(BorderFactory.createLineBorder(Color.RED));
			JOptionPane.showMessageDialog(null, "Enter Patient Last Name");
			return false;
		}
		patLastNameTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(patAgeTxt.getText() == null || patAgeTxt.getText().isEmpty() || !patAgeTxt.getText().matches("[0-9]+")){
			patAgeTxt.setBorder(BorderFactory.createLineBorder(Color.RED));
			JOptionPane.showMessageDialog(null, "Enter Patient Age in Number");
			return false;
		}
		patAgeTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		return true;
	}
	
	public void clearAllTextFields(){
		patIdTxt.setText("");
		patFirstNameTxt.setText("");
		patLastNameTxt.setText("");
		patAgeTxt.setText("");
		patSexCmbo.setSelectedIndex(0);
		
		patIdTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		patFirstNameTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		patLastNameTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		patAgeTxt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}

	@Override
	public String[][] getDataStrAry(List dataList) {
		List<PatientDetailsBO> qcBOList = (List<PatientDetailsBO>) dataList;
		String data[][] = new String[qcBOList.size()][5];
		for(int i=0;i<qcBOList.size();i++){
			PatientDetailsBO qcBO = qcBOList.get(i);
			data[i][0] = String.valueOf(qcBO.getPatientId());
			data[i][1] = qcBO.getFirstName();
			data[i][2] = qcBO.getLastName();
			data[i][3] = String.valueOf(qcBO.getAge());
			data[i][4] = String.valueOf(qcBO.getGender());
		}
		return data;
	}

	@Override
	public int getTableRowSize() {
		if(patientDetailList != null){
			return patientDetailList.size();
		}
		return 0;
	}
	
	public void insertPatientDetails(){
		PatientDetailsBO patientDetail = new PatientDetailsBO();
		patientDetail.setPatientId(Long.parseLong(patIdTxt.getText()));
		patientDetail.setAge(Integer.parseInt(patAgeTxt.getText()));
		patientDetail.setFirstName(patFirstNameTxt.getText());
		patientDetail.setLastName(patLastNameTxt.getText());
		patientDetail.setGender(patSexCmbo.getSelectedItem().toString());
		patientDetail.setPhoneNo(phoneNoTxt.getText());
		patientDetail.setEmailId(emailIdTxt.getText());
		PatientDetailHelper.addPatient(patientDetail);
	}
}
