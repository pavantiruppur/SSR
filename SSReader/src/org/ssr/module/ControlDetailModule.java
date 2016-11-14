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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.ssr.bo.ControlDetialBO;
import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.ResultDetailBO;
import org.ssr.bo.StandardDetailsBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ControlDetailHelper;
import org.ssr.converthelper.ResultDetailHelper;
import org.ssr.converthelper.StandardDetailHelper;
import org.ssr.enums.TestTypeEnum;
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
	List<ControlDetialBO> controlDetailList = null;
	
	static ControlDetailModule instance = null;
	
	public static ControlDetailModule getInstance(){
		if(instance == null){
			instance = new ControlDetailModule();
		}
		return instance;
	}
	
	@Override
	public String[] getColumnName() {
		String[] col = {"Name","Analyte","QcLot","Reference","RefPlusOrMinus"};
		return col;
	}

	@Override
	public String[][] getDataStrAry(String query) {
		controlDetailList = ControlDetailHelper.getAllControlDetialsForWhere(query);
		String[][] data = getDataStrAry(controlDetailList);
		return data;
	}

	@Override
	public String getSearchString() {
		return "Search using Id, Name and Formula";
	}

	@Override
	public JPanel getRightPanel(JPanel rigthPanel, JTable patientDetailTable) {
		int row = patientDetailTable.getSelectedRow();
		if(row<0){
			return null;
		}
		ControlDetialBO controlDetail = null;
		if(controlDetailList != null){
//			parameterDetail = parameterDetailList.get(row);
			controlDetail = ControlDetailHelper.getControlDetial(controlDetailList.get(row).getQcId());
		}
		if(controlDetail == null){
			return null;
		}
//		parameterDetail = parameterModule.getParameterStandartDetail(parameterDetail.getParameterId());
		JLabel header = ReaderComponents.getLabel("Leavy Jennings",rigthPanel ,LABEL_WIDTH + 200);
		header.setFont(new Font(Font.DIALOG,1,16));

		JPanel resultChart = new JPanel();
		resultChart.setPreferredSize(new Dimension(290,410));
		ChartPanel chartPanel = new ChartPanel(getChart(controlDetail));
		chartPanel.setPreferredSize(new Dimension(285, 400));
		resultChart.add(chartPanel);
		resultChart.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rigthPanel.add(resultChart);
		MainWindow.frame.validate();
		
		return rigthPanel;
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
				MainWindow.getInstance().controlTab.getPatientDetailTable("");
				MainWindow.getInstance().controlTab.getFirstRowData();
				MainWindow.frame.repaint();
				MainWindow.frame.validate();
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
		List<ControlDetialBO> qcBOList = (List<ControlDetialBO>) dataList;
		String data[][] = new String[qcBOList.size()][5];
		for(int i=0;i<qcBOList.size();i++){
			ControlDetialBO qcBO = qcBOList.get(i);
			data[i][0] = qcBO.getBarcode();
			data[i][1] = qcBO.getAnalyte();
			data[i][2] = String.valueOf(qcBO.getQcLot());
			data[i][3] = String.valueOf(qcBO.getReference());
			data[i][4] = String.valueOf(qcBO.getRefPlusOrMinus());
		}
		return data;
	}

	@Override
	public int getTableRowSize() {
		if(controlDetailList != null){
			return controlDetailList.size();
		}
		return 0;
	}
	
	public void insertQCMaterial(){
		ControlDetialBO qcBO = new ControlDetialBO();
		qcBO.setBarcode(barcodeTxt.getText());
		qcBO.setAnalyte(analyteTxt.getText());
		qcBO.setParameterId(selectedParameterDetailBO.getParameterId());
		qcBO.setQcLot(Integer.parseInt(qcLotTxt.getText()));
		qcBO.setReference(Double.parseDouble(referenceTxt.getText()));
		qcBO.setRefPlusOrMinus(Double.parseDouble(refPlusOrMinulTxt.getText()));
		ControlDetailHelper.addStrip(qcBO);
//		if(status > 0){
//			JOptionPane.showMessageDialog(null, "QC Data Added Successfully");
//		}
	}
	
	public JFreeChart getChart(ControlDetialBO qcBO){
		Double reference = qcBO.getReference();
		Double refPlusOrMinus = qcBO.getRefPlusOrMinus();
		double refPlus = reference + refPlusOrMinus;
		double refMinus = reference - refPlusOrMinus;

		DefaultKeyedValues data = new DefaultKeyedValues();
		try {
			List<ResultDetailBO> resultDetails = ResultDetailHelper.getAllResultDetailsForPatientID(qcBO.getQcId() +"-"+ qcBO.getBarcode() ,TestTypeEnum.CONTROL);
			Object[][] resultPlots = new Object[2][resultDetails.size()];
			int index = 0;
			Double val = 0.0;
			for(ResultDetailBO resultDetailBo : resultDetails) {
				val = resultDetailBo.getConc();
				if(val <= refPlus && val >= refMinus){
					resultPlots[0][index] = val;
					resultPlots[1][index] = resultDetailBo.getCreationDate(); 
					data.addValue(resultDetailBo.getCreationDate(), val);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

        CategoryDataset dataset = DatasetUtilities.createCategoryDataset("CONC", data);
        JFreeChart chart = ChartFactory.createBarChart("","","CONC",dataset,PlotOrientation.HORIZONTAL,true,true,false);

        //Switch from a Bar Rendered to a LineAndShapeRenderer so the chart looks like an XYChart
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setBaseLinesVisible(true); //TUrn of the lines
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setDomainGridlinesVisible(true);
        plot.setRenderer(0, renderer);

        NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();        
        numberAxis.setRange(new Range(refMinus - refPlusOrMinus, refPlus + refPlusOrMinus));
        
        return chart;
	}
}
