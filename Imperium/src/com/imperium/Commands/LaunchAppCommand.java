package com.imperium.Commands;

public class LaunchAppCommand implements ICommand {
    private CommandType type;
    private int length;
    private int appId;
    private String argument;

    public CommandType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public LaunchAppCommand(){
        type = CommandType.LaunchApplication;
    }
}
