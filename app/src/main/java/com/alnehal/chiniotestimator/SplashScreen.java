package com.alnehal.chiniotestimator;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import utilis.SharedPreferenceClass;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SharedPreferenceClass.getInstance(getApplicationContext(), "ChiniotEstimater", MODE_PRIVATE);

                if (SharedPreferenceClass.getSessionFunc("session").equalsIgnoreCase("true")) {





                    String string =SharedPreferenceClass.getSessionTimeFunc("time");

                    DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                    try {
                        Date savedDate = format.parse(string);
                        Date currentDate=getCurrentDate();
                        long difference = currentDate.getTime() - savedDate.getTime();

                        long diffSeconds = difference / 1000 % 60;
                        long diffMinutes = difference / (60 * 1000) % 60;
                        long diffHours = difference / (60 * 60 * 1000) % 24;
                        long diffDays = difference / (24 * 60 * 60 * 1000);
                        if (diffMinutes>10)
                        {

                            Intent loginIntent = new Intent(SplashScreen.this, LogInActivity.class);
                            startActivity(loginIntent);
                        finish();
                        }
                        else
                        {
                            Intent loginIntent = new Intent(SplashScreen.this, HomeActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }



                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else {

                    Intent loginIntent = new Intent(SplashScreen.this, LogInActivity.class);
                    startActivity(loginIntent);
                    finish();
                }

            }
        }, 1500);
    }


    public  Date  getCurrentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = new Date();

        return  date;

    }

}
