package com.imperium.Commands;

public class Heartbeat implements ICommand {
    private CommandType type;
    private int length;

    public CommandType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Heartbeat(){
        type = CommandType.Heartbeat;
    }
}
