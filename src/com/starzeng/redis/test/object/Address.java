package com.starzeng.redis.test.object;

public class Address {

	private Integer id;
	private Integer no;
	private String adderss;
	private String fool;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getAdderss() {
		return adderss;
	}

	public void setAdderss(String adderss) {
		this.adderss = adderss;
	}

	public String getFool() {
		return fool;
	}

	public void setFool(String fool) {
		this.fool = fool;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", no=" + no + ", adderss=" + adderss + ", fool=" + fool + "]";
	}

}
