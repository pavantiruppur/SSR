package org.ssr.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {
	public static Properties getSettingFileObj(){
		Properties propertyFile = new Properties();
		try {
			propertyFile.load(new FileInputStream("res/setting.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propertyFile;
	}
	
	public static void saveSettingFileObj(Properties properties){
		try {
			properties.store(new FileOutputStream("res/setting.properties"),null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}