package com.imperium.TCP;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class TcpClient {

    private String ipAddress;
    private String port;

    public TcpClient(String ipAddress, String port){
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public byte[] Send(byte[] data, Boolean waitForResponse) throws Exception{

        Socket client = new Socket(ipAddress, Integer.parseInt(port));

        OutputStream out = client.getOutputStream();
        out.write(data);

        if(waitForResponse){
            client.setSoTimeout(10 * 1000); // Only block for 10 seconds on read
            InputStream in = client.getInputStream();

            byte[] receiveData = new byte[1024];
            int length = in.read(receiveData, 0, receiveData.length);

            return Arrays.copyOfRange(receiveData, 0, length);
        }

        client.close();
        return null;
    }
}
