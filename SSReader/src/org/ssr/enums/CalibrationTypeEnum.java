package org.ssr.enums;

public enum CalibrationTypeEnum {
	MASTER(1,"MASTER"),
	TWO_POINT(2,"TWO_POINT");
	
	int id;
	String value;
	
	
	public int getKey() {
		return id;
	}
	public String getValue() {
		return value;
	}

	CalibrationTypeEnum(int key, String value){
		this.id = key;
		this.value = value;
	}
	
	public static CalibrationTypeEnum getEnumByKey(int id){
		for(CalibrationTypeEnum calibration : CalibrationTypeEnum.values()){
			if(calibration.getKey() == id){
				return calibration;
			}
		}
		return null;
	}
	
	public static CalibrationTypeEnum getEnumByKey(String value){
		for(CalibrationTypeEnum calibration : CalibrationTypeEnum.values()){
			if(calibration.getValue() == value){
				return calibration;
			}
		}
		return null;
	}
	
}
