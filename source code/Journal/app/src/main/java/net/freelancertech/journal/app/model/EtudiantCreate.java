package net.freelancertech.journal.app.model;

import java.io.Serializable;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class EtudiantCreate implements Serializable {   
    private static final long serialVersionUID = -6237000517345221310L;
    private String telephone; 
    private String nom;
    private String prenom;
    private String pays;
    private String password;
    private String email;
    private String operateurtel;
    

    public EtudiantCreate() {
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOperateurtel() {
        return operateurtel;
    }

    public void setOperateurtel(String operateurtel) {
        this.operateurtel = operateurtel;
    }

    
    
}
