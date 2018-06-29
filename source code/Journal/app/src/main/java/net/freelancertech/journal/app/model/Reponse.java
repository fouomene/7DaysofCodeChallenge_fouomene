package net.freelancertech.journal.app.model;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * Created by FOUOMENE on 06/2018.
 * EmailAuthor:  fouomenedaniel@gmail.com .
 *
 **/
public class Reponse {
    private int type;
    private Object valeur;

    public Reponse(int type, Object valeur) {
        this.type = type;
        this.valeur = valeur;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

}