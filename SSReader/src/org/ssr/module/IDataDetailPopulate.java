package org.ssr.module;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;


public interface IDataDetailPopulate {
	
	public static final int LABEL_WIDTH = 0;
	
	public String getSearchString();
	
	public String[][] getDataStrAry(String query);
	
	public String[] getColumnName();
	
	public JPanel getRightPanel(JPanel rightPanel, JTable detailTable);
	
	public JPanel getAddPanel(JPanel rightPanel);
	
	public String getDefaultBtView();
	
	public String[][] getDataStrAry(List dataList);
	
	public int getTableRowSize();
}
