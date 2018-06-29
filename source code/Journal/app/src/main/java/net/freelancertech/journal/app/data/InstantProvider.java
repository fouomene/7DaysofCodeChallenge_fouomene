package net.freelancertech.journal.app.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class InstantProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private InstantDbHelper mOpenHelper;

    static final int DOODLE = 100;
    static final int DOODLE_WITH_YEAR_AND_MONTH = 101;
    static final int DOODLE_WITH_YEAR_AND_MONTH_AND_IDDOODLE = 102;
    static final int DOODLE_WITH_YEAR_AND_MONTH_AND_RUNDATE = 103;
    static final int DOODLE_WITH_YEAR_AND_MONTH_AND_MOTCLE = 104;
    static final int DOODLE_WITH_YEAR_AND_MONTH_AND_MONMOTCLE = 105;
    static final int YEARMONTH = 300;
    private final String LOG_TAG = InstantProvider.class.getSimpleName();

    private static final SQLiteQueryBuilder sDoodleByYearMonthSettingQueryBuilder;

    static{
        sDoodleByYearMonthSettingQueryBuilder = new SQLiteQueryBuilder();
        sDoodleByYearMonthSettingQueryBuilder.setTables(
                InstantContract.DoodleEntry.TABLE_NAME + " INNER JOIN " +
                        InstantContract.YearMonthEntry.TABLE_NAME +
                        " ON " + InstantContract.DoodleEntry.TABLE_NAME +
                        "." + InstantContract.DoodleEntry.COLUMN_YEARMONTH_KEY +
                        " = " + InstantContract.YearMonthEntry.TABLE_NAME +
                        "." + InstantContract.YearMonthEntry._ID);
    }


    private static final String sYearMonthSettingSelection =
            InstantContract.YearMonthEntry.TABLE_NAME+
                    "." + InstantContract.YearMonthEntry.COLUMN_YEAR_SETTING+ " = ? AND "+
                    InstantContract.YearMonthEntry.COLUMN_MONTH_SETTING+ " = ? ";

    private static final String sMotCleSelection =
            InstantContract.YearMonthEntry.TABLE_NAME+
                    "." + InstantContract.YearMonthEntry.COLUMN_YEAR_SETTING+ " = ? AND "+
                    InstantContract.YearMonthEntry.COLUMN_MONTH_SETTING+ " = ? AND ("+
                    InstantContract.DoodleEntry.TABLE_NAME+
                    "." + InstantContract.DoodleEntry.COLUMN_OBJET+ " LIKE ? OR "+
                    InstantContract.DoodleEntry.COLUMN_MESSAGE+ " LIKE ? )";


    private static final String sIdDoodleSelection =
            InstantContract.DoodleEntry.TABLE_NAME+
                    "." + InstantContract.DoodleEntry._ID + " = ? ";

    private static final String sRunDateSelection =
            InstantContract.DoodleEntry.TABLE_NAME+
                    "." + InstantContract.DoodleEntry.COLUMN_RUN_DATE + " = ? ";

    private Cursor getDoodleByYearMonthSetting(Uri uri, String[] projection, String sortOrder) {

         String yearSetting = InstantContract.DoodleEntry.getYearSettingFromUri(uri);
         String monthSetting = InstantContract.DoodleEntry.getMonthSettingFromUri(uri);

         String[] selectionArgs;
         String selection;
         selection = sYearMonthSettingSelection;
         selectionArgs = new String[]{yearSetting,monthSetting};
         Cursor doodleCursor = sDoodleByYearMonthSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                 projection,
                 selection,
                 selectionArgs,
                 null,
                 null,
                 sortOrder
         );

        Log.e(LOG_TAG, "Number CursorDoodle v4 = " + doodleCursor.getCount());

        return doodleCursor;
    }


    private Cursor getDoodleByYearMonthSettingWithMotCle(Uri uri, String[] projection, String sortOrder) {

        String yearSetting = InstantContract.DoodleEntry.getYearSettingFromUri(uri);
        String monthSetting = InstantContract.DoodleEntry.getMonthSettingFromUri(uri);
        String motCle = InstantContract.DoodleEntry.getMotCleFromUri(uri);

        String[] selectionArgs;
        String selection;
        selection = sMotCleSelection;
        selectionArgs = new String[]{yearSetting,monthSetting,"%" + motCle + "%","%" + motCle + "%"};
        Cursor doodleCursor = sDoodleByYearMonthSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Log.e(LOG_TAG, "Number CursorDoodle v4 = " + doodleCursor.getCount());

        return doodleCursor;
    }

    private Cursor getDoodleByYearMonthSettingWithIdDoodle(Uri uri, String[] projection, String sortOrder) {

        String idDoodle = InstantContract.DoodleEntry.getIdDoodleFromUri(uri);

        String[] selectionArgs;
        String selection;
        selection = sIdDoodleSelection;
        selectionArgs = new String[]{idDoodle};
        Cursor doodleCursor = sDoodleByYearMonthSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Log.e(LOG_TAG, "Number CursorDoodle v5 = " + doodleCursor.getCount());

        return doodleCursor;
    }



    private Cursor getDoodleByYearMonthSettingWithRundate(Uri uri, String[] projection, String sortOrder) {

        String runDate = InstantContract.DoodleEntry.getRunDateFromUri(uri);

        String[] selectionArgs;
        String selection;
        selection = sRunDateSelection;
        selectionArgs = new String[]{runDate};
        Cursor doodleCursor = sDoodleByYearMonthSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Log.e(LOG_TAG, "Number CursorDoodle v6 = " + doodleCursor.getCount());

        return doodleCursor;
    }


    /*
       Here is where we need to create the UriMatcher.
     */
    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = InstantContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, InstantContract.PATH_DOODLE, DOODLE);
        matcher.addURI(authority, InstantContract.PATH_DOODLE + "/*/*", DOODLE_WITH_YEAR_AND_MONTH);
        matcher.addURI(authority, InstantContract.PATH_DOODLE + "/*/*/#", DOODLE_WITH_YEAR_AND_MONTH_AND_IDDOODLE);
       // matcher.addURI(authority, InstantContract.PATH_DOODLE + "/*/*/*", DOODLE_WITH_YEAR_AND_MONTH_AND_RUNDATE);
        matcher.addURI(authority, InstantContract.PATH_DOODLE + "/*/*/*", DOODLE_WITH_YEAR_AND_MONTH_AND_MOTCLE);
        matcher.addURI(authority, InstantContract.PATH_YEAR_MONTH, YEARMONTH);
        return matcher;
    }

    /*
        We just create a new InstantDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new InstantDbHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DOODLE_WITH_YEAR_AND_MONTH_AND_MOTCLE:
                return InstantContract.DoodleEntry.CONTENT_ITEM_TYPE;
           /* case DOODLE_WITH_YEAR_AND_MONTH_AND_RUNDATE:
                return InstantContract.DoodleEntry.CONTENT_ITEM_TYPE;*/
            case DOODLE_WITH_YEAR_AND_MONTH_AND_IDDOODLE:
                return InstantContract.DoodleEntry.CONTENT_ITEM_TYPE;
            case DOODLE_WITH_YEAR_AND_MONTH:
                return InstantContract.DoodleEntry.CONTENT_ITEM_TYPE;
            case DOODLE:
                return InstantContract.DoodleEntry.CONTENT_TYPE;
            case YEARMONTH:
                return InstantContract.YearMonthEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "info/*/*/*"
            case DOODLE_WITH_YEAR_AND_MONTH_AND_MOTCLE:
            {
                retCursor = getDoodleByYearMonthSettingWithMotCle(uri, projection, sortOrder);
                break;
            }
           // "info/*/*/*"
           /*  case DOODLE_WITH_YEAR_AND_MONTH_AND_RUNDATE:
            {
                retCursor = getDoodleByYearMonthSettingWithRundate(uri, projection, sortOrder);
                break;
            }*/
            // "info/*/*/#"
            case DOODLE_WITH_YEAR_AND_MONTH_AND_IDDOODLE:
            {
                retCursor = getDoodleByYearMonthSettingWithIdDoodle(uri, projection, sortOrder);
                break;
            }
            // "info/*/*"
            case DOODLE_WITH_YEAR_AND_MONTH:
            {
                retCursor = getDoodleByYearMonthSetting(uri, projection, sortOrder);
                break;
            }
            // "info"
            case DOODLE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        InstantContract.DoodleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "yearmonth"
            case YEARMONTH: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        InstantContract.YearMonthEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case DOODLE: {
                long _id = db.insert(InstantContract.DoodleEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = InstantContract.DoodleEntry.buildDoodleUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case YEARMONTH: {
                long _id = db.insert(InstantContract.YearMonthEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = InstantContract.YearMonthEntry.buildYearMonthUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case DOODLE:
                rowsDeleted = db.delete(
                        InstantContract.DoodleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case YEARMONTH:
                rowsDeleted = db.delete(
                        InstantContract.YearMonthEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case DOODLE:
                rowsUpdated = db.update(InstantContract.DoodleEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case YEARMONTH:
                rowsUpdated = db.update(InstantContract.YearMonthEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOODLE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(InstantContract.DoodleEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // we do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. we can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
