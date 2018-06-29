package com.alnehal.chiniotestimator.ChiniotPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alnehal.chiniotestimator.R;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;

import DB.SQLiteDatabaseHelper;

/**
 * Created by Mian Shahbaz Idrees on 08-Mar-18.
 */

public class ChiniotBadgeActivity extends AppCompatActivity {

    public Context context;

    public SQLiteDatabaseHelper databaseHelper;


    MenuItem item;
    BadgeStyle badgeStyle;
    ActionItemBadge.ActionItemBadgeListener listener;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        if (context instanceof ChiniotPackageActivity ) {
            // Inflate the menu; this adds items to the action bar if it is present.
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main_menu, menu);

            item = menu.findItem(R.id.action_cart);


            badgeStyle = ActionItemBadge.BadgeStyles.GREY.getStyle();
            badgeStyle.setTextColor(Color.WHITE);
            listener = new ActionItemBadge.ActionItemBadgeListener() {
                @Override
                public boolean onOptionsItemSelected(MenuItem menu) {
                    Intent cartIntent = new Intent(context, ChiniotCartActivity.class);
                    startActivity(cartIntent);
                    return false;
                }
            };

            ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, 0);


        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (context instanceof ChiniotPackageActivity )  {
            updateCartCounter();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateCartCounter() {

        databaseHelper = new SQLiteDatabaseHelper(ChiniotBadgeActivity.this);

        badgeStyle = ActionItemBadge.BadgeStyles.GREY.getStyle();

        int  cartItemsDB= databaseHelper.packageExitsOrNot();
        if (cartItemsDB>0)
        {


                badgeStyle.setColor(Color.GRAY);
                ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, 1, listener);


        }
        else
        {

            badgeStyle.setColor(Color.TRANSPARENT);
            ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, null, listener);

        }


    }

}
