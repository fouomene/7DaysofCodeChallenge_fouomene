package net.freelancertech.journal.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import net.freelancertech.journal.app.MainActivity;
import net.freelancertech.journal.app.R;
import net.freelancertech.journal.app.api.InstantInterface;
import net.freelancertech.journal.app.data.InstantContract;
import net.freelancertech.journal.app.model.InformationDTO;
import net.freelancertech.journal.app.model.NbreInformationsDTO;
import net.freelancertech.journal.app.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

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
public class DoodlesArchiveSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = DoodlesArchiveSyncAdapter.class.getSimpleName();


    // Interval at which to sync with the doodle, in seconds.
    // 60 seconds (1 minute) * 720 = 12 hours
   // public static final int SYNC_INTERVAL = 60 * 60 * 12;
    public static final int SYNC_INTERVAL = 60*90;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    private static final String[] NOTIFY_DOODLE_PROJECTION = new String[] {
            InstantContract.DoodleEntry.TABLE_NAME + "."+ InstantContract.DoodleEntry._ID,
            InstantContract.DoodleEntry.COLUMN_OBJET,
            InstantContract.DoodleEntry.COLUMN_RUN_DATE
    };

   /// private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24; (24 heure 1 jour)
    private static final long DAY_IN_MILLIS = 1000 * 30;
    public static final int DOODLE_NOTIFICATION_ID = 3004;

    // these indices must match the projection
    private static final int INDEX_ID = 0;
    private static final int INDEX_OBJET = 1;
    private static final int INDEX_RUN_DATE = 2;

    public DoodlesArchiveSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.d(LOG_TAG, "Starting sync");


        String emailQuery = Utility.getPreferredEmail(getContext());

        Utility.initialiseUrl(getContext());

        String apiUrl = Utility.getPreferredUrlApiInfosFlash(getContext());

      //  Log.d(LOG_TAG, "******************************* intervalle= " + Utility.intervalle + telephoneQuery);


        if (apiUrl != null) {

           // Log.d(LOG_TAG, "Starting sync");

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(apiUrl).build();

            InstantInterface instantDriverService  = restAdapter.create(InstantInterface.class);

            instantDriverService.getNumberInformationsFromEmail(emailQuery, new Callback<NbreInformationsDTO>() {
                @Override
                public void success(NbreInformationsDTO nbreInformationsDTO, Response response) {

                    if (nbreInformationsDTO != null) {

                        String yearQuery = Utility.getPreferredYear(getContext());
                        String monthQuery = Utility.getPreferredMonth(getContext());



                        //Log.d(LOG_TAG, "************************************************* Nbre d'information de NbreInformationsDTO = " + nbreInformationsDTO.getNombre());

                        Uri doodleUri = InstantContract.DoodleEntry.buildDoodleYearMonth(
                                yearQuery, monthQuery);

                        Cursor cursorDoodle = getContext().getContentResolver().query(doodleUri, null, null, null, null);

                        if (cursorDoodle.getCount() != nbreInformationsDTO.getNombre()) {


                            RestAdapter restAdapter2 = new RestAdapter.Builder()
                                    .setEndpoint(Utility.urlApiInfosFlash).build();

                            InstantInterface instantDriverService2  = restAdapter2.create(InstantInterface.class);

                            instantDriverService2.getAllFromEmail(Utility.getPreferredEmail(getContext()), new Callback<List<InformationDTO>>() {
                                @Override
                                public void success(List<InformationDTO> informationDTOs, Response response) {

                                    String yearQuery = Utility.getPreferredYear(getContext());
                                    String monthQuery = Utility.getPreferredMonth(getContext());

                                    long idYearMonth = addYearMonth(yearQuery, monthQuery);

                          //          Log.d(LOG_TAG, "************************************************* Nbre d'information de la liste InformationDTO = " + informationDTOs.size());

                                    Vector<ContentValues> cVVector = new Vector<ContentValues>(informationDTOs.size());


                                    for (InformationDTO info : informationDTOs) {

                                        // These are the values that will be collected.
                                        String objet;
                                        String message;
                                        String run_date;
                                        long informationId;
                                        String telephoneDelegue;
                                        String prenomDelegue;
                                        String fichier;
                                        String url;

                                        // Get the JSON object representing the
                                        objet = info.getObjet();
                                        message = info.getMessage();
                                        run_date = info.getDateEnregistrement();
                                        run_date = run_date.substring(0,16);
                                        informationId = info.getInformationId();
                                        telephoneDelegue = info.getTelephoneDelegue();
                                        prenomDelegue = info.getNomDelegue();
                                        fichier = info.getFichier();
                                        url = info.getUrl();

                                        ContentValues messagesValues = new ContentValues();

                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_YEARMONTH_KEY, idYearMonth);
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_OBJET, objet);
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_MESSAGE, message);
                                        //   doodleValues.put(InstantContract.DoodleEntry.COLUMN_URL, url);
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_RUN_DATE, run_date);
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_INFORMATION_ID,Integer.valueOf(informationId+""));
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_TELEPHONE_DELEGUE,telephoneDelegue);
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_PRENOM_DELEGUE,prenomDelegue);
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_FICHIER,fichier);
                                        messagesValues.put(InstantContract.DoodleEntry.COLUMN_URL,url);

                                        cVVector.add(messagesValues);

                                    }

                                    // add to database
                                    if (cVVector.size() > 0) {

                                        Uri doodleUri = InstantContract.DoodleEntry.buildDoodleYearMonth(
                                                yearQuery, monthQuery);

                                        Cursor cursorDoodle = getContext().getContentResolver().query(doodleUri, null, null, null, null);

                                        int nbreNouveauMessage = cVVector.size() - cursorDoodle.getCount();
                                        if (nbreNouveauMessage < 0) nbreNouveauMessage = 0;
                                        //delete Doodle where YEARMONTH_KEY = yearmonthId
                                   //     Log.d(LOG_TAG, "Delete info where YEARMONTH_KEY =" + 1);
                                        getContext().getContentResolver().delete(InstantContract.DoodleEntry.CONTENT_URI,
                                                InstantContract.DoodleEntry.COLUMN_YEARMONTH_KEY + " = ? ", new String[]{Long.toString(idYearMonth)});

                                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                                        cVVector.toArray(cvArray);
                                        getContext().getContentResolver().bulkInsert(InstantContract.DoodleEntry.CONTENT_URI, cvArray);

                                 //       Log.d(LOG_TAG, "Info Archive  Complete. " + cVVector.size() + " Inserted");

                                        if ((nbreNouveauMessage!=1)&&(nbreNouveauMessage!=0)) notifyDoodle(nbreNouveauMessage);
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d(LOG_TAG, "Erreur lors du consommation du service");
                                    int i=0;
                                }
                            });

                        }


                    }

                }

                @Override
                public void failure(RetrofitError error) {

                    Log.e(LOG_TAG, "Server Unavailable at the moment!!!");

                }
            });







        }else {
            Utility.initialiseUrl(getContext());
        }
    }


    /**
     * Helper method to handle insertion of a new Year Month in the yearmonth database.
     *
     * @param yearSetting The year string used to request updates from the server.
     * @param monthSetting The year string used to request updates from the server.
     * @return the row ID of the added year Month.
     */
    long addYearMonth(String yearSetting,String monthSetting) {
        long yearmonthId;

        // First, check if the yearmonth exists in the db
        Cursor yearmonthCursor = getContext().getContentResolver().query(
                InstantContract.YearMonthEntry.CONTENT_URI,
                new String[]{InstantContract.YearMonthEntry._ID},
                InstantContract.YearMonthEntry.COLUMN_YEAR_SETTING + " = ? AND "+ InstantContract.YearMonthEntry.COLUMN_MONTH_SETTING + " = ? ",
                new String[]{yearSetting,monthSetting},
                null);

        if (yearmonthCursor.moveToFirst()) {
            int yearmonthIdIndex = yearmonthCursor.getColumnIndex(InstantContract.YearMonthEntry._ID);
            yearmonthId = yearmonthCursor.getLong(yearmonthIdIndex);
           // Log.d(LOG_TAG, "Year and Month already exit with Id=" + yearmonthId);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues yearmonthValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            yearmonthValues.put(InstantContract.YearMonthEntry.COLUMN_YEAR_SETTING, yearSetting);
            yearmonthValues.put(InstantContract.YearMonthEntry.COLUMN_MONTH_SETTING, monthSetting);

            // Finally, insert YearMonth data into the database.
            Uri insertedUri = getContext().getContentResolver().insert(
                    InstantContract.YearMonthEntry.CONTENT_URI,
                    yearmonthValues
            );

            // The resulting URI contains the ID for the row.  Extract the yearmonthId from the Uri.
            yearmonthId = ContentUris.parseId(insertedUri);

           // Log.d(LOG_TAG, "Insert new Year and Month with Id = " + yearmonthId);

        }

        yearmonthCursor.close();
        // Wait, that worked?  Yes!
        return yearmonthId;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        DoodlesArchiveSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


    private void notifyDoodle(int nbreMessage) {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

                //set year and month to current value
                Utility.setPreferredYear(context, "" + Calendar.getInstance().get(Calendar.YEAR));
                int monthCurrent = Calendar.getInstance().get(Calendar.MONTH)+1;
                Utility.setPreferredMonth(context, "" + monthCurrent);

                String yearQuery = Utility.getPreferredYear(context);
                String monthQuery = Utility.getPreferredMonth(context);
                String daysQuery = "" + Calendar.getInstance().get(Calendar.DATE);


                Uri doodleUri =  InstantContract.DoodleEntry.buildDoodleYearMonth(yearQuery, monthQuery);


                //Uri doodleUri = InstantContract.DoodleEntry.buildDoodleYearMonthWithRunDate(yearQuery, monthQuery, yearQuery + "/" + monthQuery + "/" + daysQuery);

                // we'll query our contentProvider, as always
                Cursor cursor = context.getContentResolver().query(doodleUri, NOTIFY_DOODLE_PROJECTION, null, null, null);

                if (cursor.moveToFirst()) {

                    // define sound URI, the sound to be played when there's a notification
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                    String titleDoodle = cursor.getString(INDEX_OBJET);
                    String runDate = Utility.formatDate(cursor.getString(INDEX_RUN_DATE));
                    int iconId = R.drawable.ic_launcher;

                    // Define the text of the Doodle.
                    String contentText = String.format(context.getString(R.string.format_notification),
                            titleDoodle,
                            runDate);

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setSmallIcon(iconId)
                                    .setContentTitle(nbreMessage + getContext().getResources().getString(R.string.notify))
                                    .setSound(soundUri)
                                    .setContentText(contentText);


                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // DOODLE_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(DOODLE_NOTIFICATION_ID, mBuilder.build());


                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.commit();
                }

                cursor.close();
            ////}
        }

    }



}