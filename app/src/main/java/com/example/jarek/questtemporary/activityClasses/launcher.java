package com.example.jarek.questtemporary.activityClasses;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.activityClasses.QuestPanelMain;

public class launcher extends AppCompatActivity {

    private int miliseconds = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(miliseconds);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(getApplicationContext(),QuestPanelMain.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
