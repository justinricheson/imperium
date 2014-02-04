package com.imperium.Commands;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

// Format
// Command ID    | Length   | Parameters
// 1 byte        | 2 bytes  | Specified by Length
public class CommandEncoder {
    public static byte[] Encode(ICommand command)
    {
        switch (command.getType())
        {
            case Heartbeat:
                return EncodeHeartbeat(command);
            case Reboot:
                return EncodeRebootCommand(command);
            case LaunchApplication:
                return EncodeLaunchAppCommand(command);
            case QueryStatistics:
                return EncodeQueryStatsCommand(command);
            case QueryAppConfig:
                return EncodeQueryAppConfigCommand(command);
            default:
                return new byte[0];
        }
    }

    private static byte[] EncodeHeartbeat(ICommand command)
    {
        Heartbeat response = (Heartbeat)command;

        byte[] heartBeat = new byte[3];
        heartBeat[0] = 0x00; // Heartbeat

        byte[] length = encodeLength(response.getLength());
        heartBeat[1] = length[0];
        heartBeat[2] = length[1];

        return heartBeat;
    }

    private static byte[] EncodeRebootCommand(ICommand command){

        RebootCommand response = (RebootCommand)command;

        byte[] reboot = new byte[3];
        reboot[0] = 0x01; // Reboot

        byte[] length = encodeLength(response.getLength());
        reboot[1] = length[0];
        reboot[2] = length[1];

        return reboot;
    }

    private static byte[] EncodeLaunchAppCommand(ICommand command){

        LaunchAppCommand response = (LaunchAppCommand)command;

        byte[] launchApp = new byte[4];
        launchApp[0] = 0x02; // Launch application

        byte[] length = encodeLength(response.getLength());
        launchApp[1] = length[0];
        launchApp[2] = length[1];

        launchApp[3] = (byte)response.getAppId();

        return launchApp;
    }

    private static byte[] EncodeQueryStatsCommand(ICommand command){

        QueryStatisticsCommand response = (QueryStatisticsCommand)command;

        byte[] queryStats = new byte[3];
        queryStats[0] = 0x03; // Query Statistics

        byte[] length = encodeLength(response.getLength());
        queryStats[1] = length[0];
        queryStats[2] = length[1];

        return queryStats;
    }

    private static byte[] EncodeQueryAppConfigCommand(ICommand command){

        QueryAppConfigCommand response = (QueryAppConfigCommand)command;

        byte[] queryAppConfig = new byte[3];
        queryAppConfig[0] = 0x04; // Query App Config

        byte[] length = encodeLength(response.getLength());
        queryAppConfig[1] = length[0];
        queryAppConfig[2] = length[1];

        return queryAppConfig;
    }

    private static byte[] encodeLength(int length){
        byte[] temp = ByteBuffer.allocate(4).putInt(length).array();
        return Arrays.copyOfRange(temp, 2, 4);
    }
}
