package net.freelancertech.journal.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.freelancertech.journal.app.data.InstantContract.DoodleEntry;
import net.freelancertech.journal.app.data.InstantContract.YearMonthEntry;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 *
 * Manages a local database for doodle data.
 */
public class InstantDbHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "journal.db";

    public InstantDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold yearmonth.
        final String SQL_CREATE_YEARMONTH_TABLE = "CREATE TABLE " + YearMonthEntry.TABLE_NAME + " (" +
                YearMonthEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                YearMonthEntry.COLUMN_YEAR_SETTING+ " TEXT NOT NULL, " +
                YearMonthEntry.COLUMN_MONTH_SETTING+ " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_DOODLE_TABLE = "CREATE TABLE " + DoodleEntry.TABLE_NAME + " (" +

                DoodleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the yearmonth entry associated with this info or note data
                DoodleEntry.COLUMN_YEARMONTH_KEY + " INTEGER NOT NULL, " +
                DoodleEntry.COLUMN_OBJET + " TEXT NOT NULL, " +
                DoodleEntry.COLUMN_MESSAGE + " TEXT NOT NULL, " +
                DoodleEntry.COLUMN_RUN_DATE + " TEXT NOT NULL, " +
                DoodleEntry.COLUMN_INFORMATION_ID + " INTEGER NOT NULL, " +
                DoodleEntry.COLUMN_TELEPHONE_DELEGUE + " TEXT NOT NULL, " +
                DoodleEntry.COLUMN_PRENOM_DELEGUE + " TEXT NOT NULL, " +
                DoodleEntry.COLUMN_FICHIER + " TEXT NOT NULL, " +
                DoodleEntry.COLUMN_URL + " TEXT NOT NULL, " +

                // Set up the yearmonth column as a foreign key to yearmonth table.
                " FOREIGN KEY (" + DoodleEntry.COLUMN_YEARMONTH_KEY + ") REFERENCES " +
                YearMonthEntry.TABLE_NAME + " (" + YearMonthEntry._ID + ") );";

        sqLiteDatabase.execSQL(SQL_CREATE_YEARMONTH_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DOODLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + YearMonthEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DoodleEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
