package com.imperium.Commands;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imperium.Database.System;
import com.imperium.Statistics.Statistics;
import com.imperium.TCP.TcpClient;

class QueryStatisticsExecuter extends AsyncTask<System, Void, Statistics> {

    private IAsyncTaskCompleteListener<Statistics> listener;

    public QueryStatisticsExecuter(IAsyncTaskCompleteListener<Statistics> listener) {
        this.listener = listener;
    }

    protected Statistics doInBackground(System... systems) {

        System system = systems[0];

        try {
            Log.v("QueryStatisticsExecuter", String.format("Sending query stats to %s:%s",
                    system.getIpAddress(), system.getPort()));

            QueryStatisticsCommand queryStats = new QueryStatisticsCommand();
            queryStats.setLength(0);
            byte[] encodedQueryStats = CommandEncoder.Encode(queryStats);

            TcpClient client = new TcpClient(system.getIpAddress(), system.getPort());
            byte[] response = client.Send(encodedQueryStats, true);
            QueryStatisticsResponse queryStatsResponse = (QueryStatisticsResponse)CommandParser.Parse(response);

            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
            return gson.fromJson(queryStatsResponse.getJson(), Statistics.class);
        } catch(Exception e){
            Log.v("QueryStatisticsExecuter", String.format("Error sending query stats to %s:%s",
                    system.getIpAddress(), system.getPort()));
        }

        return null;
    }

    protected void onPostExecute(Statistics result) {
        listener.onTaskComplete(result);
    }
}