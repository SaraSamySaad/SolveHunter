package com.solvehunter.solution.hamza.solvehunter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences prefs = getSharedPreferences("myPref", MODE_PRIVATE);
                if (prefs.getString("userID","null").toString().equals("null")){
                    Intent mainIntent = new Intent(MainActivity.this,SignInActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(MainActivity.this,UserHomeActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    }
