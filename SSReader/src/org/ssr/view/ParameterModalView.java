package org.ssr.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.ssr.bo.ParameterDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ParameterDetailHelper;

public class ParameterModalView {

	private static final int TEXTFIELD_WIDTH = 180;
	public static final int LABEL_WIDTH = 100;

	JDialog dialog = null;
	
	JPanel parameterPanel = null;
	JTextField searchTxt = null;
	JScrollPane scrollPane = null;
	public JTable patientDetailTable = null;
	JTextField parameterIdTxt = null;
	JButton loadBtn = null;
	
	List<ParameterDetailsBO> qcBOList = null;
	ParameterDetailsBO selectedParamBO = null;
	
	public static ParameterModalView getInstance(){
		return new ParameterModalView();
	}
	
	public void showParameterModal(final boolean isLoadAll, JTextField paraIdTxt, ParameterDetailsBO seleParamBO){
		this.parameterIdTxt = paraIdTxt;
		selectedParamBO = seleParamBO;
		parameterPanel = ReaderComponents.getContentPanel();
		parameterPanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,2));
		parameterPanel.setPreferredSize(new Dimension(300,500));
		
		ReaderComponents.getLabel("Search  ",parameterPanel,LABEL_WIDTH - 30);
		searchTxt = ReaderComponents.getTextField(parameterPanel,TEXTFIELD_WIDTH + 50);
		searchTxt.setText("Search using Id, Name and Formula");
		
		searchTxt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				if(searchTxt.getText().trim().equals("")){
					searchTxt.setText("Search using Id, Name and Formula");
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				if(searchTxt.getText().equals("Search using Id, Name and Formula")){
					searchTxt.setText("");
				}
			}
		});
		
		searchTxt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				getPatientDetailTable(searchTxt.getText(), isLoadAll);
				dialog.validate();
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		
		loadBtn = ReaderComponents.getButton("Add", parameterPanel);
		loadBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
				int row = patientDetailTable.getSelectedRow();
				parameterIdTxt.setText(patientDetailTable.getValueAt(row, 0).toString());
				selectedParamBO.setParameterId(qcBOList.get(row).getParameterId());
				selectedParamBO.setNoOfStd(Integer.parseInt(patientDetailTable.getValueAt(row, 2).toString()));
				MainWindow.frame.validate();
			}
		});
		getPatientDetailTable("", isLoadAll);
		createModal();
	}
	
	public void createModal(){
		dialog = new JDialog(MainWindow.frame);
		dialog.add(parameterPanel);
		dialog.pack();
		dialog.setModal(true);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	public void getPatientDetailTable(String str, boolean isLoadAll){
		if(scrollPane != null){
			parameterPanel.remove(scrollPane);
		}
		qcBOList = ParameterDetailHelper.getAllParameterDetails("", isLoadAll, false);
		String data[][] = ParameterDetailHelper.getPatientDataStrAry(qcBOList);
		
		String col[] = {"Name","Unit","NoOfStd","Formula"};
		patientDetailTable = ReaderComponents.getTable(data, col, 20);
		scrollPane = ReaderComponents.getScrollPane(patientDetailTable);
		scrollPane.setPreferredSize(new Dimension(300,580));
		patientDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		if(patientDetailTable != null && patientDetailTable.getSelectedRowCount() == 0){
			patientDetailTable.changeSelection(0, 0, false, false);
		}
		parameterPanel.add(scrollPane);
	}
}