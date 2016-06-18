package com.example.inderpreet.zeropressure;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by INDERPREET on 17-06-2016.
 */
public class TimeService extends Service {

    public static final long NOTIFY_INTERVAL = 6200 * 1000; // 10 seconds
    //public static final long NOTIFY_INTERVAL = 100 * 1000; // 10 seconds
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(TimeService.this);
                    String id = settings.getString("id","0");
                    String pos = settings.getString("position","0");
                    new AsyncChkPos().execute(id,pos);
                }

            });
        }


}public class AsyncChkPos extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> prms = new HashMap<>();
            prms.put("id", params[0]);
            prms.put("pos",params[1]);
            requestHandler rh = new requestHandler();
            String response = rh.sendPostRequest(config.URL_CHK_POS,prms);
            //response=response.substring(0,response.length()-1);
            if(!response.equals("nchange\t"))
            {
                String x = response;
                String pos = x.substring(0,x.indexOf("-"));
                String id = x.substring((x.indexOf("-")+1),(x.length()-1));
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(TimeService.this);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("position");
                editor.commit();
                editor.remove("id");
                editor.commit();
                editor.putString("position",pos);
                editor.commit();
                editor.putString("id",id);
                editor.commit();
                notifypatient();
            }
            return response;
        }
    }
    public void notifypatient()
    {
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Change the Patients Position")
                        .setContentText("Position unchanged for more than 2 hrs!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, LoginActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LoginActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int mId=0;
        mNotificationManager.notify(mId, mBuilder.build());
    }




}
