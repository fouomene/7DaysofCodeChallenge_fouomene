package net.freelancertech.journal.app.model;

import java.io.Serializable;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */

public class InformationDTO implements Serializable {
    private static final long serialVersionUID = -6237000517345221310L;


    private long informationId;
    private String objet;
    private String message;
    private String dateEnregistrement;
    private String telephoneDelegue;
    private String nomDelegue;
    private String fichier;
    private String url;


    public InformationDTO() {
    }

    public InformationDTO(long informationId, String objet, String message, String dateEnregistrement, String telephoneDelegue, String nomDelegue, String fichier, String url) {
        this.informationId = informationId;
        this.objet = objet;
        this.message = message;
        this.dateEnregistrement = dateEnregistrement;
        this.telephoneDelegue = telephoneDelegue;
        this.nomDelegue = nomDelegue;
        this.fichier = fichier;
        this.url = url;
    }

    public long getInformationId() {
        return informationId;
    }

    public void setInformationId(long informationId) {
        this.informationId = informationId;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(String dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public String getTelephoneDelegue() {
        return telephoneDelegue;
    }

    public void setTelephoneDelegue(String telephoneDelegue) {
        this.telephoneDelegue = telephoneDelegue;
    }

    public String getNomDelegue() {
        return nomDelegue;
    }

    public void setNomDelegue(String nomDelegue) {
        this.nomDelegue = nomDelegue;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
