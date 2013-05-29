/*
 * LocationFragment 한 열에 표시할 location
 */
package com.example.bacassample3.tab2;

public class LocationEntity {
	String lat;
	String lng;
	String address;
	
	public LocationEntity(String lat, String lng, String address) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.address = address;
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
}