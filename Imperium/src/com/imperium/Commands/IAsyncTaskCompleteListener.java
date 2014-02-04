package com.imperium.Commands;

public interface IAsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}
