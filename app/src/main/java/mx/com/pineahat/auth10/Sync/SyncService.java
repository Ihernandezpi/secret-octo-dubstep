package mx.com.pineahat.auth10.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by ${Ignacio} on 12/07/2015.
 */
public class SyncService extends Service {
    public static final String AUTHORITY = "mx.com.pineahat.auth10";
    public static final long SECONDS_PER_MINUTE = 1L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 5L;
    public static final long SYNC_INTERVAL =SYNC_INTERVAL_IN_MINUTES *SECONDS_PER_MINUTE;
    private static SyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();
    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
                ContentResolver mContentResolver = sSyncAdapter.getContext().getContentResolver();
                AccountManager miManager = AccountManager.get(getApplicationContext());
                Account[] a = miManager.getAccountsByType(AUTHORITY);
                mContentResolver.addPeriodicSync(a[0], AUTHORITY,  Bundle.EMPTY,  SYNC_INTERVAL);
                mContentResolver.setSyncAutomatically(a[0],AUTHORITY,true);
                mContentResolver.setMasterSyncAutomatically(true);
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
