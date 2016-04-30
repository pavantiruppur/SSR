package org.ssr.enums;

public enum ErrorMessageEnum {
	
	//Tray Panel errors 100 -
	TEST_TYPE_ALREADY_SELECTED(100,"Strip already loaded. Do you want to change ?"),
	RESET_STRIP(101,"Are you sure you want to reset ?"),
	INVALID_RLUVALUE(101,"Invalid RLU value");
	
	int id;
	String value;
	
	
	public int getKey() {
		return id;
	}
	public String getValue() {
		return value;
	}

	ErrorMessageEnum(int key, String value){
		this.id = key;
		this.value = value;
	}

	
	public static ErrorMessageEnum getEnumByKey(int id){
		for(ErrorMessageEnum testType : ErrorMessageEnum.values()){
			if(testType.getKey() == id){
				return testType;
			}
		}
		return null;
	}
	
	public static ErrorMessageEnum getEnumByValue(String value){
		for(ErrorMessageEnum testType : ErrorMessageEnum.values()){
			if(testType.getValue() == value){
				return testType;
			}
		}
		return null;
	}
}
