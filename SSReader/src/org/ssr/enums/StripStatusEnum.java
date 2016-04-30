package org.ssr.enums;

public enum StripStatusEnum {
	YET_TO_START(0,"YET TO START"),
	FINISHED(1,"FINISHED");
	
	int id;
	String value;
	
	
	public int getKey() {
		return id;
	}
	public String getValue() {
		return value;
	}

	StripStatusEnum(int key, String value){
		this.id = key;
		this.value = value;
	}
	
	public static StripStatusEnum getEnumByKey(int id){
		for(StripStatusEnum status : StripStatusEnum.values()){
			if(status.getKey() == id){
				return status;
			}
		}
		return null;
	}
	
}
