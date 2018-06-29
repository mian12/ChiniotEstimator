package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alnehal.chiniotestimator.ChiniotPackage.ChiniotBadgeActivity;
import com.alnehal.chiniotestimator.ChiniotPackage.ChiniotCartActivity;
import com.alnehal.chiniotestimator.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.SQLiteDatabaseHelper;
import model.PackageListModel;
import utilis.MyApplication;
import utilis.MySingleton;

/**
 * Created by Mian Shahbaz Idrees on 20-Mar-18.
 */

public class MyPackageMenuGridBaseAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    ArrayList<ArrayList<PackageListModel>> dataList;
    public SQLiteDatabaseHelper db;

    public ProgressDialog progressDialog;

    public MyPackageMenuGridBaseAdapter(Context context,   ArrayList<ArrayList<PackageListModel>> dataList)
    {

        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
        db=new SQLiteDatabaseHelper(context);


        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Package Menu");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        View itemView = inflater.inflate(R.layout.row_menu_package, parent, false);


        TextView chicken1Tv, chicken2Tv, chicken3Tv,chicken4Tv,chicken5Tv,chicken6Tv,chicken7Tv,chicken8Tv,chicken9Tv,chicken10Tv;
        TextView packageName,perHead;
        CardView cardView;


        cardView= (CardView) itemView.findViewById(R.id.card_view);

        packageName = (TextView) itemView.findViewById(R.id.textViewPacakgeName);

        chicken1Tv = (TextView) itemView.findViewById(R.id.textViewName1);
        chicken2Tv = (TextView) itemView.findViewById(R.id.textViewName2);
        chicken3Tv = (TextView) itemView.findViewById(R.id.textViewName3);
        chicken4Tv = (TextView) itemView.findViewById(R.id.textViewName4);
        chicken5Tv = (TextView) itemView.findViewById(R.id.textViewName5);
        chicken6Tv = (TextView) itemView.findViewById(R.id.textViewName6);
        chicken7Tv = (TextView) itemView.findViewById(R.id.textViewName7);
        chicken8Tv = (TextView) itemView.findViewById(R.id.textViewName8);
        chicken9Tv = (TextView) itemView.findViewById(R.id.textViewName9);
        chicken10Tv = (TextView) itemView.findViewById(R.id.textViewName10);

        perHead = (TextView) itemView.findViewById(R.id.textViewPerHeadValue);



        for (int i=position; i<=position; i++)
        {

            for (int j=0; j<dataList.get(i).size(); j++)
            {



                String Item_des= dataList.get(i).get(j).getItem_des();
                String name= dataList.get(i).get(j).getName();
                String perHead1= dataList.get(i).get(j).getMenu_rate();
                String vrnoa= dataList.get(i).get(j).getVrnoa();


                packageName.setText(name);
                perHead.setText("Per Head : "+perHead1);

                switch (j) {
                    case 0:

                        chicken1Tv.setText(Item_des);
                        break;
                    case 1:

                        chicken2Tv.setText(Item_des);
                        break;
                    case 2:

                        chicken3Tv.setText(Item_des);
                        break;
                    case 3:

                        chicken4Tv.setText(Item_des);
                        break;
                    case 4:

                        chicken5Tv.setText(Item_des);
                        break;
                    case 5:

                        chicken6Tv.setText(Item_des);
                        break;
                    case 6:

                        chicken7Tv.setText(Item_des);
                        break;
                    case 7:

                        chicken8Tv.setText(Item_des);
                        break;
                    case 8:

                        chicken9Tv.setText(Item_des);
                        break;
                    case 9:

                        chicken10Tv.setText(Item_des);
                        break;
                    default:
                        break;

                }




            }

        }



        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteCart();


                int size= dataList.get(position).size();




                try {


                    if (MyApplication.vrnoa_all.equalsIgnoreCase("0") && MyApplication.maxId_global.equalsIgnoreCase("0")) {

                        getDataByVrnoa();


                    }
                }
                catch (Exception e)
                {
                    e.getMessage();
                }

                //addPackageIntoSQlDB(position);


                for (int j = 0; j <size; j++) {

                    String vrnoa= dataList.get(position).get(j).getVrnoa();
                    String itemName= dataList.get(position).get(j).getItem_des();
                    String menuRate= dataList.get(position).get(j).getMenu_rate();
                    String uom= dataList.get(position).get(j).getUom();
                    String itemPackage= dataList.get(position).get(j).getName();
                    String itemId= dataList.get(position).get(j).getItem_id();
                    String itemMenuId= dataList.get(position).get(j).getVrnoa();


                    db.isAddedToCartPackage(itemId,itemName,menuRate,uom,itemPackage,vrnoa,itemMenuId);
                }


                context.startActivity(new Intent(context, ChiniotCartActivity.class));


                ((ChiniotBadgeActivity) context).updateCartCounter();
                ((ChiniotBadgeActivity) context).finish();

            }



        });




        return itemView;
    }



    public void getDataByVrnoa()
    {


     //   progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, MyApplication.URL_VRNOA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                       // progressDialog.dismiss();

                        Log.e("Respone",response);

                        if (response != null) {

                            MyApplication.maxId_global=response;

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // progressDialog.dismiss();

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(context,
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }


                Toast.makeText(context,
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

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        MySingleton.getInstance().addToReqQueue(postRequest);

    }



}
