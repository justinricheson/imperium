package com.imperium;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.imperium.AppConfig.AppConfig;
import com.imperium.AppConfig.AppProperties;
import com.imperium.Commands.CommandExecuter;
import com.imperium.Commands.IAsyncTaskCompleteListener;
import com.imperium.Database.SqliteDbHelper;
import com.imperium.Database.System;

public class LaunchAppsActivity extends Activity {
    private System system;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchapp);

        String systemName = getIntent().getStringExtra("SystemName");

        SqliteDbHelper dbHelper = new SqliteDbHelper(this);
        dbHelper.openDataBase();
        system = dbHelper.getSystem(systemName);

        QueryAppConfigListener listener = new QueryAppConfigListener();
        CommandExecuter.QueryAppConfig(system, listener);
    }

    private void launchApplication(int appId){
        CommandExecuter.LaunchApp(system, appId);

        Toast.makeText(getApplicationContext(), "Launching Application",
                Toast.LENGTH_LONG).show();
    }

    private void generateAppConfigLayout(AppConfig config){
        LinearLayout layout = (LinearLayout) findViewById(R.id.llLaunchApps);

        if(config != null){

            int i = 0;
            for(AppProperties app : config.getAppProperties().values()){
                Button btn = new Button(this);
                btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                if(i % 2 == 0)
                    btn.setBackground(getResources().getDrawable(R.drawable.blackbutton));
                else  btn.setBackground(getResources().getDrawable(R.drawable.greybutton));

                btn.setTextColor(Color.WHITE);
                btn.setGravity(Gravity.CENTER);
                btn.setTypeface(null, Typeface.BOLD);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                btn.setShadowLayer(2, 1, 1, Color.BLACK);
                btn.setText(app.getName());
                btn.setId(app.getId());
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int appId = ((Button) v).getId();
                        launchApplication(appId);
                    }
                });

                layout.addView(btn);

                i++;
            }
        }
        else{
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            tv.setText(String.format("Error retrieving app config from system: %s", system.getName()));
            layout.addView(tv);
        }
    }

    class QueryAppConfigListener implements IAsyncTaskCompleteListener<AppConfig> {

        public void onTaskComplete(AppConfig result) {
            generateAppConfigLayout(result);
        }
    }
}