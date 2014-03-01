package com.xiang.mycontacts;

/**
 * 联系人数据类
 * @author HH
 * 
 */
public class contact {
	int id;
	String phonenumber;
	String name;
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
