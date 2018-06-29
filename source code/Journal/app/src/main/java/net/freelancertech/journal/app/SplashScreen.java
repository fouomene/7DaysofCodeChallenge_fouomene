package net.freelancertech.journal.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import net.freelancertech.journal.app.sync.DoodlesArchiveSyncAdapter;
import net.freelancertech.journal.app.utils.Utility;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class SplashScreen extends Activity {

    private ProgressBar bar = null;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        bar = (ProgressBar)findViewById(R.id.secondBar);





        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {



                String email = Utility.getPreferredEmail(getApplicationContext());

                if (email.equals("defaultmail@fouomene.com")) {

                    FirebaseAuth.getInstance().signOut();

                    Utility.setPreferredNom(getApplicationContext(),"Journal");
                    Utility.setPreferredPrenom(getApplicationContext(),"Note");
                    Utility.setPreferredTelephone(getApplicationContext(),"00000");
                    Utility.setPreferredIsDelegue(getApplicationContext(),"false");
                    Utility.setPreferredEmail(getApplicationContext(), "defaultmail@fouomene.com");
                    Utility.setPreferredMatricule(getApplicationContext(),"Cameroun");

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                }else {

                    DoodlesArchiveSyncAdapter.initializeSyncAdapter(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }

                DoodlesArchiveSyncAdapter.syncImmediately(getBaseContext());

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}