package com.imperium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.imperium.Database.SqliteDbHelper;
import com.imperium.Database.System;

import java.util.List;

public class MainActivity extends Activity {

    private String[] systemNames;
    SqliteDbHelper dbHelper;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try{
                updateView();
            }catch(Exception e){ }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dbHelper = new SqliteDbHelper(this);
        dbHelper.openDataBase();

        updateView();

        // Start heartbeat service
        Intent i=new Intent(this, HeartbeatService.class);
        i.putExtra(HeartbeatService.MAINCALLBACK, new Messenger(handler));
        startService(i);
    }

    private void updateView(){
        List<System> systems = dbHelper.getAllSystems();
        systemNames = new String[systems.size()];
        for(int i = 0; i < systems.size(); i++){
            systemNames[i] = systems.get(i).getName();
        }

        SystemsListViewAdapter listAdapter = new SystemsListViewAdapter(MainActivity.this , R.layout.customlist , systems);
        ListView listView=(ListView)findViewById(R.id.lvSystemsList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                openControlSystemActivity(arg2);
            }
        });
    }

    public void btnAddNew_onClick(View v){
        Intent intent = new Intent(this, AddNewActivity.class);

        finish();
        startActivity(intent);
    }

    private void openControlSystemActivity(int listIndex){

        Intent intent = new Intent(this, ControlSystemActivity.class);
        intent.putExtra("SystemName", systemNames[listIndex]);

        startActivity(intent);
    }
}
