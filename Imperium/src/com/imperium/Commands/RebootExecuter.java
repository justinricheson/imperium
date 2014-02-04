package com.imperium.Commands;

import android.os.AsyncTask;
import android.util.Log;
import com.imperium.TCP.TcpClient;
import com.imperium.Database.System;

class RebootExecuter extends AsyncTask<System, Void, Void> {

    protected Void doInBackground(System... systems) {

        System system = systems[0];
        try {

            Log.v("RebootExecuter", String.format("Sending reboot to %s:%s",
                    system.getIpAddress(), system.getPort()));

            RebootCommand reboot = new RebootCommand();
            reboot.setLength(0);
            byte[] encodedReboot = CommandEncoder.Encode(reboot);

            TcpClient client = new TcpClient(system.getIpAddress(), system.getPort());
            client.Send(encodedReboot, false);
        } catch(Exception e){
            Log.v("RebootExecuter", String.format("Error sending reboot to %s:%s",
                    system.getIpAddress(), system.getPort()));
        }
         return null;
    }
}