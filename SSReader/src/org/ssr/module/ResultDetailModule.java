package org.ssr.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
import org.ssr.bo.PatientDetailsBO;
import org.ssr.bo.ResultDetailBO;
import org.ssr.bo.StandardDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ParameterDetailHelper;
import org.ssr.converthelper.PatientDetailHelper;
import org.ssr.converthelper.ResultDetailHelper;
import org.ssr.view.MainWindow;

public class ResultDetailModule implements IDataDetailPopulate {

	static ResultDetailModule instance = null;
	List<ResultDetailBO> resultDetailList = null;
	public JTable resultDetailTable;
	
	public static ResultDetailModule getInstance(){
		if(instance == null){
			instance = new ResultDetailModule();
		}
		return instance;
	}
	
	@Override
	public String[] getColumnName() {
		String[] col = {"S.No","<html><center>Parameter<br>Id","<html><center>Patient<br>Id",getRluODMode(),"Conc"};
		return col;
	}

	@Override
	public String[][] getDataStrAry(String searchString) {
		resultDetailList = ResultDetailHelper.getAllResultDetails(searchString);
		String[][] data = getDataStrAry(resultDetailList);
		return data;
	}

	@Override
	public String getSearchString() {
		return "Search using Id, Patient or Parameter";
	}
	
	public String getRluODMode(){
		return ReaderComponents.getMode().equals("ELISA") ? "O.D":"RLU";
	}
	
	@Override
	public JPanel getRightPanel(JPanel rightPanel, JTable detailTable) {
		if(rightPanel != null){
			rightPanel.removeAll();
		}
		int row = detailTable.getSelectedRow();
		if(row<0){
			return null;
		}
		ResultDetailBO resultDetail = null;
		if(resultDetailList != null){
			resultDetail = resultDetailList.get(row);
		}
		if(resultDetail == null){
			return null;
		}
		
		ParameterDetailsBO parameterDetail = ParameterDetailHelper.getParameterDetail(resultDetail.getParameterId(), true);
		PatientDetailsBO patientDetail = null;
		if(resultDetail.getPatientId() != null){
			try{
				Long patientId = Long.parseLong(resultDetail.getPatientId());
				patientDetail = PatientDetailHelper.getPatientDetail(patientId);
			} catch (NumberFormatException e) {}
		}
		if(patientDetail != null){
			ReaderComponents.getLabel(patientDetail.getPatientId() + " - "+ patientDetail.getFirstName() +" "+ patientDetail.getLastName(),rightPanel,LABEL_WIDTH + 200);
			ReaderComponents.getLabel("Age : "+ patientDetail.getAge() + " / " + patientDetail.getGender(),rightPanel,LABEL_WIDTH + 200);
		} else {
			ReaderComponents.getLabel(resultDetail.getPatientId(),rightPanel,LABEL_WIDTH + 200);
		}
		int i = 0;
		String data[][] = new String[parameterDetail.getNoOfStd()][3];
		for(StandardDetailsBO standardDetail : parameterDetail.getStdDetailList()){
			data[i][0] = String.valueOf(i+1);
			data[i][1] = String.valueOf(standardDetail.getRluValue());
			data[i][2] = String.valueOf(standardDetail.getStdValue());
			i++;
		}
		String col[] = {"Standard",getRluODMode(),"Conc"};
		resultDetailTable = ReaderComponents.getTable(data, col, 20);
		JScrollPane resultScrollPane = ReaderComponents.getScrollPane(resultDetailTable);
		resultScrollPane.setPreferredSize(new Dimension(290,parameterDetail.getNoOfStd() * 22));
		
		rightPanel.add(resultScrollPane);
		
		JPanel buttonPanel = ReaderComponents.getContentPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,3));
		buttonPanel.setPreferredSize(new Dimension(280,40));
		JPanel resultChart = new JPanel();
		resultChart.setPreferredSize(new Dimension(290,300));
		resultChart.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ChartPanel chartPanel = new ChartPanel(getXYChart(resultDetail, parameterDetail));
		chartPanel.setPreferredSize(new Dimension(285, 280));
		resultChart.add(chartPanel);
		rightPanel.add(resultChart);
		MainWindow.frame.validate();
		
		return rightPanel;
	}
	
	@Override
	public JPanel getAddPanel(JPanel rightPanel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getDefaultBtView() {
		return "result";
	}
	
	public JFreeChart getXYChart(ResultDetailBO result, ParameterDetailsBO parameterDetailBO){
		List<StandardDetailsBO> stdDetailList = parameterDetailBO.getStdDetailList();
		double[][] stdPlots = new double[2][parameterDetailBO.getNoOfStd()];
    	double[][] resultPlot = new double[2][1];
	
		for(int i = 0; i< stdDetailList.size(); i++){
			stdPlots[0][i] = stdDetailList.get(i).getStdValue();
			stdPlots[1][i] = stdDetailList.get(i).getRluValue();
		}
		resultPlot[0][0] = result.getConc();
		resultPlot[1][0] = result.getRlu();
		
		DefaultXYDataset dataset = new DefaultXYDataset();
      	dataset.addSeries(getRluODMode(), resultPlot);
      	dataset.addSeries("TSH", stdPlots);
		JFreeChart chart = createXYChart(dataset, result);
		return chart;
	}
	
	private JFreeChart createXYChart(DefaultXYDataset dataset, ResultDetailBO result) {

		JFreeChart chart = ChartFactory.createXYLineChart(
	         "TSH" , // The chart title
	         "Conc", // x axis label
	         getRluODMode(), // y axis label
	         dataset, // The dataset for the chart
	         PlotOrientation.VERTICAL,
	         true, // Is a legend required?
	         false, // Use tooltips
	         false // Configure chart to generate URLs?
	      );
		chart.addSubtitle(new TextTitle(getRluODMode()+" = "+ result.getRlu() +"  Conc = " + result.getConc()));
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

	@Override
	public String[][] getDataStrAry(List qcBOList) {
		String data[][] = new String[qcBOList.size()][5];
		for(int i=0;i<qcBOList.size();i++){
			ResultDetailBO qcBO = (ResultDetailBO)qcBOList.get(i);
			data[i][0] = String.valueOf(i+1);
			data[i][1] = String.valueOf(qcBO.getParameterId()+ " - " +qcBO.getParameterName());
			data[i][2] = String.valueOf(qcBO.getPatientId());
			data[i][3] = String.valueOf(qcBO.getRluAsString());
			data[i][4] = String.valueOf(qcBO.getConc3Digit());
		}
		return data;
	}

	@Override
	public int getTableRowSize() {
		if(resultDetailList != null){
			return resultDetailList.size();
		}
		return 0;
	}

}
