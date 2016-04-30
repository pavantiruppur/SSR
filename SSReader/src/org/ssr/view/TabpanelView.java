package org.ssr.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ssr.components.ReaderComponents;
import org.ssr.module.IDataDetailPopulate;

public class TabpanelView {

	private static final int TEXTFIELD_WIDTH = 180;
	public static final int LABEL_WIDTH = 100;
	
	JPanel tabPanel = null;
	JPanel tablePanel = null, rightPanel = null, contentPanel = null;
	JTextField searchTxt = null;
	public JTable detailTable = null;
	JButton addNewBt = null;
	JScrollPane scrollPane = null;
	IDataDetailPopulate module = null;
	
	public JPanel getTabPanel(final IDataDetailPopulate module, String srcString){
		this.module = module;
		tabPanel = ReaderComponents.getContentPanel();
		ReaderComponents.setFlowLayout(tabPanel);
		
		tablePanel = ReaderComponents.getContentPanel();
		ReaderComponents.setFlowLayoutPadding(tablePanel, 0);
		tablePanel.setPreferredSize(new Dimension(400,615));
		
		ReaderComponents.getLabel("Search  ",tablePanel,LABEL_WIDTH - 30);
		searchTxt = ReaderComponents.getTextField(tablePanel,TEXTFIELD_WIDTH + 80);
		searchTxt.setText(module.getSearchString());
		
		searchTxt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				if(searchTxt.getText().trim().equals("")){
					searchTxt.setText(module.getSearchString());
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				if(searchTxt.getText().equals(module.getSearchString())){
					searchTxt.setText("");
				}
			}
		});
		
		JButton leftMove = ReaderComponents.getButton("<", tablePanel, 20, 20);
		leftMove.setBorder(null);
		
		searchTxt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				//TODO:
				getPatientDetailTable(searchTxt.getText());
				MainWindow.frame.repaint();
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		//TODO:
		getPatientDetailTable(srcString);
		
		getFirstRowData();
		tabPanel.add(tablePanel);
		//generate the AddNewPanel
		getRightPanel(module, module.getDefaultBtView());
		
		return tabPanel;
	}
	

	public void getFirstRowData(){
		if(detailTable != null && module.getTableRowSize() > 0 && detailTable.getSelectedRowCount() == 0){
			detailTable.changeSelection(0, 0, false, false);
		} else {
			
//			getAddParameterPanel();
			detailTable.getSelectionModel().clearSelection();
			MainWindow.frame.validate();
		}
	}
	
	public JPanel getRightPanel(IDataDetailPopulate module, String mode){

		if(rightPanel != null){
			tabPanel.remove(rightPanel);
		}
		
		rightPanel = ReaderComponents.getContentPanel();
		ReaderComponents.setFlowLayoutPadding(rightPanel, 0);
		rightPanel.setPreferredSize(new Dimension(310,615));
		
		//button panel add, edit, "<"
		JPanel buttonPanel = null;

		//content panel of the operation
		contentPanel = ReaderComponents.getRightPanelContent();
		
		if(mode.equals("default")){
			buttonPanel = getDefaultBtView(module);
			contentPanel = module.getRightPanel(contentPanel, detailTable);
		} else if(mode.equals("add")){
//			buttonPanel = getDefaultBtView(module);
			contentPanel = module.getAddPanel(contentPanel);
		} else if(mode.equals("edit")){
			
		} else if(mode.equals("result")){
			contentPanel = module.getRightPanel(contentPanel, detailTable);
		}
		
		if(contentPanel == null){
			contentPanel = ReaderComponents.getRightPanelContent();
			contentPanel = module.getAddPanel(contentPanel);
			buttonPanel = null;
		}
		
		if(buttonPanel != null){
			rightPanel.add(buttonPanel);
		}
		
		if(contentPanel != null){
			rightPanel.add(contentPanel);
		} 
		tabPanel.add(rightPanel);
		return rightPanel;
	}


	private JPanel getDefaultBtView(final IDataDetailPopulate module) {
		JPanel addEditPanel = ReaderComponents.getContentPanel();
		addEditPanel.setPreferredSize(new Dimension(300,30));
		ReaderComponents.setFlowLayoutPadding(FlowLayout.LEFT,addEditPanel, 3,1);
		
		addNewBt = ReaderComponents.getButton("Add", addEditPanel, 70, 26);
		addNewBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getRightPanel(module, "add");
				detailTable.getSelectionModel().clearSelection();
				MainWindow.frame.repaint();
				MainWindow.frame.validate();
			}
		});
		return addEditPanel;
	}
	
	public void getPatientDetailTable(String str){
		if(scrollPane != null){
			tablePanel.remove(scrollPane);
		}
		String data[][] = module.getDataStrAry(str);
		
		String col[] = module.getColumnName();
		detailTable = ReaderComponents.getTable(data, col, 30);
		detailTable.getTableHeader().setPreferredSize(new Dimension(400, 40));
		scrollPane = ReaderComponents.getScrollPane(detailTable);
		scrollPane.setPreferredSize(new Dimension(400,448));
		detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		detailTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(detailTable.getSelectedRow() >= 0){
					getRightPanel(module, module.getDefaultBtView());
					MainWindow.frame.validate();
				}
			}
		});
		tablePanel.add(scrollPane);
	}

	public String getRluODMode(){
		return "RLU";
	}
}
