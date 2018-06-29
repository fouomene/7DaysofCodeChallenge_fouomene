/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
package net.freelancertech.journal.app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import net.freelancertech.journal.app.data.InstantContract.DoodleEntry;
import net.freelancertech.journal.app.data.InstantContract.YearMonthEntry;
import net.freelancertech.journal.app.data.InstantDbHelper;

/*
    Note: This is not a complete set of tests of the Journal Doodle ContentProvider, but it does test
    that at least the basic functionality has been implemented correctly.

    Uncomment the tests in this class as you implement the functionality in your
    ContentProvider to make sure that you've implemented things reasonably correctly.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
   This helper function deletes all records from both database tables using the ContentProvider.
   It also queries the ContentProvider to make sure that the database has been successfully
   deleted, so it cannot be used until the Query and Delete functions have been written
   in the ContentProvider.

   Replace the calls to deleteAllRecordsFromDB with this one after you have written
   the delete functionality in the ContentProvider.
 */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                DoodleEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                YearMonthEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                DoodleEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Doodle table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                YearMonthEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from YearMonth table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
         Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
             Uncomment this test to verify that your implementation of GetType is
            functioning correctly.
         */
    public void testGetType() {
        // content://net.freelancertech.journal.app/doodle/
        String type = mContext.getContentResolver().getType(DoodleEntry.CONTENT_URI);
        // vnd.android.cursor.dir/net.freelancertech.journal.app/doodle
        assertEquals("Error: the DoodleEntry CONTENT_URI should return DoodleEntry.CONTENT_TYPE",
                DoodleEntry.CONTENT_TYPE, type);
        Log.d(LOG_TAG, "type :" + type);

        String testYear = "1995";
        String testMonth = "4";
        // content://net.freelancertech.journal.app/doodle/1995/4
        type = mContext.getContentResolver().getType(
                DoodleEntry.buildDoodleYearMonth(testYear, testMonth));
        // vnd.android.cursor.dir/net.freelancertech.journal.app/doodle
        assertEquals("Error: the DoodleEntry CONTENT_URI with yearmonth should return DoodleEntry.CONTENT_TYPE",
                DoodleEntry.CONTENT_TYPE, type);

        String testRunDate = "1995/4/9";
        // content://net.freelancertech.journal.app/doodle/1995/4/1995/4/9
        type = mContext.getContentResolver().getType(
                DoodleEntry.buildDoodleYearMonthWithRunDate(testYear, testMonth, testRunDate));
        // vnd.android.cursor.item/net.freelancertech.journal.app/doodle/1995/4/1995/4/9
        assertEquals("Error: the DoodleEntry CONTENT_URI with yearmonth and date should return DoodleEntry.CONTENT_ITEM_TYPE",
                DoodleEntry.CONTENT_ITEM_TYPE, type);

        // content://net.freelancertech.journal.app/year/month
        type = mContext.getContentResolver().getType(DoodleEntry.CONTENT_URI);
        // vnd.android.cursor.dir/net.freelancertech.journal.app/year/month
        assertEquals("Error: the YearMonthEntry CONTENT_URI should return YearmonthEntry.CONTENT_TYPE",
                YearMonthEntry.CONTENT_TYPE, type);
    }

    /*
         This test uses the database directly to insert and then uses the ContentProvider to
         read out the data.  Uncomment this test to see if the basic doodle query functionality
         given in the ContentProvider is working correctly.
      */
    public void testBasicDoodleQuery() {
        // insert our test records into the database
        InstantDbHelper dbHelper = new InstantDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createYearMonthValues();
        long yearmonthRowId = TestUtilities.insertYearMonthValues(mContext);

        // Fantastic.  Now that we have a location, add some doodle!
        ContentValues doodleValues = TestUtilities.createDoodleValues(yearmonthRowId);

        long doodleRowId = db.insert(DoodleEntry.TABLE_NAME, null, doodleValues);
        assertTrue("Unable to Insert DoodleEntry into the Database", doodleRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor doodleCursor = mContext.getContentResolver().query(
                DoodleEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicDoodleQuery", doodleCursor, doodleValues);
    }

    /*
     This test uses the database directly to insert and then uses the ContentProvider to
     read out the data.  Uncomment this test to see if your yearmonth queries are
     performing correctly.
  */
    public void testBasicYearMonthQueries() {
        // insert our test records into the database
        InstantDbHelper dbHelper = new InstantDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createYearMonthValues();
        long yearmonthRowId = TestUtilities.insertYearMonthValues(mContext);

        // Test the basic content provider query
        Cursor yearmonthCursor = mContext.getContentResolver().query(
                YearMonthEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicYearMonthQueries, yearmonth query", yearmonthCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: YearMonth Query did not properly set NotificationUri",
                    yearmonthCursor.getNotificationUri(), YearMonthEntry.CONTENT_URI);
        }
    }

    /*
         This test uses the provider to insert and then update the data. Uncomment this test to
         see if your update yearmonth is functioning correctly.
      */
    public void testUpdateYearMonth() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createYearMonthValues();

        Uri yearmonthUri = mContext.getContentResolver().
                insert(YearMonthEntry.CONTENT_URI, values);
        long yearmonthRowId = ContentUris.parseId(yearmonthUri);

        // Verify we got a row back.
        assertTrue(yearmonthRowId != -1);
        Log.d(LOG_TAG, "New row id: " + yearmonthRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(YearMonthEntry._ID, yearmonthRowId);
        updatedValues.put(YearMonthEntry.COLUMN_YEAR_SETTING, "1995");
        updatedValues.put(YearMonthEntry.COLUMN_MONTH_SETTING, "6");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor yearmonthCursor = mContext.getContentResolver().query(YearMonthEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        yearmonthCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                YearMonthEntry.CONTENT_URI, updatedValues, YearMonthEntry._ID + "= ?",
                new String[] { Long.toString(yearmonthRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        yearmonthCursor.unregisterContentObserver(tco);
        yearmonthCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                YearMonthEntry.CONTENT_URI,
                null,   // projection
                YearMonthEntry._ID + " = " + yearmonthRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateYearMonth.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }


    // Make sure we can still delete after adding/updating stuff
    //
    // Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createYearMonthValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(YearMonthEntry.CONTENT_URI, true, tco);
        Uri yearmonthUri = mContext.getContentResolver().insert(YearMonthEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert yearmonth
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long yearmonthRowId = ContentUris.parseId(yearmonthUri);

        // Verify we got a row back.
        assertTrue(yearmonthRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                YearMonthEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating YearMonthEntry.",
                cursor, testValues);

        // Fantastic.  Now that we have a yearmonth, add some Doodle!
        ContentValues doodleValues = TestUtilities.createDoodleValues(yearmonthRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(DoodleEntry.CONTENT_URI, true, tco);

        Uri doodleInsertUri = mContext.getContentResolver()
                .insert(DoodleEntry.CONTENT_URI, doodleValues);
        assertTrue(doodleInsertUri != null);

        // Did our content observer get called? If this fails, your insert note
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor doodleCursor = mContext.getContentResolver().query(
                DoodleEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating DoodleEntry insert.",
               doodleCursor, doodleValues);

        // Add the note values in with the Note data so that we can make
        // sure that the join worked and we actually get all the values back
        doodleValues.putAll(testValues);

        // Get the joined Note and YearMonth data
        doodleCursor = mContext.getContentResolver().query(
                DoodleEntry.buildDoodleYearMonth(TestUtilities.TEST_YEAR, TestUtilities.TEST_MONTH),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Doodle and YearMonth Data.",
                doodleCursor, doodleValues);

        // Get the joined Note and YearMonth data with a RunDate
        doodleCursor = mContext.getContentResolver().query(
                DoodleEntry.buildDoodleYearMonthWithRunDate(
                        TestUtilities.TEST_YEAR, TestUtilities.TEST_YEAR,"1995/4/9"),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Doodle and YearMonth Data with RunDate.",
                doodleCursor, doodleValues);

    }


    // Make sure we can still delete after adding/updating stuff
    //
    // Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our yearmonth delete.
        TestUtilities.TestContentObserver yearmonthObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(YearMonthEntry.CONTENT_URI, true, yearmonthObserver);

        // Register a content observer for our note delete.
        TestUtilities.TestContentObserver doodleObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoodleEntry.CONTENT_URI, true, doodleObserver);

        deleteAllRecordsFromProvider();

        //  If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        yearmonthObserver.waitForNotificationOrFail();
        doodleObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(yearmonthObserver);
        mContext.getContentResolver().unregisterContentObserver(doodleObserver);
    }



}
