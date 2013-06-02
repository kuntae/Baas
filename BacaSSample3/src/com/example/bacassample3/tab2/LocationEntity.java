/*
 * LocationFragment 한 열에 표시할 location
 */
package com.example.bacassample3.tab2;

public class LocationEntity {
	String lat;
	String lng;
	String address;
	String store;
	String phonenumber;
	String memo;
	
	public LocationEntity(String lat, String lng, String address) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.address = address;
		this.store = "";
		this.phonenumber = "";
		this.memo = "";
	}
	
	public LocationEntity(String lat, String lng, String address, String store, String phonenumber, String memo) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.address = address;
		this.store = store;
		this.phonenumber = phonenumber;
		this.memo = memo;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}