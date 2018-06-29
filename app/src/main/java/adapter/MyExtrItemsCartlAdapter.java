package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alnehal.chiniotestimator.R;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import model.ExtraDetailCartModel;
import utilis.MyApplication;

/**
 * Created by Mian Shahbaz Idrees on 13-Mar-18.
 */

public class MyExtrItemsCartlAdapter extends RecyclerView.Adapter<MyExtrItemsCartlAdapter.MyViewHolder> {


    private LayoutInflater inflater;
    private Context context;
    ArrayList<ExtraDetailCartModel> dataList;
    public SQLiteDatabaseHelper db;

    float TotalAmount = 0;

    TextView perHeadValueTV,extraValueTV,discValueTextView,taxValueTextView,totalValueTextView, netAmountValueTextView;

    EditText editTextVrValue,editTextDiscount, editTextTax,editTextNoofPersons;

    public MyExtrItemsCartlAdapter(Context context, ArrayList<ExtraDetailCartModel> dataList, TextView perHeadValueTV, TextView extraValueTV, TextView discValueTextView,
                                   TextView taxValueTextView, TextView totalValueTextView, TextView netAmountValueTextView,
                                   EditText editTextVrValue, EditText editTextDiscount, EditText editTextTax, EditText editTextNoofPersons) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
        db = new SQLiteDatabaseHelper(context);

        this.perHeadValueTV=perHeadValueTV;
        this.extraValueTV=extraValueTV;
        this.discValueTextView=discValueTextView;
        this.taxValueTextView=taxValueTextView;
        this.totalValueTextView=totalValueTextView;
        this.netAmountValueTextView=netAmountValueTextView;

        this.editTextVrValue=editTextVrValue;
        this.editTextDiscount=editTextDiscount;
        this.editTextTax=editTextTax;
        this.editTextNoofPersons=editTextNoofPersons;



    }


    @Override
    public MyExtrItemsCartlAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_cart_extra_detail, parent, false);
        MyExtrItemsCartlAdapter.MyViewHolder holder = new MyExtrItemsCartlAdapter.MyViewHolder(view);
        return holder;


    }


    @Override
    public void onBindViewHolder(MyExtrItemsCartlAdapter.MyViewHolder holder, final int position) {


        int counter=1;
        counter+=position;

        String itemName = dataList.get(position).getItemName();
        final String itemId = dataList.get(position).getItemId();
        String price =dataList.get(position).getItemRate();




        holder.sr.setText(counter+"");
        holder.itemName.setText(itemName);
        holder.rate.setText(price);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteExtraItem(itemId);

                dataList.remove(position);



                notifyDataSetChanged();

                finalAmountCalculator();
            }

        });



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView sr, itemName,rate;
        ImageView delete;


        public MyViewHolder(View itemView) {
            super(itemView);
            sr = (TextView) itemView.findViewById(R.id.tvSr);
            itemName = (TextView) itemView.findViewById(R.id.tvItemName);
            rate = (TextView) itemView.findViewById(R.id.tvRate);
            delete=  (ImageView) itemView.findViewById(R.id.delete);


        }
    }






    public float percentCalculator(float perecnt)    {
        TotalAmount = MyApplication.totalAmount;
        float res = (TotalAmount / 100) * perecnt;
        return res;
    }

    public    void finalAmountCalculator() {
        try
        {



            float  extraAmount=0;

                ArrayList<ExtraDetailCartModel> listExtraItems=db.getExtraItemsCartVales();

                for (int i=0; i<listExtraItems.size(); i++)
                {

                    extraAmount+=Float.parseFloat(listExtraItems.get(i).getItemRate());

                }

                extraValueTV.setText(extraAmount+"");


            float perHead = validaterInteger(perHeadValueTV.getText().toString());
            float extra = validaterInteger(extraValueTV.getText().toString());


            MyApplication.totalAmount=perHead+extra;

            TotalAmount =MyApplication.totalAmount;

            float discountPercent = validaterInteger(editTextDiscount.getText().toString());

            if (discountPercent!= 0) {
                float discountedRupess = 0;
                discountedRupess = percentCalculator(discountPercent);
                discValueTextView.setText(Math.round(discountedRupess) + "");
            }else {
                discValueTextView.setText("0");
            }




            float taxPercent = validaterInteger(editTextTax.getText().toString());

            if (taxPercent!= 0) {
                float taxRupess = 0;
                taxRupess = percentCalculator(taxPercent);
                taxValueTextView.setText(Math.round(taxRupess)+ "");
            }else {
                taxValueTextView.setText("0");
            }





            float discAmount = validaterInteger(discValueTextView.getText().toString());
            float taxAmount = validaterInteger(taxValueTextView.getText().toString());
            float noOfPersons = validaterInteger(editTextNoofPersons.getText().toString());

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
            Log.d("Exception",ex.getMessage() );
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



}
