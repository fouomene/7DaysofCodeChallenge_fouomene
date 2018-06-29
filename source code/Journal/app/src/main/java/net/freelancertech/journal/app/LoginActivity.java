package net.freelancertech.journal.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

import net.freelancertech.journal.app.api.InstantInterface;
import net.freelancertech.journal.app.custom.CustomTouchListener;
import net.freelancertech.journal.app.model.EtudiantDTO;
import net.freelancertech.journal.app.sync.DoodlesArchiveSyncAdapter;
import net.freelancertech.journal.app.utils.Utility;

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
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button suivantButton;
    private TextView sabonnerTextview;

    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // to display Icon launcher

        toolbar = (Toolbar) findViewById(net.freelancertech.journal.app.R.id.toolbar);
        setSupportActionBar(toolbar);



        // to display Icon launcher
        ActionBar actionBar = getSupportActionBar();

        //icon back
       // actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(net.freelancertech.journal.app.R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        // set title bar
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(net.freelancertech.journal.app.R.layout.action_bar_title_login);

        Intent intent = getIntent();

        if ( intent != null && intent.getStringExtra("deconnecter") != null){

            // user sign out in Firebase
            FirebaseAuth.getInstance().signOut();

            Utility.setPreferredNom(getApplicationContext(),"Journal");
            Utility.setPreferredPrenom(getApplicationContext(),"Note");
            Utility.setPreferredTelephone(getApplicationContext(),"00000");
            Utility.setPreferredIsDelegue(getApplicationContext(),"false");
            Utility.setPreferredEmail(getApplicationContext(), "defaultmail@fouomene.com");
            Utility.setPreferredMatricule(getApplicationContext(),"Cameroun");
        }



            Utility.initialiseUrl(getApplicationContext());

            emailEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.email_editText);
            passwordEditText = (EditText) findViewById(net.freelancertech.journal.app.R.id.password_editText);
            firebaseAuth = FirebaseAuth.getInstance();

            suivantButton = (Button) findViewById(net.freelancertech.journal.app.R.id.suivant_button);

            suivantButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    // recuperation des valeurs saisis dans le formulaire
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    if (email.matches("")|| password.matches("")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.login_activity_message1), Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                      if (isOnline()) {


                            final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Proccessing...", true);

                            // Firebase Authentication Google email/password
                            (firebaseAuth.signInWithEmailAndPassword(email, password))
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressDialog.dismiss();

                                            if (task.isSuccessful()) {

                                                Utility.setPreferredEmail(getApplicationContext(), emailEditText.getText().toString().replace(".","_"));

                                                // Get other information of user by my server
                                                String apiUrl = Utility.urlApiInfosFlash;
                                                if (apiUrl != null) {

                                                    RestAdapter restAdapter = new RestAdapter.Builder()
                                                            .setEndpoint(apiUrl).build();

                                                    InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

                                                    instantDriverService.findByEmail(emailEditText.getText().toString().replace(".","_"), new Callback<EtudiantDTO>() {
                                                        @Override
                                                        public void success(EtudiantDTO etudiantDTO, Response response) {

                                                            if (etudiantDTO != null) {
                                                                    try {
                                                                        // sync notes local with server
                                                                        DoodlesArchiveSyncAdapter.syncImmediately(getApplicationContext());
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                    intent.putExtra("telephone", etudiantDTO.getTelephone());
                                                                    intent.putExtra("operateur", etudiantDTO.getOperateurtel());
                                                                    intent.putExtra("nom", etudiantDTO.getNom());
                                                                    intent.putExtra("prenom", etudiantDTO.getPrenom());
                                                                    intent.putExtra("matricule", etudiantDTO.getPays());
                                                                    intent.putExtra("email", etudiantDTO.getEmail());
                                                                    intent.putExtra("isdelegue", etudiantDTO.isDelegue()+"");
                                                                    intent.putExtra("isadmin", etudiantDTO.isAdmin()+"");
                                                                    startActivity(intent);



                                                            } else {

                                                                Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.login_activity_message4), Toast.LENGTH_SHORT).show();

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


                                            } else {
                                                Log.e("ERROR", task.getException().toString());
                                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                        } else {

                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            sabonnerTextview = (TextView) findViewById(net.freelancertech.journal.app.R.id.sabonner_textview);
           sabonnerTextview.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(net.freelancertech.journal.app.R.drawable.ic_account_circle_black_24dp), null, null, null);
           sabonnerTextview.setOnTouchListener(new CustomTouchListener());
           sabonnerTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {

                    if (isOnline()){
                        Intent intent = new Intent(getApplicationContext(), InscriptionActivity.class);
                        startActivity(intent);
                    }else{

                        Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();

                    }

                }
            });


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
    public void onBackPressed() {
        //super.onBackPressed();
    }



}
