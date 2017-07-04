package com.example.jarek.questtemporary.activityClasses;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jarek.questtemporary.R;

public class launcher extends AppCompatActivity {

    private int miliseconds = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        setComponentsColor();

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

    private void setComponentsColor(){
        ((TextView)findViewById(R.id.textView_author)).setTextColor(getResources().getColor(R.color.color_Write));
        ((ImageView)findViewById(R.id.imageView_launcher)).setImageResource(R.drawable.main_picture);
        findViewById(R.id.RelativeLayoutLauncher).setBackgroundColor(getResources().getColor(R.color.color_backgroundWhite));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
