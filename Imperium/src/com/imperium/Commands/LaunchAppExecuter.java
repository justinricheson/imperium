package com.imperium.Commands;

import android.os.AsyncTask;
import android.util.Log;
import com.imperium.Database.System;
import com.imperium.TCP.TcpClient;

public class LaunchAppExecuter extends AsyncTask<LaunchAppExecuter.LaunchAppPackage, Void, Void> {

    protected Void doInBackground(LaunchAppPackage... packages) {

        LaunchAppPackage launchPackage = packages[0];
        System system = launchPackage.getSystem();
        int appId = launchPackage.getAppId();

        try {
            Log.v("LaunchAppExecuter", String.format("Sending launch app command to %s:%s",
                    system.getIpAddress(), system.getPort()));

            LaunchAppCommand launchApp = new LaunchAppCommand();
            launchApp.setLength(1);
            launchApp.setAppId(appId);
            byte[] encodedLaunchApp = CommandEncoder.Encode(launchApp);

            TcpClient client = new TcpClient(system.getIpAddress(), system.getPort());
            client.Send(encodedLaunchApp, false);
        } catch(Exception e){
            Log.v("LaunchAppExecuter", String.format("Error sending launch app command to %s:%s",
                    system.getIpAddress(), system.getPort()));
        }

        return null;
    }

    public static class LaunchAppPackage{
        private System system;
        private int appId;

        public System getSystem() {
            return system;
        }

        public void setSystem(System system) {
            this.system = system;
        }

        public int getAppId() {
            return appId;
        }

        public void setAppId(int appId) {
            this.appId = appId;
        }
    }
}
