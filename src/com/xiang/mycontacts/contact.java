package com.xiang.mycontacts;

/**
 * data model of the contact
 * @author xiang
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
