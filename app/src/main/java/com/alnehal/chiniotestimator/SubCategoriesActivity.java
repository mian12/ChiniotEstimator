package com.alnehal.chiniotestimator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import DB.SQLiteDatabaseHelper;
import adapter.CategoriesAdapter;
import adapter.SubCategoriesAdapter;
import helper.PixelDpConverter;
import helper.VerticalSpaceItemDecoration;

public class SubCategoriesActivity extends ParentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategories);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sub_categories);
        initView();
    }

    private void initView() {
        context = this;
        databaseHelper = new SQLiteDatabaseHelper(context);
        dialog = new ProgressDialog(context);
        rvSubCategories = (RecyclerView) findViewById(R.id.rvSubCategories);
        LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvSubCategories.setLayoutManager(itemsLayoutManager);
        rvSubCategories.addItemDecoration(new VerticalSpaceItemDecoration((int) PixelDpConverter.convertPixelsToDp(getResources().getInteger(R.integer.dp5), context)));
        fetchAllSubCategoriesByCategoryRequest(getIntent().getExtras().getInt("catID"));
        /*if (databaseHelper.getTotalSubCategories() > 0) {
            subCategoriesModelArrayList = databaseHelper.getSubCategoriesByCatID(getIntent().getExtras().getInt("catID"));
            subCategoriesAdapter = new SubCategoriesAdapter(context, subCategoriesModelArrayList);
            rvSubCategories.setAdapter(subCategoriesAdapter);
        } else {
            fetchAllSubCategoriesRequest();
        }*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
}
