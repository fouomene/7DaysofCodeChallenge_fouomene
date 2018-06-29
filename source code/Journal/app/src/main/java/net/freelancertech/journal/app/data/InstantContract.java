package net.freelancertech.journal.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class InstantContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "net.freelancertech.journal.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://net.freelancertech.doodlesarchive.app/info/ is a valid path for
    // looking at info or note data. content://net.freelancertech.doodlesarchive.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_DOODLE = "info";
    public static final String PATH_YEAR_MONTH = "yearmonth";


    /* Inner class that defines the table contents of the yearmonth table */
    public static final class YearMonthEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_YEAR_MONTH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_YEAR_MONTH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_YEAR_MONTH;

        // Table name
        public static final String TABLE_NAME = "yearmonth";

        // The year setting string is what will be sent to info or note
        // as the year query.
        public static final String COLUMN_YEAR_SETTING = "year_setting";

        // The Month setting string is what will be sent to info or note
        // as the month query.
        public static final String COLUMN_MONTH_SETTING = "month_setting";


        public static Uri buildYearMonthUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the info or note table */
    public static final class DoodleEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DOODLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOODLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOODLE;

        public static final String TABLE_NAME = "info";

        // Column with the foreign key into the yearmonth table.
        public static final String COLUMN_YEARMONTH_KEY = "yearmonth_id";

        //Name of the note,
        public static final String COLUMN_OBJET = "objet";

        //Title of the note,
        public static final String COLUMN_MESSAGE = "message";

        //Run_Date of the note,
        public static final String COLUMN_RUN_DATE = "date";

        //Information Id,
        public static final String COLUMN_INFORMATION_ID = "informationid";

        //Telephone Delegue,
        public static final String COLUMN_TELEPHONE_DELEGUE = "telephonedelegue";

        //Prenom Delegue,
        public static final String COLUMN_PRENOM_DELEGUE = "prenomdelegue";

        //fichier,
        public static final String COLUMN_FICHIER = "fichier";

        //url,
        public static final String COLUMN_URL = "url";


        public static Uri buildDoodleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildDoodleYearMonth(String yearSetting, String monthSetting) {
            return CONTENT_URI.buildUpon().appendPath(yearSetting)
                    .appendPath(monthSetting).build();
        }

        public static Uri buildDoodleYearMonthWithIdDoodle(String yearSetting, String monthSetting, long id) {
            return CONTENT_URI.buildUpon().appendPath(yearSetting)
                    .appendPath(monthSetting)
                    .appendPath(Long.toString(id)).build();
        }

        public static Uri buildDoodleYearMonthWithRunDate(String yearSetting, String monthSetting, String runDate) {
            return CONTENT_URI.buildUpon().appendPath(yearSetting)
                    .appendPath(monthSetting)
                    .appendPath(runDate).build();
        }

        public static Uri buildDoodleYearMonthWithMotCle(String yearSetting, String monthSetting, String motCle) {
            return CONTENT_URI.buildUpon().appendPath(yearSetting)
                    .appendPath(monthSetting)
                    .appendPath(motCle).build();
        }


        public static String getYearSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getMonthSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getMotCleFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        public static String getIdDoodleFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        public static String getRunDateFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }


    }
}
