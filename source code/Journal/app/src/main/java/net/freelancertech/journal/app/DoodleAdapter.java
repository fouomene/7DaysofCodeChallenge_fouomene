package net.freelancertech.journal.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.freelancertech.journal.app.utils.Utility;


/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 * {@link DoodleAdapter} exposes a list of notes
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class DoodleAdapter extends CursorAdapter {

    private static final String LOG_TAG = DoodleAdapter.class.getSimpleName();

    /**
     * Cache of the children views for a doodle list item.
     */
    public static class ViewHolder {

        public final TextView titleView;
        public final TextView runDateView;
        public final ImageView doodleImageView;


        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(net.freelancertech.journal.app.R.id.list_item_title_textview);
            runDateView = (TextView) view.findViewById(net.freelancertech.journal.app.R.id.list_item_rundate_textview);
            doodleImageView = (ImageView) view.findViewById(net.freelancertech.journal.app.R.id.list_item_doodle_imageview);
        }
    }

    public DoodleAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = -1;

         layoutId = net.freelancertech.journal.app.R.layout.list_item_doodle;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read doodle title from cursor
        String title = cursor.getString(DoodleFragment.COL_DOODLE_OBJET);
        // Find TextView and set doodle title on it
        viewHolder.titleView.setText(title);

        // Read doodle run-date from cursor
        String runDate = cursor.getString(DoodleFragment.COL_DOODLE_RUN_DATE);
        String fichier = cursor.getString(DoodleFragment.COL_DOODLE_FICHIER);
        // URL image

        String urlPhotoprofile ="/facinfosflash/imagesprofils/"+ fichier+".jpg";

        if (fichier.equals("vide") ){ urlPhotoprofile ="/facinfosflash/imagesprofils/journal.png"; }


        // Construct the URL photo
        //Log.e(LOG_TAG, "URLPHOTO = " + Utility.urlserveurimage + urlPhotoprofile);
        Uri builtUri  = new Uri.Builder()
                .scheme("http")
                .authority(Utility.urlserveurimage.replaceFirst("http://",""))
                .path(urlPhotoprofile)
                .build();
        // Log.e(LOG_TAG, "URI = " + builtUri.toString());

        //this will help us to avoid the Target being gc'd
        //viewHolder.doodleImageView.setTag(target);

        Picasso.with(context).load(builtUri.toString()).into(viewHolder.doodleImageView);

        //Picasso.with(context).load(builtUri.toString()).into(target);

        // Find TextView and set doodle run date on itUtility.formatDate(runDate)
        viewHolder.runDateView.setText(runDate);

    }


}