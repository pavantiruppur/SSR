package org.ssr.enums;

public enum ActiveCheckEnum {
	ACTIVE(0,false),
	IN_ACTIVE(1,true);
	
	int id;
	boolean value;
	
	
	public int getKey() {
		return id;
	}
	public boolean getValue() {
		return value;
	}

	ActiveCheckEnum(int key, boolean value){
		this.id = key;
		this.value = value;
	}
	
	public static ActiveCheckEnum getEnumByKey(int val){
		if(ActiveCheckEnum.ACTIVE.id == val){
			return ACTIVE;
		} else if(ActiveCheckEnum.ACTIVE.id == val){
			return IN_ACTIVE;
		}
		return IN_ACTIVE;
	}
	
	public static ActiveCheckEnum getEnumByValue(boolean val){
		if(ActiveCheckEnum.ACTIVE.value == val){
			return ACTIVE;
		} else if(ActiveCheckEnum.ACTIVE.value == val){
			return IN_ACTIVE;
		}
		return IN_ACTIVE;
	}
}
