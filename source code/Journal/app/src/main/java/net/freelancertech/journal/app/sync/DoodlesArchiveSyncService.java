package net.freelancertech.journal.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class DoodlesArchiveSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static DoodlesArchiveSyncAdapter sDoodlesArchiveSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("DoodlesArchiveSyncService", "onCreate - DoodlesArchiveSyncService");
        synchronized (sSyncAdapterLock) {
            if (sDoodlesArchiveSyncAdapter == null) {
                sDoodlesArchiveSyncAdapter = new DoodlesArchiveSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sDoodlesArchiveSyncAdapter.getSyncAdapterBinder();
    }
}