package com.imperium.Statistics;

import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

public class Statistics
{
    private int upTime;
    private String computerName;
    private String userName;
    private String osVersion;
    private String model;
    private String memory;
    private String processor;
    private List<DriveStatistics> drives;
    private List<String> applications;

    public Statistics()
    {
        drives = new ArrayList<DriveStatistics>();
        applications = new ArrayList<String>();
    }

    public Spanned toSpanned(){
        return Html.fromHtml(
                "<b>Uptime (sec): </b><small>" + upTime + "</small><br />" +
                "<b>Computer Name: </b><small>" + computerName + "</small><br />" +
                "<b>User Name: </b><small>" + userName + "</small><br />" +
                "<b>OS Version: </b><small>" + osVersion + "</small><br />" +
                "<b>Model: </b><small>" + model + "</small><br />" +
                "<b>Memory (bytes): </b><small>" + memory + "</small><br />" +
                "<b>Processor: </b><small>" + processor + "</small><br />" +
                drivesToSpanned() +
                applicationsToSpanned()
        );
    }

    private String drivesToSpanned(){
        String returnValue = "";

        for(DriveStatistics drive : drives){
            returnValue += "<b>Drive " + drive.getCaption() + "</b><br />";
            returnValue += "<small>-Description: " + drive.getDescription() + "</small><br />";
            returnValue += "<small>-File System: " + drive.getFileSystem() + "</small><br />";
            returnValue += "<small>-Size (bytes): " + drive.getSize() + "</small><br />";
            returnValue += "<small>-Free Space (bytes): " + drive.getFreeSpace() + "</small><br />";
        }

        return returnValue;
    }

    private String applicationsToSpanned(){
        String returnValue = "<b>Applications:</b><br />";

        for(String app : applications){
            returnValue += "<b>-</b><small>" + app + "</small><br />";
        }

        return returnValue;
    }
}