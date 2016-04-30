package org.ssr.main;

import java.io.File;
import java.lang.instrument.Instrumentation;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.ssr.common.ReadPropertyFile;
import org.ssr.view.MainWindow;

public class SSReaderMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		setUIFont (new javax.swing.plaf.FontUIResource(Font.MONOSPACED,Font.PLAIN,12));
//		Thread t = new MemoryTest(main);
//		t.start();
		MainWindow main = MainWindow.getInstance();
//		SimpleRead.communicateToMachine();
		String comPort = ReadPropertyFile.getSettingFileObj().getProperty("comPort");
		try {
			(new SerialComm()).connect(comPort);//"/dev/ttyACM0"
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Machine Communication Error");
			e.printStackTrace();
		}

		SerialComm.sendTrayPositionCheck();
	}
	
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	    } 

}

class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
}

class MemoryTest extends Thread{
	
	Object obj;
	
	public MemoryTest(Object obj) {
		this.obj = obj;
	}
	
	@Override
	public void run() {
		super.run();
		while(true){
			Long mb = 1024*1024l;
			
			System.out.println("Total = : " + Runtime.getRuntime().totalMemory()/(1024*1024) + " Free :" + Runtime.getRuntime().freeMemory()/(1024*1024));
			System.out.println("Available processors (cores): " + 
			        Runtime.getRuntime().availableProcessors());

			    /* Total amount of free memory available to the JVM */
			    System.out.println("Free memory (bytes): " + 
			        Runtime.getRuntime().freeMemory());

			    /* This will return Long.MAX_VALUE if there is no preset limit */
			    long maxMemory = Runtime.getRuntime().maxMemory();
			    /* Maximum amount of memory the JVM will attempt to use */
			    System.out.println("Maximum memory (bytes): " + 
			        (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

			    /* Total memory currently available to the JVM */
			    System.out.println("Total memory available to JVM (bytes): " + 
			        Runtime.getRuntime().totalMemory());

			    /* Get a list of all filesystem roots on this system */
			    File[] roots = File.listRoots();

			    /* For each filesystem root, print some info */
			    for (File root : roots) {
			      System.out.println("File system root: " + root.getAbsolutePath());
			      System.out.println("Total space (bytes): " + root.getTotalSpace());
			      System.out.println("Free space (bytes): " + root.getFreeSpace());
			      System.out.println("Usable space (bytes): " + root.getUsableSpace());
			    }
			try {
				sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
}
