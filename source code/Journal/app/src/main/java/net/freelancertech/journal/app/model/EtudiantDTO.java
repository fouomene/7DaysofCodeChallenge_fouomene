package net.freelancertech.journal.app.model;

import java.io.Serializable;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */

public class EtudiantDTO implements Serializable {   
    private static final long serialVersionUID = -6237000517345221310L;
    private String telephone; 
    private String nom;
    private String prenom;
    private String pays;
    private String password;
    private boolean delegue;
    private String code;
    private int codactive;


    private String email;
    private String operateurtel;

    private boolean admin;
    
    

    public EtudiantDTO() {
    }

    public EtudiantDTO(String telephone, String nom, String prenom, String pays,boolean delegue) {
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.delegue=delegue;
    }
 
    
    public EtudiantDTO(String telephone, String nom, String prenom, String pays,boolean delegue, String code, int codactive,String email, String operateurtel) {
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.delegue=delegue;
        this.code = code;
        this.codactive = codactive;
        this.email = email;
        this.operateurtel = operateurtel;
        
    }


    public EtudiantDTO(String telephone, String nom, String prenom, String pays, boolean delegue, String code, int codactive,String email, String operateurtel, boolean admin) {
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.delegue=delegue;
        this.code = code;
        this.codactive = codactive;
        this.email = email;
        this.operateurtel = operateurtel;
        this.admin = admin;

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

    public boolean isDelegue() {
        return delegue;
    }

    public void setDelegue(boolean delegue) {
        this.delegue = delegue;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
 
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCodactive() {
        return codactive;
    }

    public void setCodactive(int codactive) {
        this.codactive = codactive;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


}
