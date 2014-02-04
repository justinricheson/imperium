package com.imperium.Commands;

public interface ICommand {
    CommandType getType();
    int getLength();
}
