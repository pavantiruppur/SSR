package org.ssr.enums;

public enum TabAlignment {
	RESULT_VIEW(0,"RESULT VIEW"),
	CONTROL(3,"CONTROL"),
	TREND(5,"TREND"),
	LOAD_DATA(7,"LOAD DATA"),
	PATIENT(1,"PATIENT DETAILS"), 
	PARAMETERS(2,"PARAMETERS"),
	OPTIONS(6,"OPTIONS");
	
	int id;
	String value;
	
	
	public int getKey() {
		return id;
	}
	public String getValue() {
		return value;
	}

	TabAlignment(int key, String value){
		this.id = key;
		this.value = value;
	}
	
}
