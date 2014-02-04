package com.imperium.Commands;

public class HeartbeatResponse implements ICommand {
    private CommandType type;
    private int length;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CommandType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public HeartbeatResponse(){
        type = CommandType.HeartbeatResponse;
    }
}
