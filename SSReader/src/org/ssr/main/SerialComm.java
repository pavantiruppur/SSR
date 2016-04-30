package org.ssr.main;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.ssr.bo.ParameterDetailsBO;
import org.ssr.bo.StripDetailBO;
import org.ssr.common.ReadPropertyFile;
import org.ssr.components.ReaderComponents;
import org.ssr.module.CommunicationModule;

public class SerialComm {
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	public static SerialWriter writer = null;
	
	public static Timer timer = null;

	public SerialComm() {
		super();
	}

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					TIME_OUT);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(DATA_RATE,
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialReader(in))).start();
				writer = new SerialWriter(out);

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/** */
	public static class SerialReader implements Runnable {
		InputStream in;

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int len = -1;
			int index = 0;
			int arrPos = 0;
			int stripNo = 1;
			char mode = 't';
		    byte[] data = new byte[6]; 
		    ByteBuffer bufferData = null;
		    int modeLimit = 0;
		    CommunicationModule communication = new CommunicationModule();
			try {
				while ((len = this.in.read(buffer)) > -1) {
					boolean isIncubator = false;
					boolean isTrayStatus = false;
					System.out.println("Length of buffer::"+ len);
	                for(int i=0;i<len;i++){
						System.out.println("value received for::" + i +" ::"+ buffer[i]);
						if(buffer[i] == 2){
							index=1;
							data = new byte[5];
							stripNo = 1;
						} else if(index == 1){
							if(buffer[i] == 'I'){
								isIncubator = true;
								modeLimit = 4;
							} else if(buffer[i] == 'T'){
								isTrayStatus = true;
								modeLimit = 1;
							} else if(buffer[i] == 'E'){
								modeLimit = 5;
							} else if(buffer[i] == 'C'){
								modeLimit = 6;
							}
							mode = (char) buffer[i];
						    data = new byte[modeLimit]; 
							index++;
							arrPos = 0;
						} else if(isIncubator && buffer[i] == 3){
							communication.incubatorUpdate(bufferData.wrap(data));
						} else if(isTrayStatus && buffer[i] == 3){
							communication.trayPos(bufferData.wrap(data));
						} else{
							System.out.println("Array pos::"+ arrPos +" value received::"+ buffer[i] +" mode limit::"+ modeLimit
									+" condition::"+ !isIncubator +" "+ !isTrayStatus +" "+ (arrPos == modeLimit) +" "+ (stripNo <= 8));
							data[arrPos++] = buffer[i];
							if(!isIncubator && !isTrayStatus && arrPos == modeLimit && stripNo <= 8){
								//send data to resultdata
								bufferData = ByteBuffer.wrap(data);
								communication.rluUpdation(bufferData, stripNo++, modeLimit);
								arrPos = 0;
								i++;
							}
						}
	                }
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** */
	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			try {
				int c = 0;
				while ((c = System.in.read()) > -1) {
					this.out.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void write(String data){
			try {
				this.out.write(data.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void write(int data){
			try {
				this.out.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    public static void selectMode(){
		if(ReaderComponents.getMode().equals("ELISA")){
			writer.write('E');
		}else{
			writer.write('C');
		}
    }
    
    public static void sendDataIn(){
		selectMode();
		writer.write('b');
    }
    
    public static void sendDataOut(){
		selectMode();
		writer.write('a');
		if(timer != null){
			timer.cancel();
			timer = null;
		}
    }

    public static void sendBlankTest(){
		writer.write("MBTS");
    }

    public static void sendTrayPositionCheck(){
		writer.write("MTS");
    }

    public static void sendIncubatorData(int timeHH,int timeMM,int temp,int calib){
		writer.write('I');
		writer.write(timeHH);
		writer.write(timeMM);
		writer.write(temp);
		writer.write(calib);
    }
    
    public static void readData(int count, List<ParameterDetailsBO> parameterDetails){
		selectMode();
		writer.write('c');
		writer.write(count);
		if(parameterDetails != null){
			for(ParameterDetailsBO parameterDetail : parameterDetails){
				writer.write(parameterDetail.getFilterWheel().getKey());
			}
		}
		timer = new Timer();
		int time = Integer.parseInt(ReadPropertyFile.getSettingFileObj().getProperty("trayInTimeAfterRead"));
		timer.schedule(new SerialComm.TrayInTask(), time);
    }
    
	public static void main(String[] args) {
		try {
			(new SerialComm()).connect("COM14");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static class TrayInTask extends TimerTask{

		@Override
		public void run() {
			JOptionPane.showOptionDialog(null, "Tray In process will be initiastiated in 15 secs","", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {}
			if(timer != null){
				SerialComm.sendDataIn();
			}
		}
	}
}
