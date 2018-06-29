package net.freelancertech.journal.app.api;

import net.freelancertech.journal.app.model.CommentaireDTO;
import net.freelancertech.journal.app.model.DelegueDTO;
import net.freelancertech.journal.app.model.EtudiantCreate;
import net.freelancertech.journal.app.model.EtudiantDTO;
import net.freelancertech.journal.app.model.InformationDTO;
import net.freelancertech.journal.app.model.NbreInformationsDTO;
import net.freelancertech.journal.app.model.Reponse;
import net.freelancertech.journal.app.model.UrlDTO;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public interface InstantInterface {

    @POST("/etudiants/create")
    void createByEtudiant(@Body EtudiantCreate create, Callback<EtudiantDTO> cb);

    @GET("/etudiants/informations/{email}")
    void getAllFromEmail(@Path("email") String email, Callback<List<InformationDTO>> cb);

    @GET("/etudiants/recherche/{email}")
    void findByEmail(@Path("email") String email, Callback<EtudiantDTO> cb) ;

    @POST("/etudiants/diffuser/{email}")
    void diffuserInformation(@Path("email") String email, @Body InformationDTO information, Callback<InformationDTO> cb);

    @POST("/etudiants/diffuserinformations/{email}")
    void diffuserInformations(@Path("email") String email, @Body List<InformationDTO> informations, Callback<InformationDTO> cb);

    @GET("/etudiants/nombreinformations/{email}")
    void getNumberInformationsFromEmail(@Path("email") String email, Callback<NbreInformationsDTO> cb);

    @POST("/etudiants/updateinformation/{informationId}")
    void updateInformation(@Path("informationId") long informationId, @Body InformationDTO information, Callback<InformationDTO> cb);

    @POST("/etudiants/update")
    void updateEtudiant(@Body EtudiantDTO etudiantDTO, Callback<EtudiantDTO> cb);

    @GET("/etudiants/commentaires/{informationId}")
    void getAllCommentaires(@Path("informationId") long informationId, Callback<List<CommentaireDTO>> cb);

    @POST("/etudiants/commenterinformation/{informationId}")
    void commenterInformation(@Path("informationId") long informationId, @Body CommentaireDTO commentaire, Callback<CommentaireDTO> cb);

    @GET("/getallurljournal")
    void getAllUrlJournal(Callback<UrlDTO> cb);

    @GET("/etudiants/isadmin/{email}")
    void isAdmin(@Path("email") String email, Callback<DelegueDTO> cb) ;

    @GET("/administration/supprimerinformation/{informationId}")
    void supprimerInformationAndCommentaire(@Path("informationId") long informationId, Callback<Reponse> cb);

    @GET("/administration/supprimercommentaire/{commentaireId}")
    void supprimerCommentaire(@Path("commentaireId") long commentaireId, Callback<Reponse> cb);


}
