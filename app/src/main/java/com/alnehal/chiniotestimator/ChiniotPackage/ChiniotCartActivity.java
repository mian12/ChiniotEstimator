package com.alnehal.chiniotestimator.ChiniotPackage;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alnehal.chiniotestimator.R;
import com.alnehal.chiniotestimator.print.PrintForm;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import DB.SQLiteDatabaseHelper;
import adapter.MyCartAdapter;
import adapter.MyExtrItemsCartlAdapter;
import model.CartModel;
import model.ExtraDetailCartModel;
import model.ItemModel;
import utilis.MyApplication;
import utilis.MySingleton;

public class ChiniotCartActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView  imageViewDate,imageViewOrderDate,saveButton,backButton,resetButton,printButton;
    TextView dateValueTextView,orderDateValueTextView;



    public ProgressDialog progressDialog;

    int vrCounter=0;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
   public RecyclerView rvPackageCart,rvExtraDetailCart;

    public SQLiteDatabaseHelper db;
    EditText editTextVrValue,editTextDiscountPercent,editTextTax,editTextNoofPersons,editTextDiscountValue;
    TextView menuValueTV,perHeadValueTV,extraValueTV,taxValueTextView,totalValueTextView,netAmountValueTextView;
        ImageView extraItemsImageView;
    ImageView vrUpImageView,vrDownImageView,deleteButton;


    float TotalAmount = 0;
    ArrayList<CartModel> cartDataList=null;
    ArrayList<ExtraDetailCartModel> listExtraItems=null;

    public boolean flagDiscountValue=false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiniot_cart);

     //   context=this;
     //   getSupportActionBar().hide();

        db=new SQLiteDatabaseHelper(ChiniotCartActivity.this);


//
       // getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(R.string.items);

        editTextDiscountPercent= (EditText) findViewById(R.id.editTextDiscount);
        editTextDiscountValue= (EditText) findViewById(R.id.discValue);
        editTextTax= (EditText) findViewById(R.id.editTextTax);
        editTextNoofPersons= (EditText) findViewById(R.id.editTextNoofPersons);


        imageViewDate= (ImageView) findViewById(R.id.imageViewDate);
        imageViewOrderDate= (ImageView) findViewById(R.id.imageViewOrderDate);

        dateValueTextView= (TextView) findViewById(R.id.textViewDateValue);
        orderDateValueTextView= (TextView) findViewById(R.id.orderDateValue);


        if (!MyApplication.percent.equalsIgnoreCase("0"))
        {
            editTextDiscountPercent.setText(MyApplication.percent);
        }



        if (!MyApplication.tax.equalsIgnoreCase("0"))
        {
            editTextTax.setText(MyApplication.tax);
        }

        if (!MyApplication.noOfPersons.equalsIgnoreCase("0"))
        {
            editTextNoofPersons.setText(MyApplication.noOfPersons);
        }
        if (!MyApplication.date.equalsIgnoreCase("0") &&  !MyApplication.orderDate.equalsIgnoreCase("0"))
        {
            dateValueTextView.setText(MyApplication.date);
            orderDateValueTextView.setText(MyApplication.orderDate);
        }
        else
        {
            DateSetter();
        }


        deleteButton= (ImageView) findViewById(R.id.delete);
        deleteButton.setOnClickListener(this);



        taxValueTextView= (TextView) findViewById(R.id.taxValue);
        totalValueTextView= (TextView) findViewById(R.id.totalPerHeadValue);
        netAmountValueTextView= (TextView) findViewById(R.id.netAmountValue);


        printButton= (ImageView) findViewById(R.id.print);
        printButton.setOnClickListener(this);

        vrUpImageView= (ImageView) findViewById(R.id.imageViewVrUp);
        vrUpImageView.setOnClickListener(this);

        vrDownImageView= (ImageView) findViewById(R.id.imageViewVrDown);
        vrDownImageView.setOnClickListener(this);



        progressDialog = new ProgressDialog(ChiniotCartActivity.this);
        progressDialog.setTitle("Attention ");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        extraValueTV= (TextView) findViewById(R.id.extraValue);





        rvPackageCart= (RecyclerView) findViewById(R.id.rvCart);
        rvExtraDetailCart= (RecyclerView) findViewById(R.id.rvExtraCart);

        editTextVrValue= (EditText) findViewById(R.id.editTextVrValue);


        editTextVrValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // not work without set property in xml in edittext like
            // android:imeOptions="actionDone"
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    //do stuff

                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                    db.claerAllItems();




                    try {
                        vrCounter=Integer.parseInt(v.getText().toString());
                    }
                    catch (Exception e)
                    {
                        e.getMessage();
                    }



                    MyApplication.vrnoa_all=vrCounter+"";



                    getDataVrno();

                    return true;
                }
                return false;
            }
        });


        menuValueTV= (TextView) findViewById(R.id.menuValue);
        perHeadValueTV= (TextView) findViewById(R.id.perHeadValue);

        extraItemsImageView= (ImageView) findViewById(R.id.extraItems);
        extraItemsImageView.setOnClickListener(this);


//        backButton= (ImageView) findViewById(R.id.back);
//        backButton.setOnClickListener(this);

        resetButton= (ImageView) findViewById(R.id.reset);
        resetButton.setOnClickListener(this);

        saveButton= (ImageView) findViewById(R.id.save);
        saveButton.setOnClickListener(this);



        try {
           cartDataList = db.getCartVales();
            String vrNoa = cartDataList.get(0).getVrnoa();
            String menu = cartDataList.get(0).getItemPackage();
            String perHead = cartDataList.get(0).getMenuRate();



            editTextVrValue.setText(vrNoa);
            menuValueTV.setText(menu);
            perHeadValueTV.setText(perHead);


        }
        catch (Exception e)
        {
            e.getMessage();
        }


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPackageCart.setLayoutManager(mLayoutManager);
        rvPackageCart.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getApplicationContext());
        rvExtraDetailCart.setLayoutManager(mLayout);
        rvExtraDetailCart.setItemAnimator(new DefaultItemAnimator());



        MyCartAdapter adapter=new MyCartAdapter(ChiniotCartActivity.this,cartDataList);

        rvPackageCart.setAdapter(adapter);



        float  extraAmount=0;

        try
        {
                listExtraItems=db.getExtraItemsCartVales();

            for (int i=0; i<listExtraItems.size(); i++)
            {

                extraAmount+=Float.parseFloat(listExtraItems.get(i).getItemRate());

            }

            extraValueTV.setText(extraAmount+"");

            MyExtrItemsCartlAdapter adapterExtra = new MyExtrItemsCartlAdapter(ChiniotCartActivity.this,listExtraItems,perHeadValueTV,extraValueTV,editTextDiscountValue,taxValueTextView,totalValueTextView,netAmountValueTextView,editTextVrValue,editTextDiscountPercent,editTextTax,editTextNoofPersons);

            rvExtraDetailCart.setAdapter(adapterExtra);

        }
        catch (Exception e)
        {
            e.getMessage();
        }


        float perHead = validaterInteger(perHeadValueTV.getText().toString());
        float extra = validaterInteger(extraValueTV.getText().toString());


        MyApplication.totalAmount=perHead+extra;




        if (MyApplication.vrnoa_all.equalsIgnoreCase("0"))
        {

            editTextVrValue.setText(MyApplication.maxId_global);
        }
        else
        {
            editTextVrValue.setText(MyApplication.vrnoa_all);
        }

        textchangerPercentDiscount();

        textchangerValueDiscount();

        textchangerTaxPercent();

        textchangerNoOfPersons();



//        if (!MyApplication.discount.equalsIgnoreCase("0"))
//        {
//            editTextDiscountValue.setText(MyApplication.discount);
//        }


     ///   getCurrentFocus();



    }





    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ChiniotPackageActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:

                startActivity(new Intent(this,ChiniotPackageActivity.class));
                finish();
                //onBackPressed();
                return true;
            // break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        summaryStarter();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case  R.id.imageViewVrUp:
                try {
                    vrCounter = Integer.parseInt(editTextVrValue.getText().toString());
                }
                catch (Exception e)
                {
                    e.getMessage();
                }
                vrCounter+=1;
                editTextVrValue.setText(vrCounter+"");

                db.claerAllItems();

                editTextVrValue.requestFocus();

                getDataVrno();

                break;

            case  R.id.imageViewVrDown:

                try {
                    vrCounter = Integer.parseInt(editTextVrValue.getText().toString());
                }
                catch (Exception e)
                {
                    e.getMessage();
                }

                vrCounter-=1;

                if (vrCounter<1)
                {
                    vrCounter=1;
                }

                editTextVrValue.setText(vrCounter+"");
                db.claerAllItems();

                editTextVrValue.requestFocus();
                getDataVrno();

                break;





            case R.id.save:
                try
                {



                    if (MyApplication.vrnoa_all.equalsIgnoreCase("0")) {

                        if (MyApplication.insert_user.equalsIgnoreCase("0")) {
                            Toast.makeText(ChiniotCartActivity.this, "Sorry you have no rights", Toast.LENGTH_SHORT).show();
                            break;

                        }


                    } else {

                        if (MyApplication.update_user.equalsIgnoreCase("0"))
                        {
                            Toast.makeText(ChiniotCartActivity.this, "Sorry you have no rights", Toast.LENGTH_SHORT).show();
                            break;

                        }


                    }




                    String mainDate=dateValueTextView.getText().toString();
                    String orderDate=orderDateValueTextView.getText().toString();
                if (cartDataList.size() > 0) {

                JSONObject estimateObject = new JSONObject();

                estimateObject.put("vrnoa", MyApplication.vrnoa_all);
                estimateObject.put("vrno",MyApplication.vrnoa_all);
                estimateObject.put("vrdate", mainDate);
                estimateObject.put("etype", "estimate");
                estimateObject.put("party_id", "1");
                estimateObject.put("order_date", orderDate);

                estimateObject.put("persons", editTextNoofPersons.getText().toString());
                estimateObject.put("per_head", perHeadValueTV.getText().toString());
                estimateObject.put("total_amount", netAmountValueTextView.getText().toString());
                estimateObject.put("advance", "0");
                estimateObject.put("balance", "0");
                    estimateObject.put("discount", editTextDiscountValue.getText().toString());
                    estimateObject.put("discountpercent", editTextDiscountPercent.getText().toString());
                    estimateObject.put("tax", taxValueTextView.getText().toString());
                    estimateObject.put("taxpercent", editTextTax.getText().toString());
                    estimateObject.put("extraperhead", extraValueTV.getText().toString());
                    estimateObject.put("menu_id",cartDataList.get(0).getMenuId() );
                    estimateObject.put("rate_calc", "1");
                    estimateObject.put("uid", MyApplication.userId);

                estimateObject.put("remarks", "");
                estimateObject.put("status1", "");


                estimateObject.put("ph", perHeadValueTV.getText().toString());
                estimateObject.put("total_weight","0");

                JSONArray estimateDetailArray = new JSONArray();
                JSONObject estimateDetailObject;
                for (int i = 0; i < cartDataList.size(); i++) {
                    estimateDetailObject = new JSONObject();
                    estimateDetailObject.put("oid", "0");
                    estimateDetailObject.put("prid", cartDataList.get(i).getItemId());
                    estimateDetailObject.put("qty", "0");
                    estimateDetailObject.put("rate","0");
                    estimateDetailObject.put("mrate", "0");
                    estimateDetailObject.put("amount", "0");
                    estimateDetailObject.put("per", "0");
                    estimateDetailObject.put("weight", "0");
                    estimateDetailObject.put("gweight", "0");
                    estimateDetailObject.put("type", "menu");

                    estimateDetailArray.put(estimateDetailObject);
                }


                    for (int i = 0; i < listExtraItems.size(); i++) {
                        estimateDetailObject = new JSONObject();
                        estimateDetailObject.put("oid", "0");
                        estimateDetailObject.put("prid", listExtraItems.get(i).getItemId());
                        estimateDetailObject.put("rate",listExtraItems.get(i).getItemRate());
                        estimateDetailObject.put("type", "extra");

                        estimateDetailArray.put(estimateDetailObject);
                    }


                    saveData(estimateObject.toString(),estimateDetailArray.toString());




            } else {

               // showToast("Add atleast one item!", 1, Gravity.CENTER);
            }
        }catch (Exception e) {
                e.printStackTrace();
            }

                break;

            case R.id.extraItems:

                    startActivity(new Intent(ChiniotCartActivity.this,ChiniotExtraItemsActivity.class));
                    finish();

                break;


            case R.id.reset:

                resetData();

                break;
            case  R.id.delete:

                if (MyApplication.delete_user.equalsIgnoreCase("0"))
                {
                    Toast.makeText(ChiniotCartActivity.this, "Sorry you have no rights", Toast.LENGTH_SHORT).show();
                    break;

                }


              deleteVrno();
              break;


            case  R.id.print:

                if (MyApplication.print_user.equalsIgnoreCase("0"))
                {
                    Toast.makeText(ChiniotCartActivity.this, "Sorry you have no rights", Toast.LENGTH_SHORT).show();
                    break;

                }

                PrintForm printForm=new PrintForm(ChiniotCartActivity.this,cartDataList,perHeadValueTV.getText().toString(),
                        extraValueTV.getText().toString(),taxValueTextView.getText().toString(),totalValueTextView.getText().toString(),
                        "CP",listExtraItems,dateValueTextView.getText().toString(),orderDateValueTextView.getText().toString(),menuValueTV.getText().toString(),
                        MyApplication.vrnoa_all,editTextNoofPersons.getText().toString(),netAmountValueTextView.getText().toString());


                break;



        }

    }


    public  void resetData()
    {
        db.deleteCart();
        db.deleteExtraItemsCart();
        MyApplication.totalAmount=0;

        MyApplication.vrnoa_all="0";

        MyApplication.date="0";
        MyApplication.orderDate="0";

        DateSetter();



        editTextVrValue.setText("");
        menuValueTV.setText("");
        perHeadValueTV.setText("");


        perHeadValueTV.setText("");
        extraValueTV.setText("");
        editTextDiscountValue.setText("");
        taxValueTextView.setText("");
        totalValueTextView.setText("");


        editTextVrValue.setText("");
        editTextDiscountPercent.setText("");
        editTextTax.setText("");
        editTextNoofPersons.setText("");






        cartDataList = db.getCartVales();

        MyCartAdapter adapter=new MyCartAdapter(ChiniotCartActivity.this,cartDataList);

        rvPackageCart.setAdapter(adapter);




        listExtraItems=db.getExtraItemsCartVales();

        MyExtrItemsCartlAdapter adapterExtra = new MyExtrItemsCartlAdapter(ChiniotCartActivity.this,listExtraItems,perHeadValueTV,extraValueTV,editTextDiscountValue,taxValueTextView,totalValueTextView,netAmountValueTextView,editTextVrValue,editTextDiscountPercent,editTextTax,editTextNoofPersons);

        rvExtraDetailCart.setAdapter(adapterExtra);


        getDataByVrnoa();
//        try {
//
//
////            Thread t=new Thread();
////            t.join();
//            Thread.sleep(1000);
//            editTextVrValue.setText(MyApplication.maxId_global);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //  finish();
    }


    public  void saveData(final String estmateMain, final String estimateDetail)
    {

        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, MyApplication.URL_SAVE_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        saveButton.setEnabled(true);
                        progressDialog.dismiss();

                     //   Log.e("Respone",response);

                        if (response != null) {


                            Toast.makeText(ChiniotCartActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                            resetData();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


                Toast.makeText(ChiniotCartActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

               // int vrnoa=Integer.parseInt(editTextVrValue.getText().toString());
                String vrnoa=MyApplication.vrnoa_all;
                params.put("vrnoa", vrnoa+"");
                if (vrnoa.equalsIgnoreCase("0")) {


                    params.put("voucher_type_hidden", "new");
                } else {
                    params.put("voucher_type_hidden", "edit");


                }
                params.put("etype", "estimate");
                params.put("estimate", estmateMain);
                params.put("estimatedetail", estimateDetail);
                return params;
            }
        };
        // Adding request to request queue
        MySingleton.getInstance().addToReqQueue(postRequest);

    }





    public  void getDataByVrnoa()
    {




        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, MyApplication.URL_VRNOA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();

                       // Log.e("Respone",response);

                        if (response != null) {


                           MyApplication.maxId_global=response;

                            editTextVrValue.setText(MyApplication.maxId_global);

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


                Toast.makeText(ChiniotCartActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("etype", "estimate");

                return params;
            }
        };
        // Adding request to request queue
        MySingleton.getInstance().addToReqQueue(postRequest);

    }



    public  void getDataVrno()
    {


        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, MyApplication.URL_FETCH_VR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();

                      //  Log.e("Respone",response);

                        if (response != null && !response.equalsIgnoreCase("false")) {


                            parseFetchVrno(response);


                        }
                        else
                        {

                            Toast.makeText(ChiniotCartActivity.this,
                                    "No Data Found", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


                Toast.makeText(ChiniotCartActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    params.put("etype", "estimate");
                    params.put("vrnoa", editTextVrValue.getText().toString());
                }
                catch (Exception e)
                {

                }


                return params;
            }
        };
        // Adding request to request queue
        MySingleton.getInstance().addToReqQueue(postRequest);

    }



    public  void deleteVrno()
    {


        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, MyApplication.URL_DELETE_VR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();

                       // Log.e("Respone",response);

                        if (response != null && !response.equalsIgnoreCase("false")) {






                            Toast.makeText(ChiniotCartActivity.this,
                                    "Voucher deleted successfully !", Toast.LENGTH_SHORT).show();

                            resetData();
                            //  MyApplication.maxId_global=response;

                        }
                        else
                        {

                            Toast.makeText(ChiniotCartActivity.this,
                                    "No Data Found", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


                Toast.makeText(ChiniotCartActivity.this,
                        "something went wrong", Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("etype", "estimate");
                params.put("vrnoa", editTextVrValue.getText().toString());

                return params;
            }
        };
        // Adding request to request queue
        MySingleton.getInstance().addToReqQueue(postRequest);

    }





    private void parseFetchVrno(String response) {




        try {



            db.deleteCart();
            db.deleteExtraItemsCart();
            MyApplication.totalAmount=0;
            MyApplication.maxId_global="0";

            MyApplication.percent="0";
            MyApplication.tax="0";
            MyApplication.noOfPersons="0";

            MyApplication.date="0";
            MyApplication.orderDate="0";


            editTextVrValue.setText("");
            menuValueTV.setText("");
            perHeadValueTV.setText("");


            perHeadValueTV.setText("");
            extraValueTV.setText("");
            editTextDiscountValue.setText("");
            taxValueTextView.setText("");
            totalValueTextView.setText("");
            netAmountValueTextView.setText("");
            editTextVrValue.setText("");
            editTextDiscountPercent.setText("");
            editTextTax.setText("");
            editTextNoofPersons.setText("");


// parsing json array without name
            JSONArray dataArray = new JSONArray(response);
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);


                String menu_id = jsonObject.getString("menu_id");
                String vrnoa= jsonObject.getString("vrnoa");
                String vrdate= jsonObject.getString("vrdate1");
                String order_date= jsonObject.getString("order_date1");
                String per_head= jsonObject.getString("per_head");
                String extraperhead= jsonObject.getString("extraperhead");
                String discountpercent= jsonObject.getString("discountpercent");
                String discount= jsonObject.getString("discount");
                String tax= jsonObject.getString("tax");
                String taxpercent= jsonObject.getString("taxpercent");

                String persons= jsonObject.getString("persons");
                String item_id= jsonObject.getString("prid");
                String item_des= jsonObject.getString("products_name");
                String menu_name= jsonObject.getString("menu_name");
                String type= jsonObject.getString("type");
                String price= jsonObject.getString("rate");

                MyApplication.vrnoa_all=vrnoa;

                if (i==0)
                {


                    editTextVrValue.setText(vrnoa);
                    menuValueTV.setText(menu_name);
                    dateValueTextView.setText(vrdate);
                    orderDateValueTextView.setText(order_date);
                    perHeadValueTV.setText(per_head);
                    extraValueTV.setText(extraperhead);

                    editTextDiscountValue.setText(discount);
                    taxValueTextView.setText(tax);

                    editTextDiscountPercent.setText(discountpercent);
                    editTextTax.setText(taxpercent);
                    editTextNoofPersons.setText(persons);

                    MyApplication.date=vrdate;
                    MyApplication.orderDate=order_date;

                    MyApplication.percent=discountpercent;
                    MyApplication.tax=taxpercent;
                    MyApplication.noOfPersons=persons;





                }

                if (type.equalsIgnoreCase("menu")) {



                    db.isAddedToCartPackage(item_id, item_des, per_head, "", menu_name, vrnoa, menu_id);

                }
                else
                {
                    db.addedToExtraItems(item_id, item_des, price,"true");


                }


            }


//            // sometimes its  not shown value on edittext thats why i  store this value on global alue then set it on edittext
//
//            editTextDiscountPercent.setText(MyApplication.percent);
//            taxValueTextView.setText(MyApplication.tax);
//            editTextNoofPersons.setText(  MyApplication.noOfPersons);




            summaryStarter();


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }





    public  void  summaryStarter()


    {



        cartDataList = db.getCartVales();
        if (cartDataList!=null)
        {
            MyCartAdapter adapter=new MyCartAdapter(ChiniotCartActivity.this,cartDataList);

            rvPackageCart.setAdapter(adapter);
        }






        float extraAmount = 0;
        listExtraItems= db.getExtraItemsCartVales();
        for (int i = 0; i < listExtraItems.size(); i++) {

            try{
                extraAmount += Float.parseFloat(listExtraItems.get(i).getItemRate());
            }catch (Exception e)
            {
                e.getMessage();
            }


        }


        if (listExtraItems != null) {
            MyExtrItemsCartlAdapter adapterExtra = new MyExtrItemsCartlAdapter(ChiniotCartActivity.this, listExtraItems, perHeadValueTV, extraValueTV, editTextDiscountValue, taxValueTextView, totalValueTextView, netAmountValueTextView, editTextVrValue, editTextDiscountPercent, editTextTax, editTextNoofPersons);

            rvExtraDetailCart.setAdapter(adapterExtra);
            extraValueTV.setText(extraAmount + "");


            MyApplication.totalAmount = 0;

            float perHead = validaterInteger(perHeadValueTV.getText().toString());
            float extra = validaterInteger(extraValueTV.getText().toString());


            MyApplication.totalAmount = perHead + extra;


            // sometimes its  not showb value on edittext thats why i  store this value on global alue then set it on edittext

            String  percent=editTextDiscountPercent.getText().toString();
            String  tax=taxValueTextView.getText().toString();
            String  noOfPersons=editTextNoofPersons.getText().toString();




            finalAmountCalculator();


        }
    }




    public void textchangerPercentDiscount() {



        editTextDiscountPercent.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (editTextDiscountPercent.hasFocus()) {
                    editTextDiscountValue.setText("");
                    finalAmountCalculator();
                }
                else
                {
                    Log.d("Percent","false");
                }


            }
        });


    }


    public void textchangerValueDiscount() {


        editTextDiscountValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {




            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {




                if (editTextDiscountValue.hasFocus())
                {

                    editTextDiscountPercent.setText("");

                    float discountValue = validaterInteger(editTextDiscountValue.getText().toString());

                    TotalAmount = MyApplication.totalAmount;

                    if (discountValue != 0 && TotalAmount != 0) {


                        float discountPercent = 0;
                        discountPercent = discountValue * 100 / TotalAmount;


                         String afterFormate = String.format("%.3f", discountPercent);



                        editTextDiscountPercent.setText(afterFormate);
                    } else {
                        editTextDiscountPercent.setText("");
                    }





                    float discAmount = validaterInteger(editTextDiscountValue.getText().toString());
                    float taxAmount = validaterInteger(taxValueTextView.getText().toString());
                    float noOfPersons = validaterInteger(editTextNoofPersons.getText().toString());

                    MyApplication.noOfPersons=editTextNoofPersons.getText().toString();
                   // MyApplication.discount=editTextDiscountValue.getText().toString();
                    MyApplication.percent=editTextDiscountPercent.getText().toString();


                    float sum = TotalAmount - discAmount + taxAmount ;

                    totalValueTextView.setText(Math.round(sum) + "");


                    if (noOfPersons==0)
                    {
                        netAmountValueTextView.setText(Math.round(sum) + "");

                    }
                    else
                    {
                        float sumWithPersons = sum*noOfPersons ;
                        netAmountValueTextView.setText(Math.round(sumWithPersons) + "");
                    }

                }
                else
                {




                  //  Log.d("DiscountValue","false");
                }






            }
        });


    }



    public void textchangerTaxPercent() {




        editTextTax.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                finalAmountCalculator();

            }
        });


    }



    public void textchangerNoOfPersons() {




        editTextNoofPersons.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                finalAmountCalculator();

            }
        });


    }



    public float percentCalculator(float perecnt)    {
        TotalAmount = MyApplication.totalAmount;
        float res = (TotalAmount / 100) * perecnt;
        return res;
    }

    public    void finalAmountCalculator() {
        try
        {

            TotalAmount = MyApplication.totalAmount;


            MyApplication.percent=editTextDiscountPercent.getText().toString();


                float discountPercent = validaterInteger(editTextDiscountPercent.getText().toString());

                if (discountPercent != 0) {
                    float discountedRupess = 0;
                    discountedRupess = percentCalculator(discountPercent);
                    editTextDiscountValue.setText(Math.round(discountedRupess) + "");
                } else {
                    editTextDiscountValue.setText("");
                }



            MyApplication.tax=editTextTax.getText().toString();

            float taxPercent = validaterInteger(editTextTax.getText().toString());

            if (taxPercent!= 0) {
                float taxRupess = 0;
                taxRupess = percentCalculator(taxPercent);
                taxValueTextView.setText(Math.round(taxRupess)+ "");
            }else {
                taxValueTextView.setText("0");
            }





            float discAmount = validaterInteger(editTextDiscountValue.getText().toString());
            float taxAmount = validaterInteger(taxValueTextView.getText().toString());
            float noOfPersons = validaterInteger(editTextNoofPersons.getText().toString());


            MyApplication.noOfPersons=editTextNoofPersons.getText().toString();



            float sum = TotalAmount - discAmount + taxAmount ;

            totalValueTextView.setText(Math.round(sum) + "");


            if (noOfPersons==0)
            {
                netAmountValueTextView.setText(Math.round(sum) + "");

            }
            else
            {
                float sumWithPersons = sum*noOfPersons ;
                netAmountValueTextView.setText(Math.round(sumWithPersons) + "");
            }

        }
        catch ( Exception ex)
        {
          //  Log.d("Exception",ex.getMessage() );
        }

    }




    public  float  validaterInteger(String  val)
    {  float   result=0;

        try
        {
            if (val.length()!=0)
            {
                result=Float.parseFloat(val);


            }
        }
        catch (Exception ex)
        {
            return  0;

        }
        return  result;
    }

    public  void dialogItems( ArrayList<ExtraDetailCartModel> extraItemsArrayList)
    {
        // custom dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView= inflater.inflate(R.layout.row_alert_extra_items, null);
        dialogBuilder.setView(dialogView);

        for (int i=0; i<extraItemsArrayList.size(); i++)
        {

            extraItemsArrayList.get(i).getItemName();

            TextView  name = (TextView)dialogView.findViewById(R.id.tvEngName);
            TextView  price = (TextView)dialogView.findViewById(R.id.rate);
            Button addedToCart = (Button)dialogView.findViewById(R.id.added);
        }





//        addedToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //Commond here......
//
//            }
//        });




        dialogBuilder.create().show();
    }




    public  void DateSetter()
    {



        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);





        int hour = cal.get(Calendar.HOUR);
        int mint = cal.get(Calendar.MINUTE);
        //  int amPM = cal.get(Calendar.AM_PM);

        // String timeMInt=hour+":"+mint;



       // String userDate=day + "/" + (month+1) + "/" + year;

        String userDate=year + "-" + (month+1) + "-" + day;

        dateValueTextView.setText(userDate);
        orderDateValueTextView.setText(userDate);



        imageViewDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DateDialogMianDate();

            }
        });


        imageViewOrderDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DateDialogOrderDate();

            }
        });



    }


    public void DateDialogMianDate(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {

                //int hour = cal.get(Calendar.HOUR);
                // int mint = cal.get(Calendar.MINUTE);
                //  int amPM = cal.get(Calendar.AM_PM);

                //String timeMInt=hour+":"+mint;
                //String userDate=dayOfMonth + "/" + (monthOfYear+1) + "/" + year;

                String userDate=year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                dateValueTextView.setText(userDate);



            }};

        DatePickerDialog dpDialog=new DatePickerDialog(ChiniotCartActivity.this, listener, year, month, day);
        dpDialog.show();

    }

    public void DateDialogOrderDate(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {

                //int hour = cal.get(Calendar.HOUR);
                // int mint = cal.get(Calendar.MINUTE);
                //  int amPM = cal.get(Calendar.AM_PM);

                //String timeMInt=hour+":"+mint;
              //  String userDate=dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
                String userDate=year + "-" + (monthOfYear+1) + "-" + dayOfMonth;

                orderDateValueTextView.setText(userDate);



            }};

        DatePickerDialog dpDialog=new DatePickerDialog(ChiniotCartActivity.this, listener, year, month, day);
        dpDialog.show();

    }



}
