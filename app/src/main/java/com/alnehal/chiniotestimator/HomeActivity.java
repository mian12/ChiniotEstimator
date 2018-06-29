package com.alnehal.chiniotestimator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alnehal.chiniotestimator.ChiniotPackage.ChiniotPackageActivity;

import utilis.MyApplication;
import utilis.SharedPreferenceClass;

public class HomeActivity extends AppCompatActivity{


    ImageView estimateButton,packageButton,logOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);





        estimateButton= (ImageView) findViewById(R.id.buttonEstimated1);
        packageButton= (ImageView) findViewById(R.id.buttonPackage1);
        logOutButton= (ImageView) findViewById(R.id.buttonLogOut);


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(HomeActivity.this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                SharedPreferenceClass.getInstance(getApplicationContext(), "ChiniotEstimater", MODE_PRIVATE);
                                SharedPreferenceClass.clearAll();


                                finish();


                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });


        estimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CategoriesGridMenu.class));
            }
        });

        packageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ChiniotPackageActivity.class));
            }
        });


    }
}
