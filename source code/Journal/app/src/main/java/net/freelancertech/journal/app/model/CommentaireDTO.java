package net.freelancertech.journal.app.model;

import java.io.Serializable;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */

public class CommentaireDTO implements Serializable {   
    private static final long serialVersionUID = -6237000517345224526L;
      
    
    private long commentaireId;   
    private String commentaire;   
    private String dateEnregistrement;
    private String etudiantPrenom;
    private String etudiantTelephone;



    public CommentaireDTO() {
    }

    public CommentaireDTO(long commentaireId, String commentaire, String dateEnregistrement, String etudiantPrenom, String etudiantTelephone) {
        this.commentaireId = commentaireId;
        this.commentaire = commentaire;
        this.dateEnregistrement = dateEnregistrement;
        this.etudiantPrenom = etudiantPrenom;
        this.etudiantTelephone = etudiantTelephone;
    }

    public long getCommentaireId() {
        return commentaireId;
    }

    public void setCommentaireId(long commentaireId) {
        this.commentaireId = commentaireId;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getEtudiantPrenom() {
        return etudiantPrenom;
    }

    public void setEtudiantPrenom(String etudiantPrenom) {
        this.etudiantPrenom = etudiantPrenom;
    }

    public String getEtudiantTelephone() {
        return etudiantTelephone;
    }

    public void setEtudiantTelephone(String etudiantTelephone) {
        this.etudiantTelephone = etudiantTelephone;
    }

    public String getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(String dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    
}
