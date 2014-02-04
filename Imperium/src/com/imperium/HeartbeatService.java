package com.imperium;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import com.imperium.Database.SqliteDbHelper;
import com.imperium.Database.System;
import com.imperium.Commands.CommandExecuter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeartbeatService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private SqliteDbHelper mDbHelper;
    private Map<String, Messenger> mMessengers;
    public static final String MAINCALLBACK = "maincallback";
    public static final String CONTROLSYSTEMCALLBACK = "controlsystemcallback";

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            while(true){
                try {
                    Thread.sleep(10 * 1000);

                    List<System> systems = mDbHelper.getAllSystems();

                    for(System system : systems){
                        String oldStatus = system.getStatus();
                        system = CommandExecuter.UpdateStatus(system);
                        mDbHelper.updateSystem(system);

                        if(!oldStatus.equals(system.getStatus())){
                            NotifyStatusChange(system);
                            NotifyViews();
                        }
                    }
                } catch (Exception e){
                    //stopSelf();
                }
            }
        }
    }

    private void NotifyViews(){
        synchronized (mMessengers){
            Log.v("HeartbeatService", String.format("mMessengers Size = %s",
                    mMessengers.size()));

            List<Messenger> toRemove = new ArrayList<Messenger>();
            for(Messenger messenger : mMessengers.values()){
                Message message = Message.obtain();

                // Don't need to package any data
                // everything is in the database
                message.setData(new Bundle());

                // Closing the app causes an exception
                // because the callback view is not there
                try{
                    messenger.send(message);
                }catch (Exception e){
                    toRemove.add(messenger);
                }
            }

            for(Messenger messenger : toRemove){
                mMessengers.remove(messenger);
            }
        }
    }

    private void NotifyStatusChange(System system){
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Notification myNotification = new Notification(
                R.drawable.ic_launcher,
                String.format("System %s status changed to %s.", system.getName(), system.getStatus()),
                java.lang.System.currentTimeMillis());

        Context context = getApplicationContext();

        String notificationTitle = "System status change";
        String notificationText = String.format("System %s status changed to %s.", system.getName(), system.getStatus());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(), Intent.FLAG_ACTIVITY_NEW_TASK);

        myNotification.defaults |= Notification.DEFAULT_SOUND;
        myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        myNotification.setLatestEventInfo(
                context,
                notificationTitle,
                notificationText,
                pendingIntent);

        notificationManager.notify(1, myNotification);
    }

    @Override
    public void onCreate() {
        mDbHelper = new SqliteDbHelper(this);
        mDbHelper.openDataBase();

        mMessengers = new HashMap<String, Messenger>();

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        Message msg = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){
            Bundle extras = intent.getExtras();
            if(extras != null) {
                Messenger cb1 = (Messenger)extras.get(MAINCALLBACK);
                Messenger cb2 = (Messenger)extras.get(CONTROLSYSTEMCALLBACK);

                synchronized (mMessengers){
                    if(cb1 != null){
                        if(mMessengers.containsKey(MAINCALLBACK))
                            mMessengers.remove(mMessengers.get(MAINCALLBACK));

                        mMessengers.put(MAINCALLBACK, cb1);

                    }else if(cb2 != null){
                        if(mMessengers.containsKey(CONTROLSYSTEMCALLBACK))
                            mMessengers.remove(mMessengers.get(CONTROLSYSTEMCALLBACK));

                        mMessengers.put(CONTROLSYSTEMCALLBACK, cb2);
                    }
                }
            }
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
    }
}