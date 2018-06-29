package net.freelancertech.journal.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class AbonnerCreate implements Serializable {   
    private static final long serialVersionUID = -6237000517345221387L;

    private String telephone; 
    private List<Long> niveaux;
    
    public AbonnerCreate() {
    }
    
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Long> getNiveaux() {
        return niveaux;
    }

    public void setNiveaux(List<Long> niveaux) {
        this.niveaux = niveaux;
    }
    
}
