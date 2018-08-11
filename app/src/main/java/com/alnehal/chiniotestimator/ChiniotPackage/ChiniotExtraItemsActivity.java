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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alnehal.chiniotestimator.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import adapter.ExpandListAdapter;
import adapter.MyExtraItemsAdapter;
import adapter.MyExtraItemsBaseAdapter;
import model.Child;
import model.ExtraDetailCartModel;
import model.Group;
import model.ItemModel;
import utilis.MyApplication;
import utilis.MySingleton;

public class ChiniotExtraItemsActivity extends AppCompatActivity {

    SearchView searchView;

    public ProgressDialog progressDialog;

    public RecyclerView rvExtraItemsCart;

    private ExpandListAdapter expandAdapter;
    private ExpandableListView expandList;
    ProgressDialog PD;



    ArrayList<ItemModel> itemArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiniot_extra_items);





        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



      //  searchView= (SearchView)findViewById(R.id.searchView);

        expandList = (ExpandableListView) findViewById(R.id.exp_list);



        progressDialog = new ProgressDialog(ChiniotExtraItemsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


       // makejsonobjreq();


//        rvExtraItemsCart= (RecyclerView) findViewById(R.id.rvExtraItems);
//        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getApplicationContext());
//        rvExtraItemsCart.setLayoutManager(mLayout);
//        rvExtraItemsCart.setItemAnimator(new DefaultItemAnimator());

       // gettingExtraItemsListFromDatabase(rvExtraItemsCart);


        getExtraItems();
    }




    public  void getExtraItems()
    {



        progressDialog.show();


        final StringRequest getRequest = new StringRequest(Request.Method.GET, MyApplication.URL_EXTRA_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {


                        if (response != null) {


                            ArrayList<String> catArrayList=new ArrayList<>();



                            try
                            {
                                JSONArray jsonArray=new JSONArray(response);
                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                    String category_name=jsonObject.getString("category_name");
                                    String per_head_rate=jsonObject.getString("per_head_rate");
                                    String catid=jsonObject.getString("catid");
                                    String product_id=jsonObject.getString("product_id");
                                    String product_Name=jsonObject.getString("name");

                                    ItemModel object=new ItemModel();
                                    object.setCategory_name(category_name);
                                    object.setPer_head_rate(per_head_rate);
                                    object.setCatid(Integer.parseInt(catid));
                                    object.setProduct_id(Integer.parseInt(product_id));
                                    object.setProduct_name(product_Name);

                                    itemArrayList.add(object);

                                    /// add group name if not exits in arraylist

                                    if (!catArrayList.contains(category_name) ) {
                                        catArrayList.add(category_name);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Log.e("list",itemArrayList.size()+"");



                            ArrayList<Group> groupsArrayList=new ArrayList<>();

                            for (String groupName:catArrayList)
                            {
                                ArrayList<Child>  childArrayList=new ArrayList<>();

                                Group group=new Group();
                                group.setName(groupName);

                                for (int i=0; i<itemArrayList.size(); i++)
                                {

                                    if (itemArrayList.get(i).getCategory_name().equalsIgnoreCase(groupName))
                                    {
                                        Child child=new Child();
                                        child.setName(itemArrayList.get(i).getProduct_name());
                                        child.setCatid(itemArrayList.get(i).getCatid()+"");
                                        child.setPer_head_rate(itemArrayList.get(i).getPer_head_rate());
                                        child.setProduct_id(itemArrayList.get(i).getProduct_id()+"");

                                        childArrayList.add(child);


                                    }
                                }

                                group.setItems(childArrayList);

                                groupsArrayList.add(group);

                            }


                            expandAdapter = new ExpandListAdapter(
                                    ChiniotExtraItemsActivity.this, groupsArrayList);
                            expandList.setAdapter(expandAdapter);

                            progressDialog.dismiss();



                        }

                        else {
                            Toast.makeText(ChiniotExtraItemsActivity.this, "Response" + " "+response , Toast.LENGTH_LONG).show();
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









    private void makejsonobjreq() {

        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, MyApplication.URL_EXTRA_ITEMS,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Group> list = new ArrayList<Group>();
                ArrayList<Child> ch_list;

                try {

                    Iterator<String> key = response.keys();
                    while (key.hasNext()) {
                        String k = key.next();

                        Group gru = new Group();
                        gru.setName(k);
                        ch_list = new ArrayList<Child>();

                        JSONArray ja = response.getJSONArray(k);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);

                            Child ch = new Child();
                            ch.setName(jo.getString("name"));
                            ch_list.add(ch);
                        } // for loop end
                        gru.setItems(ch_list);
                        list.add(gru);
                    } // while loop end



                    expandAdapter = new ExpandListAdapter(
                            ChiniotExtraItemsActivity.this, list);
                    expandList.setAdapter(expandAdapter);

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChiniotExtraItemsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        MySingleton.getInstance().addToReqQueue(jsonObjReq);
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
