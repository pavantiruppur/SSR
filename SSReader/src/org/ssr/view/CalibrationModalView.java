package org.ssr.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
import org.ssr.bo.StandardDetailsBO;
import org.ssr.bo.StripDetailBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ParameterDetailHelper;
import org.ssr.converthelper.StandardDetailHelper;
import org.ssr.dao.parameterdetails.IParameterDetails;

public class CalibrationModalView {

	public static final int LABEL_WIDTH = 100;
	
	JDialog dialog = null;
	JPanel parameterDetailPanel = null;
	public JTable resultDetailTable = null;
	JScrollPane resultScrollPane = null;

	public void showCalibConfirmPanel(List<StripDetailBO> stripDetailList, ParameterDetailsBO parameterDetail){
		List<StandardDetailsBO> standardDetailList = StandardDetailHelper.getStandardDetailForParameterId(parameterDetail.getParameterId());
		for(int i = 0; i < stripDetailList.size() ; i++){
			standardDetailList.get(i).setRluValue(stripDetailList.get(i).getRlu());
		}
		
		buildPanel(standardDetailList, parameterDetail);
		createModal();
	}
	
	public JPanel buildPanel(final List<StandardDetailsBO> standardDetailsList, final ParameterDetailsBO parameterDetail){
		this.parameterDetailPanel = ReaderComponents.getRightPanelContent();
		parameterDetailPanel.setPreferredSize(new Dimension(300,380 + parameterDetail.getNoOfStd() * 22));
		if(parameterDetail == null){
			return null;
		}
//		parameterDetail = parameterModule.getParameterStandartDetail(parameterDetail.getParameterId());
		JLabel header = ReaderComponents.getLabel("Parameter Details",parameterDetailPanel,LABEL_WIDTH + 80);
		header.setFont(new Font(Font.DIALOG,1,16));
		
		
		if(parameterDetail != null){
			ReaderComponents.getLabel(parameterDetail.getParameterId() + " - "+ parameterDetail.getParameterName(),parameterDetailPanel,LABEL_WIDTH);
		} 
		ReaderComponents.getLabel("",parameterDetailPanel,LABEL_WIDTH - 85);
		JButton saveBt = ReaderComponents.getButton("Save", parameterDetailPanel, 120 , 30);
		saveBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				confirmGraph(standardDetailsList, parameterDetail);
				dialog.setVisible(false);
			}
		});
		ReaderComponents.getLabel("",parameterDetailPanel,LABEL_WIDTH - 90);
		JButton discardBt = ReaderComponents.getButton("Discard", parameterDetailPanel, 120 , 30);
		discardBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
			}
		});
		int i = 0;
		parameterDetail.setStdDetailList(standardDetailsList);
		String data[][] = new String[standardDetailsList.size()][3];
		for(StandardDetailsBO standardDetail : standardDetailsList){
			data[i][0] = String.valueOf(i+1);
			data[i][1] = String.valueOf(standardDetail.getRluValue());
			data[i][2] = String.valueOf(standardDetail.getStdValue());
			i++;
		}
		
		String col[] = {"Standard",getRluODMode(),"Conc"};
		resultDetailTable = ReaderComponents.getTable(data, col, 20);
		resultScrollPane = ReaderComponents.getScrollPane(resultDetailTable);
		resultScrollPane.setPreferredSize(new Dimension(290,parameterDetail.getNoOfStd() * 22));
		
		parameterDetailPanel.add(resultScrollPane);
		
		JPanel buttonPanel = ReaderComponents.getContentPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,3));
		buttonPanel.setPreferredSize(new Dimension(280,40));
		JPanel resultChart = new JPanel();
		resultChart.setPreferredSize(new Dimension(290,300));
		resultChart.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ChartPanel chartPanel = new ChartPanel(getXYChart(parameterDetail));
		chartPanel.setPreferredSize(new Dimension(285, 280));
		resultChart.add(chartPanel);
		parameterDetailPanel.add(resultChart);
		MainWindow.frame.validate();
		return parameterDetailPanel;
	}
	
	public void confirmGraph(List<StandardDetailsBO> standardDetailsList, ParameterDetailsBO parameterDetail){
		StandardDetailHelper.updateStdDetails(standardDetailsList, parameterDetail.getParameterId());
		
		parameterDetail.setCalibrated(true);
		ParameterDetailHelper.updateParameter(parameterDetail);
	}
	
	public void createModal(){
		dialog = new JDialog(MainWindow.frame);
		dialog.add(parameterDetailPanel);
		dialog.pack();
		dialog.setModal(true);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	public String getRluODMode(){
		return ReaderComponents.getMode().equals("ELISA") ? "O.D":"RLU";
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
}
