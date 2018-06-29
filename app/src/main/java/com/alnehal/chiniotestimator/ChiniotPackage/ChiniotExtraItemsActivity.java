package com.alnehal.chiniotestimator.ChiniotPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alnehal.chiniotestimator.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.MyExtraItemsAdapter;
import adapter.MyExtraItemsBaseAdapter;
import model.ExtraDetailCartModel;
import utilis.MyApplication;
import utilis.MySingleton;

public class ChiniotExtraItemsActivity extends AppCompatActivity {

    SearchView searchView;

    public ProgressDialog progressDialog;

    public RecyclerView rvExtraItemsCart;
    ImageView backButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiniot_extra_items);





        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        searchView= (SearchView)findViewById(R.id.searchView);

//        backButton= (ImageView) findViewById(R.id.back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(ChiniotExtraItemsActivity.this,ChiniotCartActivity.class));
//                finish();
//            }
//        });


//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        //getSupportActionBar().setTitle(R.string.items);

        progressDialog = new ProgressDialog(ChiniotExtraItemsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

     //   listView= (ListView) findViewById(R.id.rvExtraItems);

        rvExtraItemsCart= (RecyclerView) findViewById(R.id.rvExtraItems);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getApplicationContext());
        rvExtraItemsCart.setLayoutManager(mLayout);
        rvExtraItemsCart.setItemAnimator(new DefaultItemAnimator());

        gettingExtraItemsListFromDatabase(rvExtraItemsCart);

    }

//    @Override
//    public void onBackPressed() {
//
//    }
//


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ChiniotCartActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:

               startActivity(new Intent(this,ChiniotCartActivity.class));
                finish();
                //onBackPressed();
                return true;
               // break;
        }
        return false;
    }




    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void gettingExtraItemsListFromDatabase(final RecyclerView recyclerView) {

        progressDialog.show();


        final StringRequest getRequest = new StringRequest(Request.Method.GET, MyApplication.URL_EXTRA_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response != null) {



                            ArrayList<ExtraDetailCartModel> extraItemsArrayList= parseExtraItemsMenu(response);


                           final MyExtraItemsAdapter adapter=new MyExtraItemsAdapter(ChiniotExtraItemsActivity.this,extraItemsArrayList);
//                              final MyExtraItemsBaseAdapter adapter=new MyExtraItemsBaseAdapter(ChiniotExtraItemsActivity.this,extraItemsArrayList);
//

                            recyclerView.setAdapter(adapter);

                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String query) {

                                    adapter.getFilter().filter(query);
                                    return false;
                                }
                            });


                        } else {
                            Toast.makeText(ChiniotExtraItemsActivity.this, "Response" + " " + response, Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("error", error.toString());

                Toast.makeText(ChiniotExtraItemsActivity.this,
                        "failed to Get Data", Toast.LENGTH_SHORT).show();
            }
        });


        MySingleton.getInstance().addToReqQueue(getRequest);
    }



    private ArrayList<ExtraDetailCartModel> parseExtraItemsMenu(String response) {


        ArrayList<ExtraDetailCartModel> arrayList = new ArrayList<>();

        try {


        // parsing json array withot name
            JSONArray dataArray = new JSONArray(response);
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String itemId = jsonObject.getString("product_id");
                String itemName = jsonObject.getString("name");
                String itemRate= jsonObject.getString("per_head_rate");




                ExtraDetailCartModel object=new ExtraDetailCartModel();
                object.setItemId(itemId);
                object.setItemName(itemName);
                object.setItemRate(itemRate);
               // object.setItemAdded("false");


                arrayList.add(object);

            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arrayList;
    }





}
