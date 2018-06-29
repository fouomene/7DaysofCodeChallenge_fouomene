package net.freelancertech.journal.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.freelancertech.journal.app.R;
/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(net.freelancertech.journal.app.R.id.toolbar);
        setSupportActionBar(toolbar);

        // to display Icon launcher
        ActionBar actionBar = getSupportActionBar();

        //icon back
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(net.freelancertech.journal.app.R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        // set title bar
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(net.freelancertech.journal.app.R.layout.action_bar_title_detail);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            if (getIntent().getStringExtra(DetailFragment.DETAIL_INFORMATION_ID) != null) {

                arguments.putString(DetailFragment.DETAIL_INFORMATION_ID, getIntent().getStringExtra(DetailFragment.DETAIL_INFORMATION_ID));
            }

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);


            getSupportFragmentManager().beginTransaction()
                    .add(net.freelancertech.journal.app.R.id.doodle_detail_container, fragment)
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(net.freelancertech.journal.app.R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }



}