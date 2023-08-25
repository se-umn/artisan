package com.prismaqf.callblocker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;

/**
 * Call detect service
 * @author Moskvichev Andrey V.
 * @see 'www.codeproject.com/Articles/548416/Detecting-incoming-and-outgoing-phone-calls-on-And'
 */public class CallDetectService extends Service {

    private static final String TAG = CallDetectService.class.getCanonicalName();
    private static final int ONGOING_NOTIFICATION_ID = 1007;


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        CallDetectService getService() {
            // Return this instance of LocalService so clients can call public methods
            return CallDetectService.this;
        }
    }

    private final CallHelper myCallHelper;
    private final IBinder myBinder = new LocalBinder();
    
    public CallDetectService() {
        myCallHelper = CallHelper.GetHelper();
    }

    public int getNumReceived() {return myCallHelper.getNumReceived();}

    public int getNumTriggered() {return myCallHelper.getNumTriggered();}




    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        /*
        The idea of starting the service in the foreground + aquiring a wake lock
        does not work properly in Marshmallow. The service is still killed by the
        doze mode. Untill this is fixed the only solution is to add the app to
        the whitelist to prevent doze
         */
        final Context ctx=this;
        myCallHelper.start(ctx);
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (myCallHelper) {
                    myCallHelper.recordServiceStart(ctx);
                }
            }
        }).start();


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, CallBlockerManager.class), 0);
        NotificationCompat.Builder builder = new Builder(this, "mychannel")
              .setSmallIcon(R.drawable.police)
              .setContentTitle(getText(R.string.app_name))
              .setContentText(getText(R.string.tx_notification))
              .setContentIntent(pendingIntent)
              .setTicker(getText(R.string.app_name));

        Notification notification = new Notification.Builder(this, "mychannel")
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.tx_notification))
                .setSmallIcon(R.drawable.police)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.app_name))
                .build();
        startForeground(ONGOING_NOTIFICATION_ID, notification);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.w(TAG,"The service has been killed");
/*        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (this) {
                    myCallHelper.recordServiceStop();
                }
            }
        }).start();*/
        myCallHelper.recordServiceStop(this);
        myCallHelper.stop(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

}
