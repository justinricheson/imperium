package com.imperium;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.imperium.Commands.CommandExecuter;
import com.imperium.Database.SqliteDbHelper;
import com.imperium.Database.System;
import com.imperium.Statistics.Statistics;
import com.imperium.Commands.IAsyncTaskCompleteListener;

public class GetStatisticsActivity extends Activity {
    private System system;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getstatistics);

        String systemName = getIntent().getStringExtra("SystemName");

        SqliteDbHelper dbHelper = new SqliteDbHelper(this);
        dbHelper.openDataBase();
        system = dbHelper.getSystem(systemName);

        QueryStatsListener listener = new QueryStatsListener();
        CommandExecuter.QueryStats(system, listener);
    }

    private void displayStatistics(Statistics statistics){
        TextView tvStatistics = ((TextView)findViewById(R.id.tvStatistics));

        if(statistics != null){
            tvStatistics.setText(statistics.toSpanned());
        }else{
            tvStatistics.setText(String.format("Error retrieving statistics for system: %s", system.getName()));
        }
    }

    class QueryStatsListener implements IAsyncTaskCompleteListener<Statistics> {

        public void onTaskComplete(Statistics result) {
            displayStatistics(result);
        }
    }
}