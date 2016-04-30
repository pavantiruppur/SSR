package org.ssr.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.StripDetailBO;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ParameterDetailHelper;
import org.ssr.converthelper.StripDetailHelper;
import org.ssr.enums.CalibrationTypeEnum;
import org.ssr.enums.ErrorMessageEnum;
import org.ssr.enums.StripStatusEnum;
import org.ssr.enums.TestTypeEnum;
import org.ssr.main.SerialComm;

public class TrayWindowView {
	
	private static TrayWindowView trayWindow = null;
	public static final int LABEL_WIDTH = 30;
	public static final String TRAY_IN = "Tray In";
	public static final String TRAY_OUT = "Tray Out";
	private static final String BLANK_TEST = "Blank Test";
	
	private JPanel trayPanel = null;
	int currentStrip = 0;
	JComboBox testTypeCmb = null;
	JPanel stripDetailPnl = null,selectedStripDetail = null, incubatorPnl = null;
	JRadioButton masterCalib = null;
	JRadioButton twoPointCalib = null;
	JLabel roundImg = null;
	public List<JPanel> vesselPanel = new ArrayList<JPanel>(8);
	public JButton trayInOutbt = null, resetBt = null, readBt = null, blankTestBt = null;
	
	JTextField searchTxt = null;
	JTextField timeTxtHH = null,timeTxtMM = null, tempTxt = null, calibTxt = null;
	JButton loadBtn = null;
	
	StripDetailBO commonStripDetail = new StripDetailBO(1l, TestTypeEnum.SAMPLE, true);
	ParameterDetailsBO stripParameterDetailBO = new ParameterDetailsBO();
	List<StripDetailBO> allStripDetailsList = new ArrayList<StripDetailBO>();
	int calibrationStrips = 0;
	
	public static TrayWindowView getInstance(){
		if(trayWindow == null){
			trayWindow = new TrayWindowView();
		}
		return trayWindow;
	}
	
	public void getCurrentStripRecord(long stripNo){
		StripDetailBO temp = StripDetailHelper.getStripDetail((long)stripNo+1);
		if(temp != null){
			commonStripDetail = temp;
			if(commonStripDetail.getCalibrationType() != null && commonStripDetail.getCalibrationType() == CalibrationTypeEnum.MASTER){
				calibrationStrips = commonStripDetail.getParameterDetail().getNoOfStd();
			} else if(commonStripDetail.getCalibrationType() != null && commonStripDetail.getCalibrationType() == CalibrationTypeEnum.TWO_POINT){
				calibrationStrips = 2;
			}
		} else {
			commonStripDetail = new StripDetailBO(stripNo+1l, TestTypeEnum.SAMPLE, true);
		}
	}
	
	public JPanel designWindow(){
		if(trayPanel != null){
			trayPanel.removeAll();
		} else {
			trayPanel = ReaderComponents.getContentPanel();
			trayPanel.setPreferredSize(new Dimension(200, 500));//160, 420
//		trayPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		}
		getCurrentStripRecord(currentStrip);
		roundImg = new JLabel();
		roundImg.setPreferredSize(new Dimension(150, 120));
		roundImg.setHorizontalAlignment(SwingConstants.CENTER);
		loadStripImg(roundImg);
		roundImg.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int moved = arg0.getWheelRotation() + currentStrip;
				if(moved >= 0 && moved <= 7){
					if(moved < calibrationStrips && currentStrip > moved){
						currentStrip = 0;
					} else if(moved < calibrationStrips && currentStrip < moved){
						currentStrip = calibrationStrips;
					} else{
						currentStrip = moved;
					}
					loadStripImg(roundImg);
					getStripDetailPanel();
					MainWindow.frame.validate();
				}
			}
		});
		JLabel leftMove = ReaderComponents.getLabel("<", trayPanel, 7, 7);
		leftMove.setPreferredSize(new Dimension(16, 16));
		leftMove.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(currentStrip>0){
					currentStrip--;
					if(currentStrip < calibrationStrips){
						currentStrip = 0;
					}
					loadStripImg(roundImg);
					getStripDetailPanel();
					MainWindow.frame.validate();
				}
			}
		});
		trayPanel.add(roundImg);
		JLabel rightMove = ReaderComponents.getLabel(">", trayPanel, 7, 7);
		rightMove.setPreferredSize(new Dimension(16, 16));
		rightMove.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
		rightMove.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(currentStrip<7){
					currentStrip++;
					if(currentStrip < calibrationStrips){
						currentStrip = calibrationStrips;
					}
					loadStripImg(roundImg);
					getStripDetailPanel();
					MainWindow.frame.validate();
				}
			}
		});
		allStripDetailsList = StripDetailHelper.getAllStripDetails();
		vesselPanel = new ArrayList<JPanel>(8);
		for(int i = 1; i <= 8; i++){
			JPanel vesselPnl = createStripNoPanel(i);
			vesselPanel.add(vesselPnl);
			trayPanel.add(vesselPnl);
		}
		ReaderComponents.getSeparator(SwingConstants.HORIZONTAL, trayPanel, 150, 2);
		//initialize content layer
		stripDetailPnl = ReaderComponents.getContentPanel();
		stripDetailPnl.setPreferredSize(new Dimension(190, 140));
//		stripDetailPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ReaderComponents.setFlowLayoutPadding(0, stripDetailPnl, 0, 0);
		getStripDetailPanel();
		trayPanel.add(stripDetailPnl);
		if(trayInOutbt == null){
			trayInOutbt = ReaderComponents.getButton(TRAY_OUT, trayPanel, 80 ,20);
			trayInOutbt.setEnabled(false);
			trayInOutbt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(trayInOutbt.getText().equals(TRAY_OUT)){
						trayInOutbt.setText(TRAY_IN);
						readBt.setEnabled(true);
						SerialComm.sendDataIn();	
						if(StripDetailHelper.getTotalCount() > 0){
							readBt.setEnabled(true);
						}
					} else if(trayInOutbt.getText().equals(TRAY_IN)){
						trayInOutbt.setText(TRAY_OUT);
						readBt.setEnabled(false);
						SerialComm.sendDataOut();			
						readBt.setEnabled(false);
					} 
				}
			});
		} else {
			trayPanel.add(trayInOutbt);
		}
		if(readBt == null){
			readBt = ReaderComponents.getButton("Read", trayPanel, 70 ,20);
			readBt.setEnabled(false);
			readBt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					List<ParameterDetailsBO> parameterDetails = ParameterDetailHelper.getAllParameterDetailsOfStripDetails();
					SerialComm.readData(StripDetailHelper.getTotalCount(),parameterDetails);		
				}
			});
		} else {
			trayPanel.add(readBt);
		}
		if(ReaderComponents.getMode().equals("ELISA")){
			blankTestBt = ReaderComponents.getButton(BLANK_TEST, trayPanel, 80 ,20);
			blankTestBt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					SerialComm.sendBlankTest();
				}
			});
		}
		JButton resetBt = ReaderComponents.getButton("Reset", trayPanel, 80 ,20);
		resetBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(ReaderComponents.yesOrNoErrorMsg(ErrorMessageEnum.RESET_STRIP)){
					StripDetailHelper.deleteAllRecords();
					TrayWindowView.getInstance().designWindow();
					MainWindow.frame.validate();
				}
			}
		});
		
		loadIncubator();
		
		System.gc();
		return trayPanel;
	}
	
	public void loadIncubator(){

		ReaderComponents.getSeparator(SwingConstants.HORIZONTAL, trayPanel, 200, 5);

		ReaderComponents.getLabel("Incubator", trayPanel, 190, 16);
		incubatorPnl = ReaderComponents.getContentPanel();
		incubatorPnl.setPreferredSize(new Dimension(190, 110));
		incubatorPnl.setBackground(Color.lightGray);
		incubatorPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ReaderComponents.setFlowLayout(incubatorPnl);
		
		

//		ReaderComponents.getLabel("HH",incubatorPnl,30, 30);
//		ReaderComponents.getLabel("MM",incubatorPnl,30, 30);
//		ReaderComponents.getLabel("Time",incubatorPnl,30 - 50);
		ReaderComponents.getLabelWithTxtFont("Time(HH:MM)",incubatorPnl,90, 15);
		timeTxtHH = ReaderComponents.getTextField(incubatorPnl,30);
		ReaderComponents.getLabel(":",incubatorPnl,5);
		timeTxtMM = ReaderComponents.getTextField(incubatorPnl,30);

		ReaderComponents.getLabelWithTxtFont("Temp",incubatorPnl,35,15);
		tempTxt = ReaderComponents.getTextField(incubatorPnl,60);
		
		loadBtn = ReaderComponents.getButton("Load", incubatorPnl, 70 , 25);
		loadBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int timeHH = timeTxtHH.getText().trim().equals("")?0:Integer.parseInt(timeTxtHH.getText());
				int timeMM = timeTxtMM.getText().trim().equals("")?0:Integer.parseInt(timeTxtMM.getText());
				int temp = tempTxt.getText().trim().equals("")?0:Integer.parseInt(tempTxt.getText());
				int calib = calibTxt.getText().trim().equals("")?0:Integer.parseInt(calibTxt.getText());
				SerialComm.sendIncubatorData(timeHH, timeMM, temp, calib);
			}
		});

		ReaderComponents.getLabelWithTxtFont("Calib",incubatorPnl,35,15);
		calibTxt = ReaderComponents.getTextField(incubatorPnl,60);

		ReaderComponents.getButton("Clear All", incubatorPnl, 70 , 25);
		trayPanel.add(incubatorPnl);
	}
	
	public void loadStripImg(JLabel roundImg){
		roundImg.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/strips/stripB"+ (currentStrip +1) +".png")));
	}
	
	public JPanel createStripNoPanel(final int number){
		StripDetailBO stripdetail = null;
		for(StripDetailBO strips : allStripDetailsList){
			if(strips.getStripId() == number){
				stripdetail = strips;
				break;
			}
		}
		if(stripdetail == null){
			stripdetail = new StripDetailBO(true);
		}
		JPanel stripNoPanel = ReaderComponents.getContentPanel();
		stripNoPanel.setPreferredSize(new Dimension(20, 20));
		stripNoPanel.setBackground(null);
		if(!stripdetail.isSelfObject()){
			stripNoPanel.setBackground(stripdetail.getStripStatus() == StripStatusEnum.FINISHED ? Color.GREEN : stripdetail.getTestType().getColor());
		}
		stripNoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		ReaderComponents.setFlowLayoutPadding(0, stripNoPanel, 4, 4);
		JLabel numberImg = ReaderComponents.getLabel(String.valueOf(number), stripNoPanel, 11, 11);
		numberImg.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(number <= calibrationStrips){
					currentStrip = 0;
				} else {
					currentStrip = number-1;
				}
				loadStripImg(roundImg);
				getStripDetailPanel();
				MainWindow.frame.validate();
			}
		});
		stripNoPanel.add(numberImg);
		return stripNoPanel;
	}
	
	public JPanel getStripDetailPanel(){
		if(stripDetailPnl.getComponentCount() > 0){
			stripDetailPnl.removeAll();
		} 
		ReaderComponents.getLabel("Type :", stripDetailPnl, 70, 16);//40
		
		//Add Test type combobox
		getCurrentStripRecord(currentStrip);
		String[] testType_0 = { TestTypeEnum.SAMPLE.getValue(), TestTypeEnum.CALIBRATION.getValue(), TestTypeEnum.CONTROL.getValue() };
		String[] testType_All = { TestTypeEnum.SAMPLE.getValue(), TestTypeEnum.CONTROL.getValue() };
		if(currentStrip == 0){
			testTypeCmb = ReaderComponents.getComboBox(testType_0, stripDetailPnl, 120);//110
			testTypeCmb.setSelectedIndex(commonStripDetail.getTestType().getKey());
		} else {
			testTypeCmb = ReaderComponents.getComboBox(testType_All, stripDetailPnl, 120);//110
			testTypeCmb.setSelectedIndex(commonStripDetail.getTestType().getKey()== 0 ? 0 : 1);
		}
		testTypeCmb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!commonStripDetail.isSelfObject() && !ReaderComponents.yesOrNoErrorMsg(ErrorMessageEnum.TEST_TYPE_ALREADY_SELECTED)){
					return;
				}
				commonStripDetail = new StripDetailBO(currentStrip+1l , TestTypeEnum.getEnumByValue(testTypeCmb.getSelectedItem().toString()), true);
				getSelectedViewPanel();
				MainWindow.frame.validate();
			}
		});
		
		ReaderComponents.getLabel("", stripDetailPnl, 100, 5);
		
		getSelectedViewPanel();
		return stripDetailPnl;
	}
	
	public JPanel getSelectedViewPanel(){
		if(selectedStripDetail != null){
			stripDetailPnl.remove(selectedStripDetail);
		}
		selectedStripDetail = ReaderComponents.getContentPanel();
		selectedStripDetail.setPreferredSize(new Dimension(190, 95));//150
		selectedStripDetail.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ReaderComponents.setFlowLayoutPadding(0, selectedStripDetail, 2, 5);
		String selectedValue = testTypeCmb.getSelectedItem().toString();
		if(selectedValue.equals(TestTypeEnum.SAMPLE.getValue())){
			getSampleView(selectedStripDetail);
		} else if(selectedValue.equals(TestTypeEnum.CALIBRATION.getValue())){
			getCalibrationView(selectedStripDetail);
		} else if(selectedValue.equals(TestTypeEnum.CONTROL.getValue())) {
			getControlView(selectedStripDetail);
		}
		stripDetailPnl.add(selectedStripDetail);
		return selectedStripDetail;
	}
	
	public JPanel getSampleView(JPanel selectedStripDetail){
//		ReaderComponents.getLabel("",selectedStripDetail,LABEL_WIDTH + 100, 5);
		ReaderComponents.getLabel("Patient ID  :",selectedStripDetail,LABEL_WIDTH + 55, 15);
		final JTextField patIdTxt = ReaderComponents.getTextField(selectedStripDetail,95);
		if(commonStripDetail.getPatientId() != null){
			patIdTxt.setText(commonStripDetail.getPatientId());
		}
//		ReaderComponents.getLabel("",selectedStripDetail,LABEL_WIDTH + 50, 5);
		
		ReaderComponents.getLabel("Parameter :",selectedStripDetail,LABEL_WIDTH + 55, 15);
		final JTextField parameterIdTxt = ReaderComponents.getTextField(selectedStripDetail, 70);
		parameterIdTxt.setEnabled(false);
		if(commonStripDetail.getParameterDetail() != null){
			parameterIdTxt.setText(String.valueOf(commonStripDetail.getParameterDetail().getParameterName()));
		}
		
		JButton bt = ReaderComponents.getButton("prmBtn", selectedStripDetail, 20, 20);
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ParameterModalView.getInstance().showParameterModal(false, parameterIdTxt, stripParameterDetailBO);
			}
		});
		JButton addBt = ReaderComponents.getButton("Add", selectedStripDetail, 60, 20);
		addBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(calibrationStrips > 0 && currentStrip < calibrationStrips){
					StripDetailHelper.deleteAllCalibrationRecords();
					calibrationStrips = 0;
				}
				StripDetailBO stripDetail = new StripDetailBO(true);
				stripDetail.setStripId(currentStrip + 1l);
				stripDetail.setTestType(TestTypeEnum.SAMPLE);
				stripDetail.setParameterId(stripParameterDetailBO.getParameterId());
				stripDetail.setPatientId(patIdTxt.getText());
				StripDetailHelper.addStrip(stripDetail);
				TrayWindowView.getInstance().designWindow();
				MainWindow.frame.validate();
			}
		});
		return selectedStripDetail;
	}
	
	public JPanel getCalibrationView(JPanel selectedStripDetail){
		ButtonGroup buttonGrp = new ButtonGroup();
		masterCalib = ReaderComponents.getRadioButton("Master", buttonGrp,selectedStripDetail, LABEL_WIDTH + 50);
		twoPointCalib = ReaderComponents.getRadioButton("Two Point", buttonGrp,selectedStripDetail, LABEL_WIDTH + 70);
		if(commonStripDetail.getCalibrationType() != null && commonStripDetail.getCalibrationType() == CalibrationTypeEnum.TWO_POINT){
			twoPointCalib.setSelected(true);
		}else{
			masterCalib.setSelected(true);
		}
		
		
		ReaderComponents.getLabel("Parameter",selectedStripDetail,LABEL_WIDTH + 70, 15);
		final JTextField parameterIdTxt = ReaderComponents.getTextField(selectedStripDetail, 60);
		parameterIdTxt.setEnabled(false);
		if(commonStripDetail.getParameterDetail() != null){
			parameterIdTxt.setText(String.valueOf(commonStripDetail.getParameterDetail().getParameterName()));
		}

		masterCalib.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parameterIdTxt.setText("");
				MainWindow.frame.validate();
			}
		});
		twoPointCalib.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parameterIdTxt.setText("");
				MainWindow.frame.validate();
			}
		});
		JButton bt = ReaderComponents.getButton("prmBtn", selectedStripDetail, 20, 20);
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ParameterModalView.getInstance().showParameterModal(masterCalib.isSelected(), parameterIdTxt, stripParameterDetailBO);
			}
		});
		JButton addBt = ReaderComponents.getButton("Add", selectedStripDetail, 60, 20);
		addBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				StripDetailBO stripDetail = new StripDetailBO(true);
				stripDetail.setTestType(TestTypeEnum.CALIBRATION);
				stripDetail.setParameterId(stripParameterDetailBO.getParameterId());
				if(masterCalib.isSelected()){
					stripDetail.setCalibrationType(CalibrationTypeEnum.MASTER);
					calibrationStrips = stripParameterDetailBO.getNoOfStd();
				}else{
					stripDetail.setCalibrationType(CalibrationTypeEnum.TWO_POINT);
					calibrationStrips = 2;
				}
				for(long i = 1; i <= calibrationStrips ; i++){
					stripDetail.setStripId(currentStrip + i);
					StripDetailHelper.addStrip(stripDetail);
				}
				TrayWindowView.getInstance().designWindow();
				MainWindow.frame.validate();
			}
		});
		return selectedStripDetail;
	}
	
	public JPanel getControlView(JPanel selectedStripDetail){
		ReaderComponents.getLabel("",selectedStripDetail,LABEL_WIDTH + 50, 5);
		ReaderComponents.getLabel("Control :",selectedStripDetail,LABEL_WIDTH + 70, 20);
		final JTextField controlIdTxt = ReaderComponents.getTextField(selectedStripDetail, 120);
		controlIdTxt.setEnabled(false);
		if(commonStripDetail != null && commonStripDetail.getControlId() != null && commonStripDetail.getControlId() > 0){
			controlIdTxt.setText(String.valueOf(commonStripDetail.getControlId()));
		}
		
		JButton bt = ReaderComponents.getButton("controlBtn", selectedStripDetail, 20, 20);
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ParameterModalView.getInstance().showParameterModal(false, controlIdTxt, stripParameterDetailBO);
			}
		});
		ReaderComponents.getLabel("",selectedStripDetail,LABEL_WIDTH + 100, 5);
		
		
		JButton addBt = ReaderComponents.getButton("Add", selectedStripDetail, 60, 20);
		addBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: control work to be done here
			}
		});
		return selectedStripDetail;
	}
}
