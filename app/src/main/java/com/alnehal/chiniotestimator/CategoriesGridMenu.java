package com.alnehal.chiniotestimator;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import DB.SQLiteDatabaseHelper;
import app.AppConfig;
import model.CategoriesModel;

public class CategoriesGridMenu extends ParentActivity implements View.OnClickListener {


   // LinearLayout llOne, llTwo, llThree, llFour;
    Intent actionIntent = null;
    Dialog updateDialog;
    ProgressBar pbDownloadingMissingData;
    TextView tvProcess;
    TextView tvUserNme;
    static long back_pressed;
    Dialog skippedDialog;
    ListView lvSkippedOrders;
    ArrayAdapter skippedAdapter;
//    RelativeLayout rlOne, rlTwo, rlThree, rlFour, rlFive, rlSix, rlSeven,
//            rlEight, rlNine, rlTen;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_menu);
        getSupportActionBar().setTitle(R.string.categories);

        context = this;

        databaseHelper = new SQLiteDatabaseHelper(CategoriesGridMenu.this);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        AppConfig.DEVICE_HEIGHT = size.y;
        AppConfig.DEVICE_WIDTH = size.x;

        swipeRefreshLayout=findViewById(R.id.swipe_to_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchAllCategoriesRequest();
            }
        });


        for (int i = 0; i < linearLayoutsIds.length; i++) {

            linearLayouts[i] = findViewById(linearLayoutsIds[i]);

            if (i == 0) {
                linearLayouts[i].getLayoutParams().height = 2 * (AppConfig.DEVICE_WIDTH / 3);

            } else {
                linearLayouts[i].getLayoutParams().height = AppConfig.DEVICE_WIDTH / 3;
            }


        }

        for (int i = 0; i < realtiveLayoutIds.length; i++) {

            realtiveLayout[i] = findViewById(realtiveLayoutIds[i]);
            realtiveLayout[i].setOnClickListener(this);

            imageView[i] = findViewById(imageViewIds[i]);
            textView[i] = findViewById(textViewIds[i]);

        }


        if (databaseHelper.getTotalCategories() > 0) {
            categoriesModelArrayList = databaseHelper.getCategories();
            int cat_check=categoriesModelArrayList.size();


            for (int i = 0; i < textView.length; i++) {


                if (cat_check>i)
                {
                    textView[i].setText(categoriesModelArrayList.get(i).getName());

                    if (categoriesModelArrayList.get(i).getImgUrl().equalsIgnoreCase("null"))
                    {
                        Glide.with(context).load(R.drawable.logo).into(imageView[i]);

                    }
                    else
                    {

                        Glide.with(context).load(categoriesModelArrayList.get(i).getImgUrl()).into(imageView[i]);

                    }

                }
                else
                {
                    textView[i].setText("");
                    Glide.with(context).load("").into(imageView[i]);
                }


            }
        } else {
            fetchAllCategoriesRequest();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlOne:
                try {

                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(0).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(0).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlTwo:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(1).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(1).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlThree:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(2).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(2).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlFour:
                try {

                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(3).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(3).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlFive:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(4).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(4).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlSix:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(5).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(5).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlSeven:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(6).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                     /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(6).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlEight:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(7).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(7).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlNine:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(8).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                      /*extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(8).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlTen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;


            case R.id.rlEleven:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(10).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlTwelve:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(11).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlThirteen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(12).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;

            case R.id.rlFourteen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(13).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlFifteen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(14).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlSixteen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(15).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlSeventeen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(16).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlEighteen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(17).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlNinteen:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(18).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlTwenty:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(19).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlTwentyOne:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(20).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlTwentyTwo:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(21).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlTwentyThree:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(22).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlTwentyFour:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(23).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
            case R.id.rlTwentyFive:
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(24).getCatid());
                    gotoActivity(SubCategoriesActivity.class, extras);
                    /* extras = new Bundle();
                    extras.putInt("catID", categoriesModelArrayList.get(9).getCatid());
                    gotoActivity(ItemsActivity.class, extras);*/
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Category not found!", 1, Gravity.CENTER);
                }
                break;
        }
    }
}


