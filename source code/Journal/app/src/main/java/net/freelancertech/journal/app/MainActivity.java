package net.freelancertech.journal.app;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;

import net.freelancertech.journal.app.api.InstantInterface;
import net.freelancertech.journal.app.model.EtudiantDTO;
import net.freelancertech.journal.app.sync.DoodlesArchiveSyncAdapter;
import net.freelancertech.journal.app.utils.Utility;

import java.util.Calendar;

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
public class MainActivity extends AppCompatActivity implements DoodleFragment.Callback{

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;


    private  CircleImageView photoProfil;
    private TextView nomProfil;
    private TextView emailProfil;


    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;
    private String mYear;
    private String mMonth;
    private String mMonMotCle;

    private Uri builtUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(net.freelancertech.journal.app.R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(net.freelancertech.journal.app.R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(net.freelancertech.journal.app.R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        // set title bar
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(net.freelancertech.journal.app.R.layout.action_bar_title_main);

        Utility.initialiseUrl(getApplicationContext());


        //set year and month to current value
        Utility.setPreferredYear(this, "" + Calendar.getInstance().get(Calendar.YEAR));
        int monthCurrent = Calendar.getInstance().get(Calendar.MONTH)+1;
        Utility.setPreferredMonth(this, "" + monthCurrent);

        Intent intent = getIntent();


        String email = Utility.getPreferredEmail(getApplicationContext());


        // URL image
        String urlPhotoprofile = "/facinfosflash/imagesprofils/" + email + ".jpg";
        String isdelegue="false";
        String isadmin="false";

        if ( intent != null && intent.getStringExtra("email") != null){
            String  telephone = intent.getStringExtra("telephone");
            String nom = intent.getStringExtra("nom");
            String prenom = intent.getStringExtra("prenom");
            String matricule = intent.getStringExtra("matricule");
            email = intent.getStringExtra("email");
            String operateur = intent.getStringExtra("operateur");
            isdelegue = intent.getStringExtra("isdelegue");
            isadmin = intent.getStringExtra("isadmin");
            Utility.setPreferredEmail(this, email);
            Utility.setPreferredNom(this, nom);
            Utility.setPreferredPrenom(this, prenom);
            Utility.setPreferredTelephone(this, telephone);
            Utility.setPreferredOperateur(this, operateur);
            Utility.setPreferredMatricule(this, matricule);
            Utility.setPreferredIsDelegue(this, isdelegue);
            Utility.setPreferredIsAdmin(this, isadmin);


        }


        urlPhotoprofile = "/facinfosflash/imagesprofils/" + email + ".jpg";


       // to filter notes by month and year
        mYear = Utility.getPreferredYear(this);
        mMonth = Utility.getPreferredMonth(this);
        mMonMotCle = Utility.getPreferredMonMotcle(this);



        mDrawerLayout = (DrawerLayout) findViewById(net.freelancertech.journal.app.R.id.drawer_layout);

        //Animate the Hamburger Icon ou navigator icon
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(drawerToggle);

        //navigate
        navigationView = (NavigationView) findViewById(net.freelancertech.journal.app.R.id.navigation_view);
        setupDrawerContent(navigationView);


        photoProfil = (CircleImageView) findViewById(net.freelancertech.journal.app.R.id.photo_profil_navigateview);



        // Construct the URL photo
        // Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage+urlPhotoprofile);
        builtUri  = new Uri.Builder()
                .scheme("http")
                .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                .path(urlPhotoprofile)
                .build();
        // Log.e(LOG_TAG, "URI = " + builtUri.toString());

        Picasso.with(this).load(builtUri.toString()).placeholder(net.freelancertech.journal.app.R.drawable.nophoto).error(net.freelancertech.journal.app.R.drawable.nophoto).into(photoProfil);

        photoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("ResourceType") Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), net.freelancertech.journal.app.R.animator.fade_in);
                v.startAnimation(animFadein);

                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intent);

            }
        });

        nomProfil = (TextView)findViewById(net.freelancertech.journal.app.R.id.nom_navigateview);
        emailProfil = (TextView)findViewById(net.freelancertech.journal.app.R.id.email_navigateview);

        nomProfil.setText(Utility.getPreferredNom(this)+" "+Utility.getPreferredPrenom(this));
        emailProfil.setText(Utility.getPreferredEmail(this).replace("_","."));

        
        if (findViewById(net.freelancertech.journal.app.R.id.doodle_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(net.freelancertech.journal.app.R.id.doodle_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        // Make sur you have gotten an accound created
       // DoodlesArchiveSyncAdapter.initializeSyncAdapter(this);

        FirebaseMessaging.getInstance().subscribeToTopic("journalapp");
        FirebaseInstanceId.getInstance().getToken();


    }


    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, net.freelancertech.journal.app.R.string.drawer_open,  net.freelancertech.journal.app.R.string.drawer_close);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case net.freelancertech.journal.app.R.id.navigation_item_informations:{

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case net.freelancertech.journal.app.R.id.navigation_item_send:{


                        if (isOnline()) {

                            Intent intent = new Intent(getApplicationContext(), SendActivity.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(getApplicationContext(), getResources().getString(net.freelancertech.journal.app.R.string.system_message_internet2), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }




                    case net.freelancertech.journal.app.R.id.navigation_item_profil:{

                        Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                        startActivity(intent);
                        break;
                    }


                    case net.freelancertech.journal.app.R.id.navigation_item_setting:{

                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    }


                    case net.freelancertech.journal.app.R.id.navigation_item_help:{


                        Intent intent = new Intent(getApplicationContext(), SiteWebActivity.class);
                        startActivity(intent);

                        break;
                    }


                    case net.freelancertech.journal.app.R.id.navigation_item_logout:{

                        Utility.setPreferredNom(getApplicationContext(),"Journal");
                        Utility.setPreferredPrenom(getApplicationContext(),"Note");
                        Utility.setPreferredTelephone(getApplicationContext(),"00000");
                        Utility.setPreferredIsDelegue(getApplicationContext(),"false");
                        Utility.setPreferredEmail(getApplicationContext(), "defaultmail@fouomene.com");
                        Utility.setPreferredMatricule(getApplicationContext(),"Cameroun");

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("deconnecter", "succes");
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(net.freelancertech.journal.app.R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            Utility.setPreferredMotcle(this,"vide");
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_about) {

            showDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String year = Utility.getPreferredYear(this);
        String month = Utility.getPreferredMonth(this);
        String monmotcle = Utility.getPreferredMonMotcle(this);
        // update the year month in our second pane using the fragment manager  mMonMotCle
        if ((year != null && !year.equals(mYear)) || (month != null && !month.equals(mMonth))|| (monmotcle != null && !monmotcle.equals(mMonMotCle))) {
            DoodleFragment ff = (DoodleFragment)getSupportFragmentManager().findFragmentById(net.freelancertech.journal.app.R.id.fragment_doodle);
            if ( null != ff ) {
                ff.onYearMonthChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df ) {
                df.onYearMonthChanged(year, month);
            }
            mYear = year;
            mMonth = month;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri,String idInformation) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);
            args.putString(DetailFragment.DETAIL_INFORMATION_ID, idInformation);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(net.freelancertech.journal.app.R.id.doodle_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            intent.putExtra(DetailFragment.DETAIL_INFORMATION_ID, idInformation);
            startActivity(intent);
        }

        deleteNotification(DoodlesArchiveSyncAdapter.DOODLE_NOTIFICATION_ID);
    }

    private void deleteNotification(int notificationId){

        if (Context.NOTIFICATION_SERVICE!=null) {

            final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            //la suppression de la notification se fait grâce a son ID
            notificationManager.cancel(notificationId);
        }

    }


    public boolean isOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }




    void invokeBrowser (String url){

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public  void showDialog(){

        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        new AlertDialog.Builder(this)
                .setView(inflater.inflate(net.freelancertech.journal.app.R.layout.dialog_apropros, null))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String apiUrl = Utility.urlaproposfreelancertech;

                        if (apiUrl != null) {
                            invokeBrowser(apiUrl);
                        } else {
                            Utility.initialiseUrl(getApplicationContext());
                        }
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}
