package com.imperium.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.imperium.Database.System;

public class Validators {
    public static Boolean ValidateIpAddress(String ipAddress){
        String pattern =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        return ipAddress.matches(pattern);
    }

    public static Boolean ValidatePort(String port){
        if(ValidateInt(port)){
            int iPort = Integer.parseInt(port);
            return iPort >= 0
                && iPort <= 65536;
        }

        return false;
    }

    public static Boolean ValidateInt(String value){
        return value.matches("\\d+");
    }

    public static Boolean ValidateUniqueSystem(String ipAddress, String port, String name, List<System> systems){
        for(System system : systems){
            if(system.getIpAddress().equals(ipAddress)
            && system.getPort().equals(port))
                return false;
            if(system.getName().equals(name))
                return false;
        }

        return true;
    }
}
