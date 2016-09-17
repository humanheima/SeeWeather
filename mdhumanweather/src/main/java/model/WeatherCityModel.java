package model;

/**城市的模型
 * @author dumingwei
 *
 */
public class WeatherCityModel {

	/*
	 * "weaid": "1",
	 * "citynm": "����",
	 * "cityno": "beijing",
	 * "cityid": "101010100"
	 */
	private int id;
	private String weaid;
	private String citynm;
	private String cityno;
	private String cityid;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWeaid() {
		return weaid;
	}

	public void setWeaid(String weaid) {
		this.weaid = weaid;
	}

	public String getCitynm() {
		return citynm;
	}

	public void setCitynm(String citynm) {
		this.citynm = citynm;
	}

	public String getCityno() {
		return cityno;
	}

	public void setCityno(String cityno) {
		this.cityno = cityno;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

}
