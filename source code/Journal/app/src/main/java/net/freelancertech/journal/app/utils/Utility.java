package net.freelancertech.journal.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import net.freelancertech.journal.app.R;
import net.freelancertech.journal.app.api.InstantInterface;
import net.freelancertech.journal.app.model.UrlDTO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();


    // service web sur le serveur freelancertech (supposé être toujour disponible) qui renvoie les urls globals
    public static String APIURL = "http://freelancertech.net/taxi/web/taxiapp/taximen/";

    public static String urlApiInfosFlash ="http://37.187.88.37:8080/journalapp/api";

    public static String urlabonnementinfoflash = "http://www.fouomene.com";

    public static String urlaproposfreelancertech = "http://www.fouomene.com";

    public static String urlserveurimage = "http://freelancertech.net";

    public static String urlahelpfacinfosflash = "http://fouomene.com";

    public static String numeserveurgsmmtn = "674099619";

    public static String numeserveurgsmorange = "674099619";

    public static String intervalle = "900";

    public static String urlsendnotificationpush = "http://fouomene.com/journal/push_notification.php";

    public static String urlregisternotification = "http://fouomene.com/journal/register.php";

    public static int versionurl = 0;


   // public static String url ;

    public static String getPreferredYear(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_year_key),
                context.getString(R.string.pref_year_default));
    }

    public static String getPreferredMonth(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_month_key),
                context.getString(R.string.pref_month_default));
    }

    public static String getPreferredTelephone(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_telephone_key),
                context.getString(R.string.pref_telephone_default));
    }

    public static String getPreferredOperateur(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_operateur_key),
                context.getString(R.string.pref_operateur_default));
    }

    public static String getPreferredNom(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_nom_key),
                context.getString(R.string.pref_nom_default));
    }

    public static String getPreferredPrenom(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_prenom_key),
                context.getString(R.string.pref_prenom_default));
    }


    public static String getPreferredMatricule(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_matricule_key),
                context.getString(R.string.pref_matricule_default));
    }

    public static String getPreferredEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_email_key),
                context.getString(R.string.pref_email_default));
    }

    public static String getPreferredIsDelegue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_isdelegue_key),
                context.getString(R.string.pref_isdelegue_default));
    }

    public static String getPreferredIsAdmin(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_isadmin_key),
                context.getString(R.string.pref_isadmin_default));
    }

    public static String getPreferredAPIURL(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_APIURL_key),
                context.getString(R.string.pref_APIURL_default));
    }

    public static String getPreferredUrlApiInfosFlash(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_urlApiInfosFlash_key),
                context.getString(R.string.pref_urlApiInfosFlash_default));
    }

    public static String getPreferredUrlserveurimage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_urlserveurimage_key),
                context.getString(R.string.pref_urlserveurimage_default));
    }

    public static String getPreferredUrlsendnotificationpush(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_urlsendnotificationpush_key),
                context.getString(R.string.pref_urlsendnotificationpush_default));
    }

    public static String getPreferredUrlregisternotification(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_urlregisternotification_key),
                context.getString(R.string.pref_urlregisternotification_default));
    }

    public static String getPreferredVersionurl(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_versionurl_key),
                context.getString(R.string.pref_versionurl_default));
    }

    public static String getPreferredMotcle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_motcle_key),
                context.getString(R.string.pref_motcle_default));
    }

    public static String getPreferredMonMotcle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_monmotcle_key),
                context.getString(R.string.pref_monmotcle_default));
    }


    public static void setPreferredYear(Context context, String year) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_year_key), year);
        editor.commit();
    }

    public static void setPreferredMonth(Context context, String month) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_month_key) ,month);
        editor.commit();
    }

    public static void setPreferredTelephone(Context context, String telephone) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_telephone_key) ,telephone);
        editor.commit();
    }

    public static void setPreferredOperateur(Context context, String operateur) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_operateur_key) ,operateur);
        editor.commit();
    }

    public static void setPreferredNom(Context context, String nom) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_nom_key) ,nom);
        editor.commit();
    }

    public static void setPreferredPrenom(Context context, String prenom) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_prenom_key) ,prenom);
        editor.commit();
    }

    public static void setPreferredMatricule(Context context, String matricule) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_matricule_key) ,matricule);
        editor.commit();
    }

    public static void setPreferredEmail(Context context, String email) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_email_key) ,email);
        editor.commit();
    }

    public static void setPreferredIsDelegue(Context context, String isdelegue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_isdelegue_key) ,isdelegue);
        editor.commit();
    }

    public static void setPreferredIsAdmin(Context context, String isadmin) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_isadmin_key) ,isadmin);
        editor.commit();
    }


    public static String getPreferredPosition(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_position_key),
                context.getString(R.string.pref_position_default));
    }

    public static void setPreferredPosition(Context context, String artist) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_position_key), artist);
        editor.commit();
    }

    public static String getPreferredIntervalle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_intervalle_key),
                context.getString(R.string.pref_intervalle_default));
    }

    public static void setPreferredIntervalle(Context context, String intervalle) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_intervalle_key), intervalle);
        editor.commit();
    }

    public static void setPreferredMotcle(Context context, String motcle) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_motcle_key), motcle);
        editor.commit();
    }

    public static void setPreferredMonMotcle(Context context, String monmotcle) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_monmotcle_key), monmotcle);
        editor.commit();
    }


    public static void setPreferredAPIURL(Context context, String urlApi) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_APIURL_key) ,urlApi);
        editor.commit();
    }

    public static void setPreferredUrlApiInfosFlash(Context context, String urlApiInfosFlash) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_urlApiInfosFlash_key) ,urlApiInfosFlash);
        editor.commit();
    }

    public static void setPreferredUrlserveurimage(Context context, String urlserveurimage) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_urlserveurimage_key) ,urlserveurimage);
        editor.commit();
    }

    public static void setPreferredUrlsendnotificationpush(Context context, String urlsendnotificationpush) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_urlsendnotificationpush_key) ,urlsendnotificationpush);
        editor.commit();
    }

    public static void setPreferredUrlregisternotification(Context context, String urlregisternotification) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_urlregisternotification_key) ,urlregisternotification);
        editor.commit();
    }

    public static void setPreferredVersionurl(Context context, String versionurl) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_versionurl_key) ,versionurl);
        editor.commit();
    }

    public static void initialiseUrl( final Context context ) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Utility.APIURL).build();

        InstantInterface instantDriverService = restAdapter.create(InstantInterface.class);

        instantDriverService.getAllUrlJournal(new Callback<UrlDTO>() {
            @Override
            public void success(UrlDTO urlDTO, Response response) {

                if (urlDTO != null) {
                   // Log.e(LOG_TAG, "**************** InitialiseUrl Tous les URLAPI " + urlDTO.toString());

                    urlApiInfosFlash = urlDTO.getUrlapiinfoflash();
                    urlApiInfosFlash = urlApiInfosFlash.replace("_", "/");

                    urlabonnementinfoflash = urlDTO.getUrlabonnementinfoflash();
                    urlabonnementinfoflash = urlabonnementinfoflash.replace("_", "/");

                    urlaproposfreelancertech = urlDTO.getUrlaproposfreelancertech();
                    urlaproposfreelancertech = urlaproposfreelancertech.replace("_", "/");

                    urlserveurimage = urlDTO.getUrlserveurimage();
                    urlserveurimage = urlserveurimage.replace("_", "/");

                    urlahelpfacinfosflash = urlDTO.getUrlahelpfacinfosflash();
                    urlahelpfacinfosflash = urlahelpfacinfosflash.replace("_","/");

                    numeserveurgsmmtn = urlDTO.getNumeserveurgsmmtn();
                    numeserveurgsmorange = urlDTO.getNumeserveurgsmorange();

                    intervalle = urlDTO.getIntervalle();

                    APIURL = urlDTO.getApiurl();
                    APIURL = APIURL.replace("-","/");

                    urlsendnotificationpush = urlDTO.getUrlsendnotificationpush();
                    urlsendnotificationpush = urlsendnotificationpush.replace("-","/");

                    urlregisternotification = urlDTO.getUrlregisternotification();
                    urlregisternotification = urlregisternotification.replace("-","/");

                    versionurl = Integer.parseInt(urlDTO.getVersionurl());

                    int versionParam = Integer.parseInt(Utility.getPreferredVersionurl(context));

                    if (versionParam!= versionurl) {

                        Utility.setPreferredAPIURL(context,APIURL);
                        Utility.setPreferredUrlsendnotificationpush(context,urlsendnotificationpush);
                        Utility.setPreferredUrlregisternotification(context,urlregisternotification);
                        Utility.setPreferredVersionurl(context,versionurl+"");
                        Utility.setPreferredUrlApiInfosFlash(context,urlApiInfosFlash);
                        Utility.setPreferredUrlserveurimage(context,urlserveurimage);
                    }


                } else {
                    Log.e(LOG_TAG, "****************UrlDTO null ");
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Server Unavailable at the moment!!!");
            }
        });

    }

    public static String formatDate(String date) {

        String[] dateArray = date.split("/");

        try {
            Calendar cal = new GregorianCalendar(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1])-1, Integer.parseInt(dateArray[2]));
            return new SimpleDateFormat("MMMM dd, yyyy").format(cal.getTime());
        } catch(NumberFormatException nfe) {
            Log.e(LOG_TAG, "Could not parse " + nfe);
        }
        return  date;
    }


    public static void sendPushNotification(String message,Context context) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Objet",message)
                .build();

        Request request = new Request.Builder()
                .url(Utility.getPreferredUrlsendnotificationpush(context))
                .post(body)
                .build();

        Call postCall=client.newCall(request);
        postCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(LOG_TAG, "******************************* Notification PUSH FAIL ");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d(LOG_TAG, "******************************* Notification PUSH SUCCES ");
            }
        });

    }



}