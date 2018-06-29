package net.freelancertech.journal.app.model;

import java.io.Serializable;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class DelegueDTO implements Serializable {   
    private static final long serialVersionUID = -6237000517345221310L;
     
    
    private String telephone;   
    private String nom;
    private boolean isadmin;

    public DelegueDTO() {
    }

    public DelegueDTO(String telephone, String nom) {
        this.telephone = telephone;
        this.nom = nom;
    }

    public DelegueDTO(String telephone, String nom, boolean isadmin) {
        this.telephone = telephone;
        this.nom = nom;
        this.isadmin = isadmin;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }



}
