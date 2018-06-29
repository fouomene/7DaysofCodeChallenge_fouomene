package net.freelancertech.journal.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.freelancertech.journal.app.data.InstantContract;
import net.freelancertech.journal.app.sync.DoodlesArchiveSyncAdapter;
import net.freelancertech.journal.app.utils.Utility;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * Created by FOUOMENE on 06/2018.
 * EmailAuthor:  fouomenedaniel@gmail.com .
 *
 * Encapsulates fetching the note and displaying it as a {@link ListView} layout.
 */
public class DoodleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private DoodleAdapter mDoodleAdapter;

    private final String LOG_TAG = DoodleFragment.class.getSimpleName();

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private FloatingActionButton fab;
    private FloatingActionButton fabSearch;
    private FloatingActionButton fabHome;
    private String isDelegue ;
    private String filterStr = null;

    private static final String SELECTED_KEY = "selected_position";

    private static final int DOODLE_LOADER = 0;
    private static final int DOODLE_LOADER_SEARCH = 1;
    private static final int DOODLE_LOADER_MYSEARCH = 2;

    // For the doodle view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] DOODLE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the yearmonth & doodle tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the doodle table
            // using the year and month set by the user, which is only in the yearmonth table.
            // So the convenience is worth it.
            InstantContract.DoodleEntry.TABLE_NAME + "." + InstantContract.DoodleEntry._ID,
            InstantContract.DoodleEntry.COLUMN_OBJET,
            InstantContract.DoodleEntry.COLUMN_FICHIER,
            InstantContract.DoodleEntry.COLUMN_RUN_DATE,
            InstantContract.DoodleEntry.COLUMN_INFORMATION_ID,
            InstantContract.YearMonthEntry.COLUMN_YEAR_SETTING,
            InstantContract.YearMonthEntry.COLUMN_MONTH_SETTING
    };

    // These indices are tied to DOODLE_COLUMNS.  If DOODLE_COLUMNS changes, these
    // must change.
    static final int COL_DOODLE_ID = 0;
    static final int COL_DOODLE_OBJET = 1;
    static final int COL_DOODLE_FICHIER = 2;
    static final int COL_DOODLE_RUN_DATE = 3;
    static final int COL_DOODLE_INFORMATION_ID = 4;
    static final int COL_YEAR_MONTH_SETTING = 5;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri,String idInformation);
    }

    public DoodleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(net.freelancertech.journal.app.R.menu.doodlefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The DoodleAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mDoodleAdapter = new DoodleAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(net.freelancertech.journal.app.R.layout.fragment_main, container, false);

        isDelegue = Utility.getPreferredIsDelegue(getActivity());

        fabSearch = (FloatingActionButton) rootView .findViewById(R.id.fab_search);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog diaBox = EditAskOption();
                diaBox.show();
            }
        });


        fab = (FloatingActionButton) rootView .findViewById(net.freelancertech.journal.app.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SendActivity.class);
                startActivity(intent);
            }
        });

        fabHome = (FloatingActionButton) rootView .findViewById(R.id.fab_home);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rechercherOffres("vide");
                updateDoodle();


            }
        });

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(net.freelancertech.journal.app.R.id.listview_doodle);
        mListView.setAdapter(mDoodleAdapter);
        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String yearSetting = Utility.getPreferredYear(getActivity());
                    String monthSetting = Utility.getPreferredMonth(getActivity());
                    Log.d(LOG_TAG, "URI = " + InstantContract.DoodleEntry.buildDoodleYearMonthWithIdDoodle(
                            yearSetting, monthSetting, cursor.getLong(COL_DOODLE_ID)
                    ).toString());

                   // Log.d(LOG_TAG, "IDINFORMATION = " + cursor.getLong(COL_DOODLE_INFORMATION_ID));

                    ((Callback) getActivity())
                            .onItemSelected(InstantContract.DoodleEntry.buildDoodleYearMonthWithIdDoodle(
                                    yearSetting,monthSetting,cursor.getLong(COL_DOODLE_ID)
                            ),cursor.getLong(COL_DOODLE_INFORMATION_ID)+"");
                }
                mPosition = position;
                Utility.setPreferredPosition(getActivity(),Integer.toString(mPosition));
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    public boolean isOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DOODLE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    // since we read the year month when we create the loader, all we need to do is restart things
    void onYearMonthChanged() {
        updateDoodle();

        String monmotcle = Utility.getPreferredMonMotcle(getActivity());
        Utility.setPreferredMotcle(getActivity(),monmotcle);

        if ("vide".equals(monmotcle)) {
            getLoaderManager().restartLoader(DOODLE_LOADER, null, this);

        }else{
            getLoaderManager().restartLoader(DOODLE_LOADER_MYSEARCH, null, this);
        }

    }

    private void updateDoodle() {

        DoodlesArchiveSyncAdapter.syncImmediately(getActivity());


    }


    @Override
    public void onStart() {
        super.onStart();
        mPosition = Integer.parseInt(Utility.getPreferredPosition(getActivity()));
        //updateDoodle();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.


        // Sort order:  Ascending, by id.
       // String sortOrder = InstantContract.DoodleEntry.COLUMN_RUN_DATE + " ASC";

        String yearSetting = Utility.getPreferredYear(getActivity());
        String monthSetting = Utility.getPreferredMonth(getActivity());

        Uri doodleUri ;
        //   Log.e(LOG_TAG, "URI =  " + doodleForYearMonthUri.toString());
        //InstantContract.DoodleEntry.TABLE_NAME + "." + InstantContract.DoodleEntry._ID + " DESC"


        if (i == 1){

            filterStr = Utility.getPreferredMotcle(getActivity());

            doodleUri = InstantContract.DoodleEntry.buildDoodleYearMonthWithMotCle(
                    yearSetting, monthSetting, filterStr);


            return new CursorLoader(getActivity(),
                    doodleUri,
                    DOODLE_COLUMNS,
                    null,
                    null,
                    null);
        }
        else if (i == 2){

            filterStr = Utility.getPreferredMonMotcle(getActivity());

            doodleUri = InstantContract.DoodleEntry.buildDoodleYearMonthWithMotCle(
                    yearSetting, monthSetting, filterStr);

            return new CursorLoader(getActivity(),
                    doodleUri,
                    DOODLE_COLUMNS,
                    null,
                    null,
                    null);

        }

        filterStr = Utility.getPreferredMotcle(getActivity());

        if (!"vide".equals(filterStr)){

            doodleUri = InstantContract.DoodleEntry.buildDoodleYearMonthWithMotCle(
                    yearSetting, monthSetting, filterStr);


            return new CursorLoader(getActivity(),
                    doodleUri,
                    DOODLE_COLUMNS,
                    null,
                    null,
                    null);
        }

        doodleUri = InstantContract.DoodleEntry.buildDoodleYearMonth(yearSetting, monthSetting);

       /* doodleForYearMonthUri = InstantContract.DoodleEntry.buildDoodleYearMonthWithMotCle(
                yearSetting, monthSetting,"informatique");*/

        return new CursorLoader(getActivity(),
                doodleUri,
                DOODLE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDoodleAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            //mListView.smoothScrollToPosition(mPosition);
            if ((mPosition - 1) != ListView.INVALID_POSITION) {
                mListView.smoothScrollToPosition(mPosition - 1);
            }else{

                mListView.smoothScrollToPosition(mPosition);
            }
            mListView.setSelection(mPosition);
            mListView.setItemChecked(mPosition , true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mDoodleAdapter.swapCursor(null);
    }


    public AlertDialog EditAskOption()
    {
        final EditText edit = new EditText(getActivity());
        String message = getResources().getString(R.string.motcle);
        Drawable icon = getResources().getDrawable(net.freelancertech.journal.app.R.drawable.ic_magnify_black_24dp);
        String title = getResources().getString(R.string.rechercheroffre);
        String textButton = getResources().getString(R.string.rechercher);

        //ancien mot cle rechercher
        String precedantMotCle = Utility.getPreferredMotcle(getActivity());

        if (!"vide".equals(precedantMotCle)) edit.setText(precedantMotCle.toString());


        AlertDialog myEditDialogBox =new AlertDialog.Builder(getActivity())

                //set message, title, and icon
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setView(edit)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        final String motcle = edit.getText().toString();
                        if (!motcle.matches("")) {

                            rechercherOffres(motcle);

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(net.freelancertech.journal.app.R.string.vide), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(getResources().getString(net.freelancertech.journal.app.R.string.inscription_activity_annuler), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        rechercherOffres("vide");

                        dialog.dismiss();

                    }
                })
                .create();

        return myEditDialogBox;

    }

    private void rechercherOffres(String motcle) {

        Utility.setPreferredMotcle(getActivity(),motcle);
        if("vide".equals(motcle)) {
            getLoaderManager().restartLoader(DOODLE_LOADER, null, this);
        }else {
            getLoaderManager().restartLoader(DOODLE_LOADER_SEARCH, null, this);
        }


    }


}