package org.ssr.enums;

public enum FilterWheelEnum {
	F405450(1,"405-450"),
	F405630(2,"405-630"),
	F405492(3,"405-492"),
	F450405(4,"450-405"),
	F450630(5,"450-630"),
	F450492(6,"450-492"),
	F630405(7,"630-405"),
	F630450(8,"630-450"),
	F630492(9,"630-492"),
	F492405(10,"492-405"),
	F492450(11,"492-450"),
	F492630(12,"492-630"),
	F405(13,"405"),
	F450(14,"450"),
	F492(15,"492"),
	F630(16,"630");
	
	int id;
	String value;
	
	
	public int getKey() {
		return id;
	}
	public String getValue() {
		return value;
	}

	FilterWheelEnum(int key, String value){
		this.id = key;
		this.value = value;
	}
	
	public static FilterWheelEnum getEnumByKey(int id){
		if(id==0){
			return null;
		}
		for(FilterWheelEnum calibration : FilterWheelEnum.values()){
			if(calibration.getKey() == id){
				return calibration;
			}
		}
		return null;
	}
	
	public static FilterWheelEnum getEnumByKey(String value){
		for(FilterWheelEnum filter : FilterWheelEnum.values()){
			if(filter.getValue() == value){
				return filter;
			}
		}
		return null;
	}
	
}
