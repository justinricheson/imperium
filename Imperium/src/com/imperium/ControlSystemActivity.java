package com.imperium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.TextView;
import com.imperium.Commands.IAsyncTaskCompleteListener;
import com.imperium.Database.SqliteDbHelper;
import com.imperium.Database.System;

import java.util.List;

public class ControlSystemActivity extends Activity {
    private System system;
    private SqliteDbHelper dbHelper;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try{
                system = dbHelper.getSystem(system.getName());
                if(system != null) // System can be null if it was deleted
                    setStatusLabel();
                else finish();
            }catch(Exception e){ }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlsystem);

        // Start heartbeat service
        Intent i=new Intent(this, HeartbeatService.class);
        i.putExtra(HeartbeatService.CONTROLSYSTEMCALLBACK, new Messenger(handler));
        startService(i);

        String systemName = getIntent().getStringExtra("SystemName");
        ((TextView)findViewById(R.id.tvSystemName)).setText(systemName);

        dbHelper = new SqliteDbHelper(this);
        dbHelper.openDataBase();
        system = dbHelper.getSystem(systemName);

        setStatusLabel();
    }

    public void btnReboot_onClick(View v){
        Intent intent = new Intent(this, RebootSystemActivity.class);
        intent.putExtra("SystemName", system.getName());

        finish();
        startActivity(intent);
    }

    public void btnLaunchApplication_onClick(View v){
        Intent intent = new Intent(this, LaunchAppsActivity.class);
        intent.putExtra("SystemName", system.getName());

        startActivity(intent);
    }

    public void btnRequestStatistics_onClick(View v){
        Intent intent = new Intent(this, GetStatisticsActivity.class);
        intent.putExtra("SystemName", system.getName());

        startActivity(intent);
    }

    public void btnDeleteSystem_onClick(View v){
        dbHelper.deleteSystem(system);

        finish();
    }

    private void setStatusLabel(){
        TextView tvSystemStatus = ((TextView)findViewById(R.id.tvSystemStatus));
        if(system.getStatus().equals("UP")){
            tvSystemStatus.setText("UP");
            tvSystemStatus.setBackgroundResource(R.color.green);
        }else if(system.getStatus().equals("DOWN")){
            tvSystemStatus.setText("DOWN");
            tvSystemStatus.setBackgroundResource(R.color.red);
        }
        else{
            tvSystemStatus.setText("HELP");
            tvSystemStatus.setBackgroundResource(R.color.yellow);
        }
    }
}