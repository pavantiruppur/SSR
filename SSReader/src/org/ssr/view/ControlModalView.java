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

import org.ssr.bo.ControlDetialBO;
import org.ssr.bo.ParameterDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ControlDetailHelper;
import org.ssr.converthelper.ParameterDetailHelper;

public class ControlModalView {

	private static final int TEXTFIELD_WIDTH = 180;
	public static final int LABEL_WIDTH = 100;

	JDialog dialog = null;
	
	JPanel contorlPanel = null;
	JTextField searchTxt = null;
	JScrollPane scrollPane = null;
	public JTable patientDetailTable = null;
	JTextField contorlIdTxt = null;
	JButton loadBtn = null;
	
	List<ControlDetialBO> qcBOList = null;
	ParameterDetailsBO selectedParamBO = null;
	
	public static ControlModalView getInstance(){
		return new ControlModalView();
	}
	
	public void showControlModal(final boolean isLoadAll, JTextField paraIdTxt, ParameterDetailsBO seleParamBO){
		this.contorlIdTxt = paraIdTxt;
		selectedParamBO = seleParamBO;
		contorlPanel = ReaderComponents.getContentPanel();
		contorlPanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,2));
		contorlPanel.setPreferredSize(new Dimension(415,500));
		
		ReaderComponents.getLabel("Search  ",contorlPanel,LABEL_WIDTH - 30);
		searchTxt = ReaderComponents.getTextField(contorlPanel,TEXTFIELD_WIDTH + 50);
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
				getControlDetailTable(searchTxt.getText(), isLoadAll);
				dialog.validate();
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		
		loadBtn = ReaderComponents.getButton("Add", contorlPanel);
		loadBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
				int row = patientDetailTable.getSelectedRow();
				contorlIdTxt.setText(patientDetailTable.getValueAt(row, 0).toString() +"-"+ qcBOList.get(row).getBarcode());
				selectedParamBO.setParameterId(qcBOList.get(row).getParameterId());
				ParameterDetailsBO parameterDetailBO = ParameterDetailHelper.getParameterDetail(qcBOList.get(row).getParameterId(), false);
				selectedParamBO.setNoOfStd(parameterDetailBO.getNoOfStd());
				MainWindow.frame.validate();
			}
		});
		getControlDetailTable("", isLoadAll);
		createModal();
	}
	
	public void createModal(){
		dialog = new JDialog(MainWindow.frame);
		dialog.add(contorlPanel);
		dialog.pack();
		dialog.setModal(true);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	public void getControlDetailTable(String str, boolean isLoadAll){
		if(scrollPane != null){
			contorlPanel.remove(scrollPane);
		}
		qcBOList = ControlDetailHelper.getAllControlDetialsForWhere("");
		String data[][] = ControlDetailHelper.getControlDataStrAry(qcBOList);
		
		String col[] = {"ControlID","Name","Analyte","QcLot","Reference","+/-"};
		patientDetailTable = ReaderComponents.getTable(data, col, 20);
		scrollPane = ReaderComponents.getScrollPane(patientDetailTable);
		scrollPane.setPreferredSize(new Dimension(410,580));
		patientDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		if(patientDetailTable != null && patientDetailTable.getSelectedRowCount() == 0){
			patientDetailTable.changeSelection(0, 0, false, false);
		}
		contorlPanel.add(scrollPane);
	}
}