package net.freelancertech.journal.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.freelancertech.journal.app.sync.DoodlesArchiveSyncAdapter;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * Created by FOUOMENE on 06/2018.
 * EmailAuthor:  fouomenedaniel@gmail.com .
 *
 **/

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

     //sync when phone restart
        DoodlesArchiveSyncAdapter.syncImmediately(context);

    }
}