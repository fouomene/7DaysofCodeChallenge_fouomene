package net.freelancertech.journal.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 * The service which allows the sync adapter framework to access the authenticator.
 */
public class DoodlesArchiveAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private DoodlesArchiveAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new DoodlesArchiveAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}