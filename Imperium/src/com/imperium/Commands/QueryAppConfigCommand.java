package com.imperium.Commands;

public class QueryAppConfigCommand implements  ICommand {
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

    public QueryAppConfigCommand(){
        type = CommandType.QueryAppConfig;
    }
}
