package org.three60.createdb;

import java.io.File;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadDataFromXML {

	public static void main(String argv[]) {

		DBConnection db = new DBConnection();
		try {

			JFrame frame = new JFrame();
//			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setSize(1150,725);
//			String width = ReadPropertyFile.getSettingFileObj().getProperty("width");
//			frame.setSize(width!=null?Integer.parseInt(width):1150,725);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			//Create a file chooser
			final JFileChooser fc = new JFileChooser();
			//In response to a button click:
			int returnVal = fc.showOpenDialog(frame);
			
			File fXmlFile = fc.getSelectedFile();
			if(fXmlFile == null){
				JOptionPane.showMessageDialog(null, "Please choose a file");
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("tabledetail");

			System.out.println("----------------------------");


			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("Table ID : " +eElement.getAttribute("id"));
					System.out.println("Table Name : "+ eElement.getElementsByTagName("tablename").item(0).getTextContent());
					System.out.println("Create Query : "+ eElement.getElementsByTagName("createquery").item(0).getTextContent());
					try{
						db.createTable(eElement.getElementsByTagName("createquery").item(0).getTextContent());
						if(eElement.getElementsByTagName("insertquery").item(0)!=null){
							System.out.println("Create Query : "+ eElement.getElementsByTagName("createquery").item(0).getTextContent());
							db.insert(eElement.getElementsByTagName("insertquery").item(0).getTextContent());
						}
					} catch(SQLException e){
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			db.closeConnection();
			System.exit(0);
		}
	}
}
