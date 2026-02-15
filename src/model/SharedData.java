package model;

public class SharedData {
	private static SharedData instance;
	private String data;
	private String data2;
	private String data3;
	
	private SharedData() {}
	
	public static SharedData getInstance() {
		if(instance == null) {
			instance = new SharedData();
		}
		return instance;
	}
	public static SharedData getSecIntance() {
		if(instance == null) {
			instance = new SharedData();
		}
		return instance;		
	}
	public static SharedData getThIntance() {
		if(instance == null) {
			instance = new SharedData();
		}
		return instance;		
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getData2() {
		return data2;
	}
	public void setData2(String data) {
		this.data2 = data;
	}
	public String getData3() {
		return data3;
	}
	public void setData3(String data) {
		this.data3 = data;
	}
}
