package com.alnehal.chiniotestimator;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import DB.SQLiteDatabaseHelper;

public class LogInActivity extends ParentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initView();
    }

    private void initView() {

        context = this;
        databaseHelper=new SQLiteDatabaseHelper(context);
        dialog=new ProgressDialog(context);

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tilName = (TextInputLayout) findViewById(R.id.tilName);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);

        btnLogIn.setOnClickListener(this);

        etUserName.addTextChangedListener(new CustomTextWatcher(etUserName));
        etPassword.addTextChangedListener(new CustomTextWatcher(etPassword));
    }
}
