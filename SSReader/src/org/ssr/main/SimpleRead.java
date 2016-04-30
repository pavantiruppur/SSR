package org.ssr.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import javax.swing.JOptionPane;

import org.ssr.common.GXUtil;
import org.ssr.common.ReadPropertyFile;
import org.ssr.view.MainWindow;



public class SimpleRead implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;
    public static SimpleRead reader = null;
    
    InputStream inputStream;
    OutputStream outputStream;
    SerialPort serialPort;
    Thread readThread;
    ByteBuffer data = null;
    byte[] storeByte = new byte[183]; 
    int storeIndex = 0;
    ArrayList<ByteBuffer> storesList = new ArrayList<ByteBuffer>();

    public static void communicateToMachine(){
    	if(reader != null){
    		reader.serialPort.close();
    	}
        portList = CommPortIdentifier.getPortIdentifiers();
        boolean isPortAvailable = false;
        String comPort = ReadPropertyFile.getSettingFileObj().getProperty("comPort");
        String ports = "Start ";
		if (!GXUtil.isBlank(comPort)) {
			while (portList.hasMoreElements()) {
				portId = (CommPortIdentifier) portList.nextElement();
				ports += portId.getName() + " ";
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (portId.getName().equals(comPort)) {
						reader = new SimpleRead();
						isPortAvailable = true;
					}
				}
			}
		}
        
        if(!isPortAvailable){
        	JOptionPane.showMessageDialog(MainWindow.frame, "Machine Communication Error");
        }
    }

    public SimpleRead() {
        try {
        	if(serialPort!=null){
        		portId = null;
        		serialPort.close();
        	}
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            System.out.println("-"+ serialPort.getInputBufferSize() +"-");
        } catch (IOException e) {System.out.println(e);}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {System.out.println(e);}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {}

    public void sendDataIn(){
    	try {
			outputStream.write('b');
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void sendDataOut(){
    	try {
			outputStream.write('a');
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void readData(int count){
    	try {
			outputStream.write('c');
			outputStream.write(count);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void sendIncubatorData(int timeHH,int timeMM,int temp,int calib){
    	try {
			outputStream.write('I');
			outputStream.write(timeHH);
			outputStream.write(timeMM);
			outputStream.write(temp);
			outputStream.write(calib);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            byte[] readBuffer = new byte[183];
            try {
            	int size = inputStream.available();
                while (inputStream.available() > 0) {
                    inputStream.read(readBuffer);
                }
                for(int i=0;i<size;i++){
					System.out.println(readBuffer[i]);
					if(readBuffer[i] == 2){
						storeIndex = 0;
						storeByte = new byte[183]; 
						storeByte[storeIndex] = readBuffer[i];
					} else{
						storeIndex++;
						storeByte[storeIndex] = readBuffer[i];
					}
					
					if(storeByte[storeIndex] == 3){
						System.out.println("Done");
						data = ByteBuffer.wrap(storeByte);
						storesList.add(data);
						MainWindow.getInstance().showData(data);
						storeIndex = 0;
						storeByte = new byte[183]; 
					}
					
					if(readBuffer[i] == 4 && !storesList.isEmpty() ){
						for(ByteBuffer dat : storesList){
//							resultViewModule.insertResultData(dat);
						}
						storesList = new ArrayList<ByteBuffer>();
					}
                }
            } catch (IOException e) {
            	System.out.println(e);
            } 
            break;
        }
    }
}