package net.freelancertech.journal.app.model;

import java.io.Serializable;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class NbreInformationsDTO  implements Serializable {   
    private static final long serialVersionUID = -6237000517345221393L;
       
    private int nombre;  
    
    
    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
    
    public NbreInformationsDTO() {
    }

    public NbreInformationsDTO(int nbre) {
        this.nombre = nbre;
    }
}
