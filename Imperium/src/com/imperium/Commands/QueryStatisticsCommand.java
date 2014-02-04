package com.imperium.Commands;

public class QueryStatisticsCommand implements ICommand{
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

    public QueryStatisticsCommand(){
        type = CommandType.QueryStatistics;
    }
}
