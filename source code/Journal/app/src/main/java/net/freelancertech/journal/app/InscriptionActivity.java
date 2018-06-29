package net.freelancertech.journal.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import net.freelancertech.journal.app.api.InstantInterface;
import net.freelancertech.journal.app.custom.CustomTouchListener;
import net.freelancertech.journal.app.model.EtudiantCreate;
import net.freelancertech.journal.app.model.EtudiantDTO;
import net.freelancertech.journal.app.utils.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Locale;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class InscriptionActivity extends AppCompatActivity {

    private static final String LOG_TAG = InscriptionActivity.class.getSimpleName();


    private TextView paysTextView;
    private TextView indicatifTextView;
    private Button paysButton;
    private EditText telEditText;
    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText emailEditText;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText motdepasseEditText;
    private EditText confirmeMotdepasseEditText;
    private Button enregistrerButton;
    private Button annulerButton;

    private CheckBox accepteCheckBox;
    private TextView termesTextview;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        toolbar = (Toolbar) findViewById(net.freelancertech.journal.app.R.id.toolbar);
        setSupportActionBar(toolbar);

        // to display Icon launcher
        // to display Icon launcher
        ActionBar actionBar = getSupportActionBar();

        //icon back
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(net.freelancertech.journal.app.R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        // set title bar
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(net.freelancertech.journal.app.R.layout.action_bar_title_inscription);

        firebaseAuth = FirebaseAuth.getInstance();

        Utility.initialiseUrl(getApplicationContext());

        paysTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.select_pays_textview);
        indicatifTextView = (TextView) findViewById(net.freelancertech.journal.app.R.id.select_indicatif_textview);

        final Intent intent = new Intent(this, CountrycodeActivity.class);
        paysButton = (Button) findViewById(net.freelancertech.journal.app.R.id.select_pays_button);
        paysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                startActivityForResult(intent, 1);
            }
        });

        telEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.tel_editText);
        nomEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.nom_editText);
        prenomEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.prenom_editText);

        emailEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.email_editText);
        motdepasseEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.motdepasse_editText);
        confirmeMotdepasseEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.confirmemotdepasse_editText);

        termesTextview = (TextView) findViewById(net.freelancertech.journal.app.R.id.termes_textview);
        termesTextview.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_playlist_add_check_black_24dp), null, null, null);
        termesTextview.setOnTouchListener(new CustomTouchListener());
        termesTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });


        accepteCheckBox = (CheckBox) findViewById( net.freelancertech.journal.app.R.id.termes_checkBox );
        accepteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.inscription_activity_message_confirmation), Toast.LENGTH_SHORT).show();
                }

            }
        });



        enregistrerButton = (Button) findViewById(net.freelancertech.journal.app.R.id.enregister_button);

        enregistrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                // recuperation des valeurs saisis dans le formulaire
                final String pays = paysTextView.getText().toString();
                final String indicatif = indicatifTextView.getText().toString();
                final String tel = telEditText.getText().toString();
                final String nom = nomEditText.getText().toString();
                final String prenom = prenomEditText.getText().toString();
               // final String prenom = " ";

                String email = emailEditText.getText().toString().trim();
                final String motdepasse = motdepasseEditText.getText().toString();
                final String confirmemotdepasse = confirmeMotdepasseEditText.getText().toString();


                if (email.matches("")  || tel.matches("")  || nom.matches("")|| prenom.matches("")|| motdepasse.matches("")||confirmemotdepasse.matches("")
                        ||(pays.matches(""))) {
                    Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.inscription_activity_message7), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                        if (!email.matches(emailPattern)) {
                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.inscription_activity_message12), Toast.LENGTH_SHORT).show();
                            return;
                        }else{

                            email = emailEditText.getText().toString();
                        }

                        if(!motdepasse.equals(confirmemotdepasse)){

                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.inscription_activity_message8), Toast.LENGTH_SHORT).show();
                            return;

                        }else{


                            if (!accepteCheckBox.isChecked()) {

                                Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.inscription_activity_message13), Toast.LENGTH_SHORT).show();

                            } else{

                                // Firebase register Google email/password
                                final ProgressDialog progressDialog = ProgressDialog.show(InscriptionActivity.this, "Please wait...", "Processing...", true);
                                (firebaseAuth.createUserWithEmailAndPassword(email, motdepasse))
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progressDialog.dismiss();

                                                if (task.isSuccessful()) {

                                                 //save other informations to my server
                                                    String apiUrl = Utility.urlApiInfosFlash;

                                                    if (apiUrl != null) {

                                                        RestAdapter restAdapter = new RestAdapter.Builder()
                                                                .setEndpoint(apiUrl).build();

                                                        InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

                                                        EtudiantCreate etud = new EtudiantCreate();

                                                        etud.setTelephone(tel);
                                                        etud.setNom(nom);
                                                        etud.setPrenom(prenom);
                                                        etud.setPays(pays);
                                                        etud.setEmail(emailEditText.getText().toString().replace(".","_"));
                                                        etud.setPassword(motdepasse);
                                                        etud.setOperateurtel(indicatif);

                                                        instantDriverService.createByEtudiant(etud, new Callback<EtudiantDTO>() {
                                                            @Override
                                                            public void success(EtudiantDTO etudiantDTO, Response response) {


                                                                if(etudiantDTO!=null){

                                                                    Toast.makeText(InscriptionActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                                                    Intent i = new Intent(InscriptionActivity.this, LoginActivity.class);
                                                                    startActivity(i);
                                                                }else{

                                                                    Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void failure(RetrofitError error) {
                                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.system_message_server), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    } else {
                                                        Utility.initialiseUrl(getApplicationContext());
                                                    }


                                                }
                                                else
                                                {
                                                    Log.e("ERROR", task.getException().toString());
                                                    Toast.makeText(InscriptionActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });



                         }

                        }




                }
            }
        });

        annulerButton= (Button) findViewById(net.freelancertech.journal.app.R.id.annuler_button);

        annulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String selectCountryCode = data.getStringExtra(CountrycodeActivity.RESULT_COUNTRY_CODE);
            String[] tab = selectCountryCode.split("_");
            paysTextView.setText(tab[0]);
            indicatifTextView.setText("+" + tab[1]);
        }
    }

    void invokeBrowser (String url){
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
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
    protected void onStart() {
        super.onStart();
    }


    public  void showDialog(){


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_termes);
        dialog.setTitle(getResources().getString(R.string.inscription_activity_termes));

        EmojiconTextView termesConditionTextview = (EmojiconTextView) dialog.findViewById(R.id.termecondition_textview);

        String fromServerUnicodeDecoded;
        if ( Locale.getDefault().getLanguage().contains("en")) {

            fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava("At Journal Note, our mission is to empower every person and every organization on the planet to achieve more. We are doing this by building an intelligent cloud, reinventing productivity and business processes and making computing more personal. In all of this, we will maintain the timeless value of privacy and preserve the ability for you to control your data.\n" +
                    "\n" +
                    "This starts with making sure you get meaningful choices about how and why data is collected and used, and ensuring that you have the information you need to make the choices that are right for you across our products and services.\n" +
                    "\n" +
                    "We are working to earn your trust every day by focusing on six key privacy principles:\n" +
                    "\n" +
                    "\\\u2022 Control: We will put you in control of your privacy with easy-to-use tools and clear choices.\n" +
                    "\\\u2022  Transparency: We will be transparent about data collection and use so you can make informed decisions.\n" +
                    "\\\u2022 Security: We will protect the data you entrust to us through strong security and encryption.\n" +
                    "\\\u2022 Strong legal protections: We will respect your local privacy laws and fight for legal protection of your privacy as a fundamental human right.\n" +
                    "\\\u2022  No content-based targeting: We will not use your email, chat, files or other personal content to target ads to you.\n" +
                    "\\\u2022  Benefits to you: When we do collect data, we will use it to benefit you and to make your experiences better.");

        }else{

            fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava("Notre mission au sein de Journal Note est de donner à chaque individu et à chaque organisation les moyens d'en faire plus. Pour atteindre cet objectif, nous mettons en place un cloud intelligent, réinventons les processus productifs et commerciaux et rendons l'expérience utilisateur plus personnalisée. Nous gardons évidemment à l'esprit l'importance capitale de la confidentialité des informations et vous permettons toujours de contrôler vous-même vos données.\n" +
                    "\n" +
                    "Nous commençons par nous assurer que vous disposez d'options significatives concernant le mode de collecte et d'utilisation des données et la cause de cette collecte. Nous voulons vérifier que vous disposez des informations requises pour prendre les décisions qui vous conviennent et ce, pour l'ensemble de nos produits et services.\n" +
                    "\n" +
                    "Nous nous concentrons sur six principes majeurs de la confidentialité, afin de gagner votre confiance au jour le jour :\n" +
                    "\n" +
                    "\\\u2022 Contrôle : nous vous octroyons le contrôle de votre confidentialité, en mettant à votre disposition des outils conviviaux et en vous présentant des options claires.\n" +
                    "\\\u2022  Transparence : nous nous efforcerons d'être transparents quant à la collecte et à l'utilisation des données pour votre prise de décision informée.\n" +
                    "\\\u2022 Sécurité : nous ferons en sorte de protéger les données que vous nous confiez, via des fonctionnalités de chiffrement et de sécurité renforcées.\n" +
                    "\\\u2022  Une protection juridique solide : nous respecterons la réglementation locale en vigueur en matière de confidentialité et soutiendrons la protection juridique de cette dernière en tant que droit fondamental.\n" +
                    "\\\u2022  Pas de ciblage en fonction du contenu : nous n’utiliserons pas votre fonctionnalité de discussion, le contenu de vos e-mails, vos fichiers ou tout autre contenu personnel pour cibler nos publicités sur vos besoins.\n" +
                    "\\\u2022  Des avantages pour vous : la collecte de données par nos soins sera à votre avantage et améliorera votre expérience utilisateur.");        }

        termesConditionTextview.setText(fromServerUnicodeDecoded);

        Button dialogOkButton = (Button) dialog.findViewById(R.id.btnok_dialog);
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accepteCheckBox.setChecked(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
