/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
package net.freelancertech.journal.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import net.freelancertech.journal.app.data.InstantContract;
import net.freelancertech.journal.app.data.InstantDbHelper;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(InstantDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Uncomment this test once you've written the code to create the YearMonth
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the YearMonth table has the correct columns, since we
        give you the code for the Doodle table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(InstantContract.YearMonthEntry.TABLE_NAME);
        tableNameHashSet.add(InstantContract.DoodleEntry.TABLE_NAME);

        mContext.deleteDatabase(InstantDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new InstantDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the yearmonth entry
        // and note entry tables
        assertTrue("Error: Your database was created without both the yearmonth entry and doodle entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + InstantContract.YearMonthEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> yearmonthColumnHashSet = new HashSet<String>();
        yearmonthColumnHashSet.add(InstantContract.YearMonthEntry._ID);
        yearmonthColumnHashSet.add(InstantContract.YearMonthEntry.COLUMN_YEAR_SETTING);
        yearmonthColumnHashSet.add(InstantContract.YearMonthEntry.COLUMN_MONTH_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            yearmonthColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required yearmonth
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required yearmonth entry columns",
                yearmonthColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Here is where you will build code to test that we can insert and query the
        yearmonth database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createYearMonthValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testYearMonthTable() {
        insertYearMonth();
    }

    /*
        Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createDoodleValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testDoodleTable() {
        // First insert the yearmonth, and then use the yearmonthRowId to insert
        // the note. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testYearMonthTable
        // we can move this code to insertYearMonth and then call insertYearMonth from both
        // tests. Why move it? We need the code to return the ID of the inserted yearmonth
        // and our testDoodleTable can only return void because it's a test.

        long yearmonthRowId = insertYearMonth();

        // Make sure we have a valid row ID.
        assertFalse("Error: YearMonth Not Inserted Correctly", yearmonthRowId == -1L);

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        InstantDbHelper dbHelper = new InstantDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Note): Create Note values
        ContentValues doodleValues = TestUtilities.createDoodleValues(yearmonthRowId);

        // Third Step (Note): Insert ContentValues into database and get a row ID back
        long doodleRowId = db.insert(InstantContract.DoodleEntry.TABLE_NAME, null, doodleValues);
        assertTrue(doodleRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor doodleCursor = db.query(
                InstantContract.DoodleEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from yearmonth query", doodleCursor.moveToFirst() );

        // Fifth Step: Validate the yearmonth Query
        TestUtilities.validateCurrentRecord("testInsertReadDb DoodleEntry failed to validate",
                doodleCursor, doodleValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from doodle query",
                doodleCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        doodleCursor.close();
        dbHelper.close();
    }


    /*
       This is a helper method for the testDoodleTable quiz. You can move your
        code from testDoodleTable to here so that you can call this code from both
        testDoodleTable and testDoodleTable.
     */
    public long insertYearMonth() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        InstantDbHelper dbHelper = new InstantDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createYearMonthValues if you wish)
        ContentValues testValues = TestUtilities.createYearMonthValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long yearmonthRowId;
        yearmonthRowId = db.insert(InstantContract.YearMonthEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(yearmonthRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                InstantContract.YearMonthEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from yearmonth query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: YearMonth Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from yearmonth query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return yearmonthRowId;
    }
}
