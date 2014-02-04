package com.imperium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.imperium.Database.SqliteDbHelper;
import com.imperium.Utils.Validators;
import com.imperium.Database.System;

import java.util.List;

public class AddNewActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew);
    }

    public void btnAdd_onClick(View v){

        if(validateEntries()){
            System system = new System();
            system.setIpAddress(getIpAddress());
            system.setPort(getPort());
            system.setName(getName());
            system.setStatus("DOWN");

            SqliteDbHelper dbHelper = new SqliteDbHelper(this);
            dbHelper.openDataBase();
            dbHelper.insertSystem(system);

            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Invalid values", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean validateEntries(){

        String ipAddress = getIpAddress();
        String port = getPort();
        String name = getName();

        if(Validators.ValidateIpAddress(ipAddress)
            && Validators.ValidatePort(port)){

            SqliteDbHelper dbHelper = new SqliteDbHelper(this);
            dbHelper.openDataBase();
            List<System> systems = dbHelper.getAllSystems();

            if(Validators.ValidateUniqueSystem(
                ipAddress, port, name, systems))
                return true;
        }

        return false;
    }

    private String getIpAddress(){
        return ((EditText)findViewById(R.id.etIpAddress)).getText().toString();
    }

    private String getPort(){
        return ((EditText)findViewById(R.id.etPort)).getText().toString();
    }

    private String getName(){
        return ((EditText)findViewById(R.id.etName)).getText().toString();
    }
}