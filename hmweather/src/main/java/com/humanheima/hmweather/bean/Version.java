package com.humanheima.hmweather.bean;

/**
 * Created by Administrator on 2016/9/26.
 */

public class Version {
    /**
     * name : HMWeather
     * version : 1
     * changelog : First release
     * updated_at : 1474867450
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/57e8b0e8959d690950000b28?download_token=b7efd3716aeec2a41ddb9907b0ee01c0
     * install_url : http://download.fir.im/v2/app/install/57e8b0e8959d690950000b28?download_token=b7efd3716aeec2a41ddb9907b0ee01c0
     * direct_install_url : http://download.fir.im/v2/app/install/57e8b0e8959d690950000b28?download_token=b7efd3716aeec2a41ddb9907b0ee01c0
     * update_url : http://fir.im/719e
     * binary : {"fsize":4054722}
     */

    private String name;
    private String version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    /**
     * fsize : 4054722
     */

    private BinaryBean binary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public static class BinaryBean {
        private int fsize;

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }
    }

}
