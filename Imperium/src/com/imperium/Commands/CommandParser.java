package com.imperium.Commands;

import java.util.Arrays;

// Format
// Command ID    | Length   | Parameters
// 1 byte        | 2 bytes  | Specified by Length
public class CommandParser {
    public static ICommand Parse(byte[] bytes){

        switch ((int)bytes[0]){
            case -128: // Heartbeat Response
                return parseHeartbeatResponse(bytes);
            case -127: // Query Statistics Response
                return parseQueryStatisticsResponse(bytes);
            case -126: // Query App Config Response
                return parseQueryAppConfigResponse(bytes);
            default:
                return null;
        }
    }

    private static ICommand parseHeartbeatResponse(byte[] bytes){
        HeartbeatResponse response = new HeartbeatResponse();

        byte[] length = Arrays.copyOfRange(bytes, 1, 3);
        response.setLength(getLength(length));

        switch (bytes[3]){
            case 0x00:
                response.setStatus("DOWN");
                break;
            default: // If we got a response, it's up
            case 0x01:
                response.setStatus("UP");
                break;
            case 0x02:
                response.setStatus("HELP");
                break;
        }

        return response;
    }

    private static ICommand parseQueryStatisticsResponse(byte[] bytes){
        QueryStatisticsResponse response = new QueryStatisticsResponse();

        byte[] length = Arrays.copyOfRange(bytes, 1, 3);
        response.setLength(getLength(length));

        byte[] json = Arrays.copyOfRange(bytes, 3, response.getLength() + 3);
        response.setJson(new String(json));

        return response;
    }

    private static ICommand parseQueryAppConfigResponse(byte[] bytes){
        QueryAppConfigResponse response = new QueryAppConfigResponse();

        byte[] length = Arrays.copyOfRange(bytes, 1, 3);
        response.setLength(getLength(length));

        byte[] json = Arrays.copyOfRange(bytes, 3, response.getLength() + 3);
        response.setJson(new String(json));

        return response;
    }

    private static int getLength(byte[] lengthArray){
        int byte1 = (int)lengthArray[0];
        byte1 = byte1 < 0 ? byte1 + 256 : byte1; // Convert to unsigned
        int byte2 = (int)lengthArray[1];
        byte2 = byte2 < 0 ? byte2 + 256 : byte2; // Convert to unsigned

        return (byte1 * 256) + byte2;
    }
}
