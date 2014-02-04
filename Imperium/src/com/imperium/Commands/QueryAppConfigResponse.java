package com.imperium.Commands;

public class QueryAppConfigResponse implements ICommand{
    private CommandType type;
    private int length;
    private String json;

    public CommandType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public QueryAppConfigResponse(){
        type = CommandType.QueryAppConfigResponse;
    }
}