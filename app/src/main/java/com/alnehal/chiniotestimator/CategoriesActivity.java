package com.alnehal.chiniotestimator;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;

import DB.SQLiteDatabaseHelper;
import adapter.CategoriesAdapter;
import helper.PixelDpConverter;
import helper.VerticalSpaceItemDecoration;

public class CategoriesActivity extends ParentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.categories);
        initView();
    }

    private void initView() {
        context = this;
        dialog = new ProgressDialog(context);
        databaseHelper = new SQLiteDatabaseHelper(context);
        rvCategories = (RecyclerView) findViewById(R.id.rvCategories);

        LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvCategories.setLayoutManager(itemsLayoutManager);

        rvCategories.addItemDecoration(new VerticalSpaceItemDecoration((int) PixelDpConverter.convertPixelsToDp(getResources().getInteger(R.integer.dp5), context)));

        if (databaseHelper.getTotalCategories() > 1) {
            categoriesModelArrayList = databaseHelper.getCategories();
            categoriesAdapter = new CategoriesAdapter(context, categoriesModelArrayList);
            rvCategories.setAdapter(categoriesAdapter);
        } else {
            fetchAllCategoriesRequest();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
}
