package com.imperium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.imperium.Commands.CommandExecuter;
import com.imperium.Database.SqliteDbHelper;

public class RebootSystemActivity extends Activity {
    private com.imperium.Database.System system;
    private int mStartTime;
    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            mStartTime++;

            TextView tvRebootCountLabel = ((TextView)findViewById(R.id.tvRebootCountLabel));
            tvRebootCountLabel.setText(Integer.toString(Math.abs(mStartTime - 10)));

            mHandler.removeCallbacks(this);
            if(mStartTime < 10)
                mHandler.postDelayed(this, 1000);
            else{
                finish();
                goBackToControlSystem();
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rebootsystem);

        String systemName = getIntent().getStringExtra("SystemName");

        SqliteDbHelper dbHelper = new SqliteDbHelper(this);
        dbHelper.openDataBase();
        system = dbHelper.getSystem(systemName);

        CommandExecuter.Reboot(system);

        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    private void goBackToControlSystem(){
        Intent intent = new Intent(this, ControlSystemActivity.class);
        intent.putExtra("SystemName", system.getName());
        startActivity(intent);
    }
}