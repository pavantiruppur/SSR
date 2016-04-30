package org.ssr.module;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.ResultDetailBO;
import org.ssr.bo.StandardDetailsBO;
import org.ssr.bo.StripDetailBO;
import org.ssr.common.ReadPropertyFile;
import org.ssr.components.ReaderComponents;
import org.ssr.converthelper.ParameterDetailHelper;
import org.ssr.converthelper.ResultDetailHelper;
import org.ssr.converthelper.StandardDetailHelper;
import org.ssr.converthelper.StripDetailHelper;
import org.ssr.enums.CalibrationTypeEnum;
import org.ssr.enums.ErrorMessageEnum;
import org.ssr.enums.StripStatusEnum;
import org.ssr.enums.TestTypeEnum;
import org.ssr.view.CalibrationModalView;
import org.ssr.view.MainWindow;
import org.ssr.view.TrayWindowView;

public class CommunicationModule {

	List<StripDetailBO> stripDetailList = new ArrayList<StripDetailBO>();
	ParameterDetailsBO parameterDetail = null;
	
	public void rluUpdation(ByteBuffer data, long stripNo, int modeLimit){
		StripDetailBO stripDetail = StripDetailHelper.getStripDetail(stripNo);
		if(stripDetail == null || stripDetail.getStripStatus() == StripStatusEnum.FINISHED){
			return;
		}
		
		String title = "";
		for (int i = 0; i < modeLimit; i++) {
			title += (char) ((int) data.get(i) & 0xff);
		}
		if (ReadPropertyFile.getSettingFileObj().getProperty("userResEnable").trim().equals("1")) {
			title = ReadPropertyFile.getSettingFileObj().getProperty("res"+ stripNo);
		}
		System.out.println(title);
		double rlu = 0;
		try{
			rlu = Double.parseDouble(title);
			if(MainWindow.mode.equals("CLIA")){
				rlu = rlu * Integer.parseInt(ReadPropertyFile.getSettingFileObj().getProperty("cliaMultiplicationValue"));
			}
		} catch (Exception e) {
			ReaderComponents.showErrorMsg(ErrorMessageEnum.INVALID_RLUVALUE);
			return;
		}

		//update in stripdetail
		stripDetail.setRlu(rlu);
		stripDetail.setStripStatus(StripStatusEnum.FINISHED);
		StripDetailHelper.updateStrip(stripDetail);
		
		if(stripDetail.getTestType() == TestTypeEnum.SAMPLE){
			insertResultData(rlu, stripDetail);
		} else if(stripDetail.getTestType() == TestTypeEnum.CALIBRATION 
				&& stripDetail.getCalibrationType() == CalibrationTypeEnum.MASTER){
			calibrateData(stripNo, stripDetail);
		} else if(stripDetail.getTestType() == TestTypeEnum.CALIBRATION 
				&& stripDetail.getCalibrationType() == CalibrationTypeEnum.TWO_POINT){
			System.exit(0);
		} else if(stripDetail.getTestType() == TestTypeEnum.CONTROL){
			
		}
		JPanel vesselPanel = TrayWindowView.getInstance().vesselPanel.get((int)stripNo-1);
		if(vesselPanel != null){
			vesselPanel.setBackground(Color.GREEN);
		}
		MainWindow.frame.validate();
	}

	public void insertResultData(double rlu, StripDetailBO stripDetail){
		List<StandardDetailsBO> standardDetailList = StandardDetailHelper.getStandardDetailForParameterId(stripDetail.getParameterId());
		int minId = 0, maxId = standardDetailList.size()-1;
		for(int i=0;i<standardDetailList.size(); i++){
			StandardDetailsBO stdDetail_1 = standardDetailList.get(i);
			StandardDetailsBO stdDetail_2 = standardDetailList.get(i);
			if(i+1<standardDetailList.size()){
				stdDetail_2 = standardDetailList.get(i+1);
			}
			if(stdDetail_1.getRluValue() < rlu && stdDetail_2.getRluValue() >= rlu){
				minId = i;
				maxId = i + 1;
			} else if(stdDetail_2.getRluValue() < rlu && stdDetail_1.getRluValue() >= rlu){
				maxId = i;
				minId = i + 1;
			} 
		}
		ResultDetailBO resultDetail = new ResultDetailBO();
		Double conc = calculateConc(standardDetailList.get(minId), standardDetailList.get(maxId), rlu);
		resultDetail.setConc(conc);
//		concList.add(Double.parseDouble(dc_conc.format(conc)));
		resultDetail.setParameterId(stripDetail.getParameterId());
		resultDetail.setPatientId(stripDetail.getPatientId());
		resultDetail.setRlu(rlu);
		resultDetail.setSampleType(stripDetail.getTestType());
		ResultDetailHelper.addResult(resultDetail);
		MainWindow.getInstance().resultTab.getPatientDetailTable("");
	}
	
	public double calculateConc(StandardDetailsBO stdDetail_1, StandardDetailsBO stdDetail_2, double rlu){
		double x1 = stdDetail_1.getRluValue(), x2 = stdDetail_2.getRluValue(), x = 0.0;
		double y1 = stdDetail_1.getStdValue(), y2 = stdDetail_2.getStdValue(), y = 0.0;
		double m = 0.0;
		double c = 0.0;
		//Y = mx + c
		x = x1 - x2;
		y = y1 - y2;
		m = y / x;
		if(Double.isNaN(m) || Double.isInfinite(m)){
			m = 0;
		}
		c = y1 -(m * x1);
		
		x = rlu;
		y = (m * x) + c;
		return y<0.0?0.0:y;
	}

	public void calibrateData(long stripNo, StripDetailBO stripDetail){
		if(stripNo == 1){
			parameterDetail = ParameterDetailHelper.getParameterDetail(stripDetail.getParameterId(), false);
			stripDetailList = new ArrayList<StripDetailBO>();
		}
		if(stripNo < parameterDetail.getNoOfStd()){
			stripDetailList.add(stripDetail);
			return;
		} 
		if(stripNo == parameterDetail.getNoOfStd()){
			stripDetailList.add(stripDetail);
			new CalibrationModalView().showCalibConfirmPanel(stripDetailList, parameterDetail);
			stripDetailList = new ArrayList<StripDetailBO>();
		}
	}
	
	public void incubatorUpdate(ByteBuffer data){
		
	}
	
	public void trayPos(ByteBuffer data){
		TrayWindowView trayWindow = TrayWindowView.getInstance();
		trayWindow.trayInOutbt.setEnabled(true);
		if((char) ((int) data.get(0) & 0xff) == 'I'){
			trayWindow.trayInOutbt.setText(TrayWindowView.TRAY_OUT);
			trayWindow.readBt.setEnabled(false);
		} else if((char) ((int) data.get(0) & 0xff) == 'O'){
			trayWindow.trayInOutbt.setText(TrayWindowView.TRAY_IN);
			trayWindow.readBt.setEnabled(true);
		}
	}
}
