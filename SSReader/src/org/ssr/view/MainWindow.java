package org.ssr.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ssr.common.ReadPropertyFile;
import org.ssr.components.ReaderComponents;
import org.ssr.datacommunication.SimpleRead;
import org.ssr.enums.TabAlignment;
import org.ssr.main.SerialComm;
import org.ssr.module.ControlDetailModule;
import org.ssr.module.ParameterDetailModule;
import org.ssr.module.PatientDetailModule;
import org.ssr.module.ResultDetailModule;

public class MainWindow {
	
	public static JFrame frame = null;
	public static JPanel mainPanel, contentPanel;
	public static JTabbedPane tabPane;
	public static MainWindow mainWindow = null;
	public static String mode = null;
	public TabpanelView resultTab = null;
	public TabpanelView patientTab = null;
	public TabpanelView parameterTab = null;
	public TabpanelView controlTab = null;
	public JPanel resultTabView = null;
	public JPanel patientTabView = null;
	public JPanel parameterTabView = null;
	public JPanel controlTabView = null;
	ChangeListener chg = null;
	JLabel tempValue = null;
	JMenuBar menuBar;
	JMenu fileMenu;
	JButton cliaBt =null, elisaBt = null;
	
	public MainWindow() {
		mode = ReadPropertyFile.getSettingFileObj().getProperty("mode");
		designWindow();
		String machineMode = ReadPropertyFile.getSettingFileObj().getProperty("machinemode");
		if(machineMode != null && machineMode.equalsIgnoreCase("clia")){
			elisaBt.setVisible(false);
		} else if(machineMode != null && machineMode.equalsIgnoreCase("elisa")){
			cliaBt.setVisible(false);
		}
//		SimpleRead.communicateToMachine();
	}
	
	public static MainWindow getInstance(){
		if(mainWindow == null){
			mainWindow = new MainWindow();
		}
		return mainWindow;
	}
	
	private void designWindow(){
		frame = ReaderComponents.getFrame();
		/*DisplayMode dm = new DisplayMode(1024,768, 32, 60);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

	    //When we get environment and getDegaultScreen Device, we get access to the entire monitor, not just a window
		GraphicsDevice vc = env.getDefaultScreenDevice();
		vc.setFullScreenWindow(frame);
		for ( DisplayMode availDM : vc.getDisplayModes() ){
            System.out.println(availDM.getWidth() + "x" + availDM.getHeight()+" B :"+ availDM.getBitDepth()+" R :"+ availDM.getRefreshRate());
        }
//		JOptionPane.showMessageDialog(null, vc.isDisplayChangeSupported() ? "S" : "N");
		try{
			vc.setDisplayMode(dm);
		}catch (Exception e) {
			System.out.println(e);
		}*/
//		frame.setContentPane(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("resources/background.jpg"))));
//		frame.setJMenuBar(buildMenuBar());
		mainPanel = ReaderComponents.getMainPanel();
		ReaderComponents.setFlowLayout(mainPanel);
		ReaderComponents.setBorderLayout(mainPanel);
		mainPanel.add(getToolBar(),BorderLayout.NORTH);
//		mainPanel.add(HomePageView.getInstance().getAddPatientView(),BorderLayout.LINE_START);
		buildContentPane();
		frame.add(mainPanel);
		Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		frame.setIconImage(icon);
//		frame.setIconImage(Toolkit.getDefaultToolkit()
//	    		   .getImage(getClass().getClassLoader().getResource("resources/LilacLogo.jpg")));
//		new DBConnection();
		frame.setUndecorated(true);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		frame.setVisible(true);
	}
	
	public void buildContentPane(){
		contentPanel = ReaderComponents.getMainPanel();
		ReaderComponents.setFlowLayout(contentPanel);
		contentPanel.add(TrayWindowView.getInstance().designWindow());
		contentPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		mainPanel.add(contentPanel);
		buildTabPane();
		ReaderComponents.getButton("Print", contentPanel, 60 ,40).addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(ResultDetailModule.getInstance().resultDetailTable.getModel().getRowCount() > 0){
						ResultDetailModule.getInstance().resultDetailTable.print();
					} else {
						JOptionPane.showMessageDialog(null, "Error.There is no records to print!!!");
					}
				} catch (PrinterException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	private void buildTabPane() {
		tabPane = ReaderComponents.getTabPane();
		addTabPaneComponent();
		
		chg = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				int selectedIndex = tabPane.getSelectedIndex();
				tabPane.removeChangeListener(chg);
//				tabPane.removeAll();
//				addTabPaneComponent();
				tabPane.setSelectedIndex(selectedIndex);
				tabPane.addChangeListener(chg);
			}
		};
		tabPane.addChangeListener(chg);
		
		contentPanel.add(tabPane);
	}

	private void addTabPaneComponent() {
		tabPane.setPreferredSize(new Dimension(728, 530));//adding +130 to height +110 width
		resultTab = new TabpanelView();
		resultTabView = resultTab.getTabPanel(ResultDetailModule.getInstance(),"");
		patientTab = new TabpanelView();
		patientTabView = patientTab.getTabPanel(PatientDetailModule.getInstance(),"");
		parameterTab = new TabpanelView();
		parameterTabView = parameterTab.getTabPanel(ParameterDetailModule.getInstance(),"");
		controlTab = new TabpanelView();
		controlTabView = controlTab.getTabPanel(ControlDetailModule.getInstance(),"");
		tabPane.add(TabAlignment.RESULT_VIEW.getValue(),resultTabView);
		tabPane.add(TabAlignment.PATIENT.getValue(),patientTabView);
		tabPane.add(TabAlignment.PARAMETERS.getValue(),parameterTabView);
		tabPane.add(TabAlignment.CONTROL.getValue(),controlTabView);
		//LOAD_DATA
	}
	
	public void mainWindowRepaint(){
		tabPane.remove(TabAlignment.RESULT_VIEW.getKey());
		frame.repaint();
	}
	
	public JMenuBar buildMenuBar(){
		menuBar = ReaderComponents.getMenuBar();
		fileMenu = ReaderComponents.getMainMenu(menuBar, "File");
		JMenuItem exitSubMenu = ReaderComponents.getSubMenu(fileMenu, "Exit");
		
		exitSubMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		JMenu modeMenu = ReaderComponents.getSubMainMenu(fileMenu, "Mode");
		JMenuItem elisaMenu = ReaderComponents.getSubMenu(modeMenu, "ELISA");
		
		elisaMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mode = "ELISA";
				if(mode.equals(ReadPropertyFile.getSettingFileObj().getProperty("mode"))){
					return;
				}
				try{
					SimpleRead.reader.sendDataOut();
				} catch (Exception e) {
					System.out.println("Connection to machine unavailable");
				}
				Properties propertyFile = ReadPropertyFile.getSettingFileObj();
				propertyFile.setProperty("mode", mode);
				ReadPropertyFile.saveSettingFileObj(propertyFile);
				if(MainWindow.mode.equals("ELISA")){
					mainPanel.setBackground(Color.getHSBColor(.6f,0.1f,0.9f));
					contentPanel.setBackground(Color.getHSBColor(.6f,0.1f,0.9f));
				} else{
					mainPanel.setBackground(Color.getHSBColor(.4f,0.1f,0.9f));
					contentPanel.setBackground(Color.getHSBColor(.4f,0.1f,0.9f));
				}
				frame.setTitle("Color ("+ ReadPropertyFile.getSettingFileObj().getProperty("mode") + ")");
//				HomePageView.getInstance().reset();
				MainWindow.frame.dispose();
				designWindow();
				MainWindow.frame.validate();
			}
		});
		
		JMenuItem cliaMenu = ReaderComponents.getSubMenu(modeMenu, "CLIA");
		
		cliaMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mode = "CLIA";
				if(mode.equals(ReadPropertyFile.getSettingFileObj().getProperty("mode"))){
					return;
				}
				try{
					SimpleRead.reader.sendDataOut();
				} catch (Exception e) {
					System.out.println("Connection to machine unavailable");
				}
				Properties propertyFile = ReadPropertyFile.getSettingFileObj();
				propertyFile.setProperty("mode", mode);
				ReadPropertyFile.saveSettingFileObj(propertyFile);
				if(MainWindow.mode.equals("ELISA")){
					mainPanel.setBackground(Color.getHSBColor(.6f,0.1f,0.9f));
					contentPanel.setBackground(Color.getHSBColor(.6f,0.1f,0.9f));
				} else{
					mainPanel.setBackground(Color.getHSBColor(.4f,0.1f,0.9f));
					contentPanel.setBackground(Color.getHSBColor(.4f,0.1f,0.9f));
				}
				frame.setTitle("Color ("+ ReadPropertyFile.getSettingFileObj().getProperty("mode") + ")");
//				HomePageView.getInstance().reset();
				MainWindow.frame.dispose();
				designWindow();
				MainWindow.frame.validate();
			}
		});
		
		return menuBar;
	}
	
	public JToolBar getToolBar(){
		JToolBar toolBar = ReaderComponents.getToolBar();
		toolBar.setLayout(new BorderLayout());
		
		JPanel menuPanel = ReaderComponents.getContentPanel();
		menuPanel.setOpaque(false);
		ReaderComponents.setFlowLayout(menuPanel);
		
		cliaBt = ReaderComponents.getPrimaryButton("CLIA", menuPanel, 100, 30);
		cliaBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				modeChange("CLIA");
			}
		});
		elisaBt = ReaderComponents.getPrimaryButton("ELISA", menuPanel, 100, 30);
		elisaBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				modeChange("ELISA");
			}
		});
		/*JButton incubatorBt = ReaderComponents.getPrimaryButton("INCUBATOR", menuPanel, 150, 30);
		incubatorBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				IncubatorModalView.getInstance().showParameterModal();
				SimpleRead.reader.sendIncubatorData(0, 45, 28, 0);
			}
		});*/
		
		JPanel displayPanel = ReaderComponents.getContentPanel();
		displayPanel.setOpaque(false);
		ReaderComponents.setFlowLayout(displayPanel);
		tempValue = ReaderComponents.getLabel("Temp : ", displayPanel, 150, 20);
		
		JButton closeBt = ReaderComponents.getButton("X", displayPanel, 42, 20);
		closeBt.setAlignmentX(0.0f);
		closeBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int choice = JOptionPane.showOptionDialog(null, "Choose your option", "", JOptionPane.OK_OPTION, JOptionPane.CANCEL_OPTION, null,new String[]{"Restart", "Shutdown", "Cancel"}, // this is the array
					        "default");
					if(choice == 0){
						System.out.println("Restarting machine...");
						Runtime.getRuntime().exec("/sbin/reboot");
					} else if(choice == 1){
						System.out.println("Shutting down machine...");
						Runtime.getRuntime().exec("shutdown -h now");
					}
				} catch (Exception e) {
				}
			}
		});
		
		mode = ReadPropertyFile.getSettingFileObj().getProperty("mode");
		if(MainWindow.mode.equals("ELISA")){
			elisaBt.setEnabled(false);
			cliaBt.setEnabled(true);
		} else{
			elisaBt.setEnabled(true);
			cliaBt.setEnabled(false);
		}
		
		toolBar.add(menuPanel, BorderLayout.LINE_START);
		toolBar.add(displayPanel, BorderLayout.LINE_END);
		return toolBar;
	}
	
	public void modeChange(String changingMode){
		mode = changingMode;
		if(mode.equals(ReadPropertyFile.getSettingFileObj().getProperty("mode"))){
			return;
		}
		try{
			SimpleRead.reader.sendDataOut();
		} catch (Exception e) {
			System.out.println("Connection to machine unavailable");
		}
		Properties propertyFile = ReadPropertyFile.getSettingFileObj();
		propertyFile.setProperty("mode", mode);
		ReadPropertyFile.saveSettingFileObj(propertyFile);
		if(MainWindow.mode.equals("ELISA")){
			mainPanel.setBackground(Color.getHSBColor(.6f,0.1f,0.9f));
			contentPanel.setBackground(Color.getHSBColor(.6f,0.1f,0.9f));
		} else{
			mainPanel.setBackground(Color.getHSBColor(.4f,0.1f,0.9f));
			contentPanel.setBackground(Color.getHSBColor(.4f,0.1f,0.9f));
			elisaBt.setEnabled(true);
			cliaBt.setEnabled(false);
		}
		frame.setTitle("Color ("+ ReadPropertyFile.getSettingFileObj().getProperty("mode") + ")");
//		HomePageView.getInstance().reset();
		MainWindow.frame.dispose();
		designWindow();
		SerialComm.sendTrayPositionCheck();
		MainWindow.frame.validate();
	}
	
	public void changeTempValue(ByteBuffer data){
		String degree = "";
		degree += (char) ((int) data.get(2) & 0xff);
		degree += (char) ((int) data.get(3) & 0xff);
		tempValue.setText("Temp : "+ degree + (char)186 +"C");
		MainWindow.frame.validate();
	}
	
	public void showData(ByteBuffer data){
		 String title = "";
		 int index = 1;
		 int limit = 5;
		 if((char) ((int) data.get(index++) & 0xff) == 'I'){
			 changeTempValue(data);
			 return;
		 }
	}
}
