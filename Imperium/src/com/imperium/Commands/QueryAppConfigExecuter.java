package com.imperium.Commands;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imperium.AppConfig.AppConfig;
import com.imperium.Database.System;
import com.imperium.TCP.TcpClient;

class QueryAppConfigExecuter extends AsyncTask<System, Void, AppConfig> {

    private IAsyncTaskCompleteListener<AppConfig> listener;

    public QueryAppConfigExecuter(IAsyncTaskCompleteListener<AppConfig> listener) {
        this.listener = listener;
    }

    protected AppConfig doInBackground(System... systems) {

        System system = systems[0];

        try {
            Log.v("QueryAppConfigExecuter", String.format("Sending query app config to %s:%s",
                    system.getIpAddress(), system.getPort()));

            QueryAppConfigCommand queryAppConfig = new QueryAppConfigCommand();
            queryAppConfig.setLength(0);
            byte[] encodedQueryAppConfig = CommandEncoder.Encode(queryAppConfig);

            TcpClient client = new TcpClient(system.getIpAddress(), system.getPort());
            byte[] response = client.Send(encodedQueryAppConfig, true);
            QueryAppConfigResponse queryAppConfigResponse = (QueryAppConfigResponse)CommandParser.Parse(response);

            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
            return gson.fromJson(queryAppConfigResponse.getJson(), AppConfig.class);
        } catch(Exception e){
            Log.v("QueryAppConfigExecuter", String.format("Error sending query app config to %s:%s",
                    system.getIpAddress(), system.getPort()));
        }

        return null;
    }

    protected void onPostExecute(AppConfig result) {
        listener.onTaskComplete(result);
    }
}