package org.ssr.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.StandardDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ParameterDetailHelper;
import org.ssr.converthelper.StandardDetailHelper;
import org.ssr.enums.FilterWheelEnum;
import org.ssr.view.MainWindow;

public class ParameterDetailModule implements IDataDetailPopulate {

	
	private static final int TEXTFIELD_WIDTH = 180;
	public static final int LABEL_WIDTH = 100;
	
	JTextField patIdTxt = null, patFirstNameTxt = null, patLastNameTxt = null,  patAgeTxt = null;
	JComboBox patSexCmbo = null, filterWheelCmbo = null;
	JButton addPatientbt = null, clearBt = null;
	JPanel patientDetailPanel = null;
	JPanel noStdPanel = null;
	ArrayList<JTextField> noStdList = null;
	public JTable resultDetailTable = null;
	JScrollPane resultScrollPane = null;
	List<ParameterDetailsBO> parameterDetailList = null;

	static ParameterDetailModule instance = null;
	
	public static ParameterDetailModule getInstance(){
		if(instance == null){
			instance = new ParameterDetailModule();
		}
		return instance;
	}
	
	@Override
	public String[] getColumnName() {
		String[] col = {"Name","Unit","NoOfStd","Formula"};
		return col;
	}

	@Override
	public String[][] getDataStrAry(String searchString) {
		parameterDetailList = ParameterDetailHelper.getAllParameterDetails(searchString, true, false);
		String[][] data = getDataStrAry(parameterDetailList);
		return data;
	}

	@Override
	public String getSearchString() {
		return "Search using Id, Name and Formula";
	}

	@Override
	public JPanel getRightPanel(JPanel rigthPanel, JTable patientDetailTable) {
		this.patientDetailPanel = rigthPanel;
		int row = patientDetailTable.getSelectedRow();
		if(row<0){
			return null;
		}
		ParameterDetailsBO parameterDetail = null;
		if(parameterDetailList != null){
//			parameterDetail = parameterDetailList.get(row);
			parameterDetail = ParameterDetailHelper.getParameterDetail(parameterDetailList.get(row).getParameterId(), true);
		}
		if(parameterDetail == null){
			return null;
		}
//		parameterDetail = parameterModule.getParameterStandartDetail(parameterDetail.getParameterId());
		JLabel header = ReaderComponents.getLabel("Parameter Details",patientDetailPanel,LABEL_WIDTH + 200);
		header.setFont(new Font(Font.DIALOG,1,16));
		
		if(parameterDetail != null){
			ReaderComponents.getLabel(parameterDetail.getParameterId() + " - "+ parameterDetail.getParameterName(),patientDetailPanel,LABEL_WIDTH + 200);
		} 
		int i = 0;
		List<StandardDetailsBO> standardDetailsList = StandardDetailHelper.getStandardDetailForParameterId(parameterDetail.getParameterId());
		parameterDetail.setStdDetailList(standardDetailsList);
		String data[][] = new String[standardDetailsList.size()][3];
		for(StandardDetailsBO standardDetail : standardDetailsList){
			data[i][0] = String.valueOf(i+1);
			data[i][1] = standardDetail.getRluValueString();
			data[i][2] = String.valueOf(standardDetail.getStdValue());
			i++;
		}
		
		String col[] = {"Standard",getRluODMode(),"Conc"};
		resultDetailTable = ReaderComponents.getTable(data, col, 20);
		resultScrollPane = ReaderComponents.getScrollPane(resultDetailTable);
		resultScrollPane.setPreferredSize(new Dimension(290,parameterDetail.getNoOfStd() * 22));
		
		patientDetailPanel.add(resultScrollPane);
		
		JPanel buttonPanel = ReaderComponents.getContentPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,3));
		buttonPanel.setPreferredSize(new Dimension(280,40));
		JPanel resultChart = new JPanel();
		resultChart.setPreferredSize(new Dimension(290,300));
		resultChart.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ChartPanel chartPanel = new ChartPanel(getXYChart(parameterDetail));
		chartPanel.setPreferredSize(new Dimension(285, 280));
		resultChart.add(chartPanel);
		patientDetailPanel.add(resultChart);
		MainWindow.frame.validate();
		return patientDetailPanel;
	}
	
	@Override
	public JPanel getAddPanel(JPanel rigthPanel) {
		patientDetailPanel = rigthPanel;
		JLabel header = ReaderComponents.getLabel("Parameter Details",patientDetailPanel,LABEL_WIDTH + 200);
		header.setFont(new Font(Font.DIALOG,1,16));
		
		ReaderComponents.getLabel("Number",patientDetailPanel,LABEL_WIDTH + 20);
		patIdTxt = ReaderComponents.getTextField(patientDetailPanel,TEXTFIELD_WIDTH - 20);
		patIdTxt.setText(String.valueOf(ParameterDetailHelper.getNextIdNumber()));
		patIdTxt.setEnabled(false);
		
		ReaderComponents.getLabel("Name",patientDetailPanel,LABEL_WIDTH + 20);
		patFirstNameTxt = ReaderComponents.getTextField(patientDetailPanel,TEXTFIELD_WIDTH - 20);

		ReaderComponents.getLabel("Untis",patientDetailPanel,LABEL_WIDTH + 20);
		patLastNameTxt = ReaderComponents.getTextField(patientDetailPanel,TEXTFIELD_WIDTH - 20);

		ReaderComponents.getLabel("No. Of std",patientDetailPanel,LABEL_WIDTH + 20);
		String data[] = {"0","1", "2","3","4", "5","6","7", "8","9","10"};
		patSexCmbo = ReaderComponents.getComboBox(data ,patientDetailPanel, TEXTFIELD_WIDTH*25/100);
		patSexCmbo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				generateStdPanel();
				MainWindow.frame.repaint();
			}
		});
		ReaderComponents.getLabel(" ",patientDetailPanel,LABEL_WIDTH);
		
		if(MainWindow.mode.equals("ELISA")){
			ReaderComponents.getLabel("Filter Wheel",patientDetailPanel,LABEL_WIDTH + 20);
			String filterWheelData[] = new String[16];
			int i =0;
			for(FilterWheelEnum filter : FilterWheelEnum.values()){
				filterWheelData[i++] = filter.getValue();
			}
			filterWheelCmbo = ReaderComponents.getComboBox(filterWheelData ,patientDetailPanel, TEXTFIELD_WIDTH*50/100);
		} 

		ReaderComponents.getLabel("Formula",patientDetailPanel,LABEL_WIDTH + 20);
		patAgeTxt = ReaderComponents.getTextField(patientDetailPanel,TEXTFIELD_WIDTH*50/100);
		
		JPanel buttonPanel = ReaderComponents.getContentPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,3));
		buttonPanel.setPreferredSize(new Dimension(280,40));
		
		addPatientbt = ReaderComponents.getButton("Add", buttonPanel);
		addPatientbt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(validateAddPatient()){
					insertPatientDetails();
					MainWindow.getInstance().parameterTab.getPatientDetailTable("");
					MainWindow.getInstance().parameterTab.getFirstRowData();
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
		patientDetailPanel.add(buttonPanel);
		return patientDetailPanel;
	}
	
	@Override
	public String getDefaultBtView() {
		return "default";
	}
	
	public JFreeChart getXYChart(ParameterDetailsBO parameterDetailBO){
		List<StandardDetailsBO> stdDetailList = null;
		if(parameterDetailBO.getStdDetailList() != null){
			stdDetailList = parameterDetailBO.getStdDetailList();
		} else {
			stdDetailList = StandardDetailHelper.getStandardDetailForParameterId(parameterDetailBO.getParameterId());	
		}
		double[][] stdPlots = new double[2][stdDetailList.size()];
    	double[][] resultPlot = new double[2][1];
	
		for(int i = 0; i< stdDetailList.size(); i++){
			stdPlots[0][i] = stdDetailList.get(i).getStdValue();
			stdPlots[1][i] = stdDetailList.get(i).getRluValue();
		}
		
		DefaultXYDataset dataset = new DefaultXYDataset();
      	dataset.addSeries(getRluODMode(), resultPlot);
      	dataset.addSeries(parameterDetailBO.getParameterName(), stdPlots);
		JFreeChart chart = createXYChart(dataset, parameterDetailBO);
		return chart;
	}
	
	private JFreeChart createXYChart(DefaultXYDataset dataset, ParameterDetailsBO parameterDetailBO) {

		JFreeChart chart = ChartFactory.createXYLineChart(
	         parameterDetailBO.getParameterName() , // The chart title
	         "Conc", // x axis label
	         getRluODMode(), // y axis label
	         dataset, // The dataset for the chart
	         PlotOrientation.VERTICAL,
	         true, // Is a legend required?
	         false, // Use tooltips
	         false // Configure chart to generate URLs?
	      );
		TextTitle source = new TextTitle("");

		source.setPosition(RectangleEdge.BOTTOM);
		source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		chart.addSubtitle(source);
		chart.setBackgroundPaint(Color.white);
		
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setDomainGridlinesVisible(true);
		
		ValueAxis categoryAxis = (ValueAxis) plot.getDomainAxis();
//		categoryAxis.setLabelAngle(45);
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//		rangeAxis.setRange(Math.round(0),Math.round(15));
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
		 renderer.setShapesVisible(true);
		renderer.setDrawOutlines(true);
		return chart;
	}

	public String getRluODMode(){
		return ReaderComponents.getMode().equals("ELISA") ? "O.D":"RLU";
	}
	
	public void generateStdPanel(){
		if(noStdPanel != null){
			patientDetailPanel.remove(noStdPanel);
		}
		int noStd = Integer.parseInt(patSexCmbo.getSelectedItem().toString());
		if(noStd == 0){
			return;
		}
		noStdPanel = ReaderComponents.getContentPanel();
		ReaderComponents.setFlowLayoutPadding(FlowLayout.LEFT,noStdPanel, 5,1);
		noStdPanel.setPreferredSize(new Dimension(290,(30*(int)Math.ceil((double)noStd/2))+10));
		noStdPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		noStdList = new ArrayList<JTextField>();
		for(int i=0;i<noStd;i++){
			noStdPanel.add(ReaderComponents.getLabel("Std "+ (i+1),patientDetailPanel,LABEL_WIDTH - 50));
			noStdList.add(ReaderComponents.getTextField(patientDetailPanel,TEXTFIELD_WIDTH - 110));
			noStdPanel.add(noStdList.get(i));
		}
		patientDetailPanel.add(noStdPanel,10);
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
		
		if(patAgeTxt.getText() == null || patAgeTxt.getText().isEmpty()){
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
	
	public void insertPatientDetails(){
		ParameterDetailsBO parameterDetail = new ParameterDetailsBO();
		parameterDetail.setParameterId(Long.parseLong(patIdTxt.getText()));
		parameterDetail.setNoOfStd(Integer.parseInt(patSexCmbo.getSelectedItem().toString()));
		parameterDetail.setParameterName(patFirstNameTxt.getText());
		parameterDetail.setParameterUnit(patLastNameTxt.getText());
		parameterDetail.setFormula(patAgeTxt.getText());
		if(MainWindow.mode.equals("ELISA")){
			parameterDetail.setFilterWheel(FilterWheelEnum.getEnumByKey(filterWheelCmbo.getSelectedItem().toString()));
		} 
		ArrayList<StandardDetailsBO> stdDetailList = new ArrayList<StandardDetailsBO>();
		for(int i=0;i<parameterDetail.getNoOfStd();i++){
			StandardDetailsBO stdDetail = new StandardDetailsBO();
			stdDetail.setStdId(i+1l);
			stdDetail.setStdValue(Double.parseDouble(noStdList.get(i).getText()));
			stdDetailList.add(stdDetail);
		}
		parameterDetail.setStdDetailList(stdDetailList);
		ParameterDetailHelper.addParameter(parameterDetail);
	}

	@Override
	public String[][] getDataStrAry(List dataList) {
		List<ParameterDetailsBO> qcBOList = (List<ParameterDetailsBO>) dataList;
		String data[][] = new String[qcBOList.size()][4];
		for(int i=0;i<qcBOList.size();i++){
			ParameterDetailsBO qcBO = qcBOList.get(i);
//			data[i][0] = String.valueOf(qcBO.getParameterId());
			data[i][0] = qcBO.getParameterName();
			data[i][1] = qcBO.getParameterUnit();
			data[i][2] = String.valueOf(qcBO.getNoOfStd());
			data[i][3] = String.valueOf(qcBO.getFormula());
		}
		return data;
	}

	@Override
	public int getTableRowSize() {
		if(parameterDetailList != null){
			return parameterDetailList.size();
		}
		return 0;
	}
}
