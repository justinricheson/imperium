package com.imperium.Commands;

import android.util.Log;
import com.imperium.Database.System;
import com.imperium.TCP.TcpClient;

public class CommandExecuter {
    public static void QueryStats(System system, IAsyncTaskCompleteListener listener){
        QueryStatisticsExecuter executer = new QueryStatisticsExecuter(listener);
        executer.execute(system);
    }

    public static void QueryAppConfig(System system, IAsyncTaskCompleteListener listener){
        QueryAppConfigExecuter executer = new QueryAppConfigExecuter(listener);
        executer.execute(system);
    }

    public static void LaunchApp(System system, int appId){
        LaunchAppExecuter.LaunchAppPackage launchPackage = new LaunchAppExecuter.LaunchAppPackage();
        launchPackage.setSystem(system);
        launchPackage.setAppId(appId);

        LaunchAppExecuter executer = new LaunchAppExecuter();
        executer.execute(launchPackage);
    }

    public static void Reboot(System system){
        RebootExecuter executer = new RebootExecuter();
        executer.execute(system);
    }

    public static System UpdateStatus(System system){

        try {
            Log.v("UpdateStatusExecuter", String.format("Sending heartbeat to %s:%s",
                    system.getIpAddress(), system.getPort()));

            Heartbeat heartbeat = new Heartbeat();
            heartbeat.setLength(0);
            byte[] encodedHeartbeat = CommandEncoder.Encode(heartbeat);

            TcpClient client = new TcpClient(system.getIpAddress(), system.getPort());
            byte[] response = client.Send(encodedHeartbeat, true);
            HeartbeatResponse heartbeatResponse = (HeartbeatResponse)CommandParser.Parse(response);

            system.setStatus(heartbeatResponse.getStatus());
        } catch(Exception e){
            Log.v("UpdateStatusExecuter", String.format("Error sending heartbeat to %s:%s",
                    system.getIpAddress(), system.getPort()));
            system.setStatus("DOWN");
        }

        Log.v("UpdateStatusExecuter", String.format("System status: %s", system.getStatus()));
        return system;
    }
}
