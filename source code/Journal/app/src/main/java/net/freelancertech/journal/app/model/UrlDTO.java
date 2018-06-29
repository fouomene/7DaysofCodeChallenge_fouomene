package net.freelancertech.journal.app.model;

import java.io.Serializable;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * Created by FOUOMENE on 06/2018.
 * EmailAuthor:  fouomenedaniel@gmail.com .
 *
 **/
public class UrlDTO implements Serializable {

    private static final long serialVersionUID = 2L;

    private String apiurl;

    private String urlapiinfoflash;
    private String urlabonnementinfoflash;
    private String urlaproposfreelancertech;
    private String urlserveurimage;
    private String urlahelpfacinfosflash;

    private String numeserveurgsmmtn;
    private String numeserveurgsmorange;

    private String intervalle;

    private String urlsendnotificationpush ;
    private String urlregisternotification ;

    private String versionurl ;

    public UrlDTO() {
    }

    public String getUrlabonnementinfoflash() {
        return urlabonnementinfoflash;
    }

    public void setUrlabonnementinfoflash(String urlabonnementinfoflash) {
        this.urlabonnementinfoflash = urlabonnementinfoflash;
    }

    public String getUrlapiinfoflash() {
        return urlapiinfoflash;
    }

    public void setUrlapiinfoflash(String urlapiinfoflash) {
        this.urlapiinfoflash = urlapiinfoflash;
    }

    public String getUrlaproposfreelancertech() {
        return urlaproposfreelancertech;
    }

    public void setUrlaproposfreelancertech(String urlaproposfreelancertech) {
        this.urlaproposfreelancertech = urlaproposfreelancertech;
    }

    public String getUrlserveurimage() {
        return urlserveurimage;
    }

    public void setUrlserveurimage(String urlserveurimage) {
        this.urlserveurimage = urlserveurimage;
    }

    public String getNumeserveurgsmorange() {
        return numeserveurgsmorange;
    }

    public void setNumeserveurgsmorange(String numeserveurgsmorange) {
        this.numeserveurgsmorange = numeserveurgsmorange;
    }

    public String getNumeserveurgsmmtn() {
        return numeserveurgsmmtn;
    }

    public void setNumeserveurgsmmtn(String numeserveurgsmmtn) {
        this.numeserveurgsmmtn = numeserveurgsmmtn;
    }

    public String getUrlahelpfacinfosflash() {
        return urlahelpfacinfosflash;
    }

    public void setUrlahelpfacinfosflash(String urlahelpfacinfosflash) {
        this.urlahelpfacinfosflash = urlahelpfacinfosflash;
    }

    public String getIntervalle() {
        return intervalle;
    }

    public void setIntervalle(String intervalle) {
        this.intervalle = intervalle;
    }

    public String getApiurl() {
        return apiurl;
    }

    public void setApiurl(String apiurl) {
        this.apiurl = apiurl;
    }

    public String getUrlsendnotificationpush() {
        return urlsendnotificationpush;
    }

    public void setUrlsendnotificationpush(String urlsendnotificationpush) {
        this.urlsendnotificationpush = urlsendnotificationpush;
    }

    public String getUrlregisternotification() {
        return urlregisternotification;
    }

    public void setUrlregisternotification(String urlregisternotification) {
        this.urlregisternotification = urlregisternotification;
    }

    public String getVersionurl() {
        return versionurl;
    }

    public void setVersionurl(String versionurl) {
        this.versionurl = versionurl;
    }

    @Override
    public String toString() {
        return "UrlDTO{" +
                "urlapiinfoflash='" + urlapiinfoflash + '\'' +
                ", urlabonnementinfoflash='" + urlabonnementinfoflash + '\'' +
                ", urlaproposfreelancertech='" + urlaproposfreelancertech + '\'' +
                ", urlserveurimage='" + urlserveurimage + '\'' +
                ", numeserveurgsmmtn='" + numeserveurgsmmtn + '\'' +
                ", numeserveurgsmorange='" + numeserveurgsmorange + '\'' +
                ", urlahelpfacinfosflash='" + urlahelpfacinfosflash + '\'' +
                ", intervalle='" + intervalle + '\'' +
                '}';
    }
}
