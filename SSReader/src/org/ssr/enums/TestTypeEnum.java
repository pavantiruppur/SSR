package org.ssr.enums;

import java.awt.Color;

public enum TestTypeEnum {
	SAMPLE(0,"SAMPLE", Color.pink),
	CALIBRATION(1,"CALIBRATION", Color.GRAY),
	CONTROL(2,"CONTROL", Color.RED);
	
	int id;
	String value;
	Color color;
	
	
	public int getKey() {
		return id;
	}
	public String getValue() {
		return value;
	}
	public Color getColor() {
		return color;
	}

	TestTypeEnum(int key, String value, Color color){
		this.id = key;
		this.value = value;
		this.color = color;
	}
	
	public static TestTypeEnum getEnumByKey(int id){
		for(TestTypeEnum testType : TestTypeEnum.values()){
			if(testType.getKey() == id){
				return testType;
			}
		}
		return null;
	}
	
	public static TestTypeEnum getEnumByValue(String value){
		for(TestTypeEnum testType : TestTypeEnum.values()){
			if(testType.getValue() == value){
				return testType;
			}
		}
		return null;
	}
}
