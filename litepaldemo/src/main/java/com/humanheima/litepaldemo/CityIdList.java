package com.humanheima.litepaldemo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/10.
 */
public class CityIdList {

    /**
     * city_info : [{"city":"南子岛","cnty":"中国","id":"CN101310230","lat":"11.26","lon":"114.20","prov":"海南"},{"city":"北京","cnty":"中国","id":"CN101010100","lat":"39.904000","lon":"116.391000","prov":"直辖市"}]
     * status : ok
     */

    private String status;
    /**
     * city : 南子岛
     * cnty : 中国
     * id : CN101310230
     * lat : 11.26
     * lon : 114.20
     * prov : 海南
     */

    @SerializedName("city_info")
    private List<CityInfo> cityInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CityInfo> getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(List<CityInfo> cityInfo) {
        this.cityInfo = cityInfo;
    }

    public static class CityInfo {
        private String city;
        private String cnty;
        @SerializedName("id")
        private String weatherId;
        private String lat;
        private String lon;
        private String prov;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCnty() {
            return cnty;
        }

        public void setCnty(String cnty) {
            this.cnty = cnty;
        }

        public String getWeatherId() {
            return weatherId;
        }

        public void setWeatherId(String weatherId) {
            this.weatherId = weatherId;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getProv() {
            return prov;
        }

        public void setProv(String prov) {
            this.prov = prov;
        }
    }
}
