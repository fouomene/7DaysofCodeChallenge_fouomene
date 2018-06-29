package net.freelancertech.journal.app;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.freelancertech.journal.app.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Locale;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class SiteWebActivity extends ActionBarActivity {

    private static final String LOG_TAG = SiteWebActivity.class.getSimpleName();
    private  String RESULTATS="https://www.fouomene.com";
    private boolean mFromSavedInstanceState;
    private ProgressBar progressbar;
    private WebView mWebView;

    private EmojiconTextView maide_textview;


    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siteweb);
        // to display Icon launcher


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // to display Icon launcher
        ActionBar actionBar = getSupportActionBar();

        //icon back
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        // set title bar
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_title_siteweb);

        if (savedInstanceState != null) {

            RESULTATS = savedInstanceState.getString("RESULTATS");
            mFromSavedInstanceState = true;

        }


        final Activity activity = this;



        maide_textview = (EmojiconTextView) findViewById(R.id.aide_textview);
        String fromServerUnicodeDecoded ;

        if ( Locale.getDefault().getLanguage().contains("en")) {

            fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava("With Journal Doodle : All your notes, in one place and at your fingertips, wherever you are.\n" +
                    "\n" +
                    "Comment and Share your notes, to-do lists ... add images, URL.\n" +
                    "Your notes are synced across all your Android devices (phone, tablet), so you can access them wherever you are.\n" +
                    "\n" +
                    "Key features:\n" +
                    "\n" +
                    "GET ORGANIZED\n" +
                    "• Write, collect, and capture ideas in the form of notes, notebooks, and task lists that can be searched.\n" +
                    "• Take notes in a variety of formats, including: text, images, URLs, web captures.\n" +
                    "• Comment and Share your notes with ease.\n" +
                    "\n" +
                    "FILTERING NOTES BY KEYWORDS\n" +
                    "• Find notes later using a simple search. by keyword.\n" +
                    "\n" +
                    "IN OFF-CONNECTION MODE\n" +
                    "• Only text notes are accessible offline because they are stored in your Android device.\n" +
                    "\n" +
                    "SYNCHRONIZE YOUR DATA EVERYWHERE\n" +
                    "Doodle Journal gives you the ability to sync and comment on all your Android devices:\n" +
                    "• Start your task on an Android device, then continue your work on another Android device without wasting time\n" +
                    "\n" +
                    "Required conditions :\n" +
                    "• Android system, version 4.2 or later.");
        }else{

            fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava("Avec Journal Doodle : Toutes vos notes, en un seul endroit et à portée de main, où que vous soyez. \n" +
                    "\n" +
                    "Commentez et Partagez vos notes, listes de tâches... ajoutez des images, URL. \n" +
                    "Vos notes sont synchronisées sur tous vos appareils (téléphone, tablette) Android, de sorte que vous pouvez y accéder où que vous soyez. \n" +
                    "\n" +
                    "Fonctionnalités clés :\n" +
                    "\n" +
                    "ORGANISEZ-VOUS\n" +
                    "\t• Écrivez, collectez et capturez des idées sous forme de notes, carnets de notes et listes de tâches pouvant faire l'objet de recherche.\n" +
                    "\t• Prenez des notes dans divers formats, notamment : du texte, des images, URL, des captures Web.\n" +
                    "\t• Commentez et Partagez vos notes en toute simplicité.\n" +
                    "\n" +
                    "FILTRAGE DES NOTES PAR MOTS-CLES \n" +
                    "\t• Retrouvez les notes plus tard à l'aide d'une simple recherche. par mot clé.\n" +
                    "\n" +
                    "EN MODE HORS CONNEXION \n" +
                    "\t•Uniquement les notes textes sont accessibles en mode hors connexion car stockées dans votre appareil Android.\n" +
                    "\n" +
                    "SYNCHRONISEZ VOS DONNÉES PARTOUT\n" +
                    "\tJournal Doodle vous offre la possibilité de synchroniser et de commenter votre contenu sur tous vos appareils Android:\n" +
                    "\t• Commencez votre tâche sur un appareil Android, puis poursuivez votre travail sur un autre appareil Android sans perdre de temps\n" +
                    "\n" +
                    "Conditions requises : \n" +
                    "• Système Android, version 4.2 ou ultérieure. ");
        }

        maide_textview.setText(fromServerUnicodeDecoded);

    }



    public boolean isOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()== NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()== NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOnline()) {

           // mWebView.loadUrl(RESULTATS);

        }else{

           // mWebView.loadData("<html><body>V&eacute;rifier que votre connexion internet est active..</body></html>", "text/html", null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("RESULTATS", RESULTATS);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        RESULTATS = savedInstanceState.getString("RESULTATS");

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    public void setValue(int progress) {
        this.progressbar.setProgress(progress);
    }

}
