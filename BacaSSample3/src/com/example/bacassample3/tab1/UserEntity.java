/*
 * UserFragment 한 열에 표시할 user
 */
package com.example.bacassample3.tab1;

public class UserEntity {
	private String id;					// 사용자 ID
	private String pwd;				// 패스워드
	private String mail;				// 이메일
	private String deviceid;			// 장치 ID
	
	public UserEntity(String deviceid) {
		super();
		this.id = "";
		this.pwd = "";
		this.mail = "";
		this.deviceid = deviceid;
	}
	
	public UserEntity(String id, String pwd, String mail, String deviceid) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.mail = mail;
		this.deviceid = deviceid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	
}