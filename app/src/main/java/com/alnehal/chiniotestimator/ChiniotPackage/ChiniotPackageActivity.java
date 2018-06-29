package com.alnehal.chiniotestimator.ChiniotPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alnehal.chiniotestimator.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.MyPackageMenuAdapter;
import adapter.MyPackageMenuGridBaseAdapter;
import model.PackageListModel;
import utilis.MyApplication;
import utilis.MySingleton;

public class ChiniotPackageActivity extends ChiniotBadgeActivity {

    RecyclerView recyclerViewPackages;
    public ProgressDialog progressDialog;

    ImageView backButton;
    public ArrayList<PackageListModel> packageArrayList;

    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiniot_package);


       // getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle(R.string.items);

        context=this;

        progressDialog = new ProgressDialog(ChiniotPackageActivity.this);
        progressDialog.setTitle("Package Menu");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

//
//        backButton= (ImageView) findViewById(R.id.back);
//        backButton= (ImageView) findViewById(R.id.back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            startActivity(new Intent(ChiniotPackageActivity.this,HomeActivity.class));
//             finish();
//            }
//        });



        gridView= (GridView)findViewById(R.id.gridview);

// Note.................... Start here .......................


       // recyclerViewPackages= (RecyclerView) findViewById(R.id.rvPacakges);
     //   int numberOfColumns = 2;

      //  int numberOfColumns= calculateNoOfColumns(this);

      //  recyclerViewPackages.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
       // recyclerViewPackages.setHasFixedSize(true);
       // recyclerViewPackages.addItemDecoration(new GridSpacing(columnQty.calculateSpacing()));


      //  gettingPartyNameListFromDatabase(recyclerViewPackages);

        // Note.................... end  here .......................


       gettingPartyNameListFromDatabase(gridView);

    }




    @Override
    protected void onResume() {
        super.onResume();

       updateCartCounter();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void gettingPartyNameListFromDatabase(final GridView recyclerViewPackages) {

        progressDialog.show();

        packageArrayList=new ArrayList<>();

        final StringRequest getRequest = new StringRequest(Request.Method.GET, MyApplication.URL_PACKAGE_MENU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response != null) {

                        //    Log.d("response",  response + " " + "response");


                            ArrayList<PackageListModel> packageMenuList= parsePackageMenu(response);


                            ArrayList<ArrayList<PackageListModel>> listGlobal=new ArrayList<>();


                            String prevItem="";

                            ArrayList<PackageListModel> list1=null;

                            String currentItem="";

                            for (int i=0; i<packageMenuList.size(); i++)
                            {

                                currentItem=packageMenuList.get(i).getName();

                                if(!prevItem.equalsIgnoreCase(currentItem))
                                {
                                    if (i!=0)
                                    {
                                        // push final list
                                        listGlobal.add(list1);
                                    }
                                    list1=new ArrayList<>();
                                    prevItem=packageMenuList.get(i).getName();

                                }

                                PackageListModel object=new PackageListModel();

                                object.setActive(packageMenuList.get(i).getActive());
                                object.setMenu_id(packageMenuList.get(i).getMenu_id());
                                object.setVrnoa(packageMenuList.get(i).getVrnoa());
                                object.setName(packageMenuList.get(i).getName());
                                object.setMenu_rate(packageMenuList.get(i).getMenu_rate());
                                object.setDescription(packageMenuList.get(i).getDescription());
                                object.setItem_des(packageMenuList.get(i).getItem_des());
                                object.setUid(packageMenuList.get(i).getUid());
                                object.setItem_id(packageMenuList.get(i).getItem_id());
                                object.setItem_des(packageMenuList.get(i).getItem_des());
                                object.setUom(packageMenuList.get(i).getUom());
                                object.setUser_name(packageMenuList.get(i).getUser_name());
                                object.setCatid(packageMenuList.get(i).getCatid());
                                object.setCategory_name(packageMenuList.get(i).getCategory_name());
                                object.setServing(packageMenuList.get(i).getServing());
                                object.setPer_person(packageMenuList.get(i).getPer_person());
                                object.setWeight_meat(packageMenuList.get(i).getWeight_meat());
                                object.setMrate(packageMenuList.get(i).getMrate());

                                list1.add(object);

                                if (i==packageMenuList.size()-1)
                                {
                                    // push final list
                                    listGlobal.add(list1);
                                }


                            }

                           // Log.d("packageSize",packageMenuList.size()+"");

                          // MyPackageMenuAdapter adapter= new MyPackageMenuAdapter(ChiniotPackageActivity.this,listGlobal);

                            MyPackageMenuGridBaseAdapter adapter=new MyPackageMenuGridBaseAdapter(ChiniotPackageActivity.this,listGlobal);
                            recyclerViewPackages.setAdapter(adapter);



                        } else {
                            Toast.makeText(ChiniotPackageActivity.this, "Response" + " " + response, Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("error", error.toString());

                Toast.makeText(ChiniotPackageActivity.this,
                        "failed to Get Data", Toast.LENGTH_SHORT).show();
            }
        });


       // getRequest.setShouldCache(false);
        // Adding request to request queue


        // MySingleton.getInstance().getReqQueue().getCache().clear();

        MySingleton.getInstance().addToReqQueue(getRequest);
    }



    private ArrayList<PackageListModel> parsePackageMenu(String response) {


        ArrayList<PackageListModel> arrayList = new ArrayList<>();

        try {


// parsing json array withot name
            JSONArray dataArray = new JSONArray(response);
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String active = jsonObject.getString("active");
                String menu_id = jsonObject.getString("menu_id");
                String vrnoa= jsonObject.getString("vrnoa");
                String name= jsonObject.getString("name");
                String menu_rate= jsonObject.getString("menu_rate");
                String description= jsonObject.getString("description");
                String uid= jsonObject.getString("uid");
                String item_id= jsonObject.getString("item_id");
                String item_des= jsonObject.getString("item_des");
                String uom= jsonObject.getString("uom");
                String user_name= jsonObject.getString("user_name");
                String catid= jsonObject.getString("catid");
                String category_name= jsonObject.getString("category_name");
                String serving= jsonObject.getString("serving");
                String per_person= jsonObject.getString("per_person");
                String weight_meat= jsonObject.getString("weight_meat");
                String mrate= jsonObject.getString("mrate");




                PackageListModel object=new PackageListModel();

                object.setActive(active);
                object.setMenu_id(menu_id);
                object.setVrnoa(vrnoa);
                object.setName(name);
                object.setMenu_rate(menu_rate);
                object.setDescription(description);
                object.setUid(uid);
                object.setItem_id(item_id);
                object.setItem_des(item_des);
                object.setUom(uom);
                object.setUser_name(user_name);
                object.setCatid(catid);
                object.setCategory_name(category_name);
                object.setServing(serving);
                object.setPer_person(per_person);
                object.setWeight_meat(weight_meat);
                object.setMrate(mrate);





                arrayList.add(object);

            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arrayList;
    }



    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }


    // Recyclerview Click Listenere  below/////

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }


    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ChiniotPackageActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ChiniotPackageActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

}
