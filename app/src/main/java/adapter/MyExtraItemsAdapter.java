package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.alnehal.chiniotestimator.R;
import com.alnehal.chiniotestimator.customFillter.CustomFillter;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import model.ExtraDetailCartModel;

/**
 * Created by Mian Shahbaz Idrees on 14-Mar-18.
 */

public class MyExtraItemsAdapter extends RecyclerView.Adapter<MyExtraItemsAdapter.MyViewHolder> {


    private LayoutInflater inflater;
    private Context context;
   public ArrayList<ExtraDetailCartModel> dataList,fillterList;
    public SQLiteDatabaseHelper db;

    CustomFillter filter;

    public MyExtraItemsAdapter(Context context, ArrayList<ExtraDetailCartModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.fillterList = dataList;
        inflater = LayoutInflater.from(context);
        db = new SQLiteDatabaseHelper(context);

    }


    @Override
    public MyExtraItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_alert_extra_items, parent, false);
        MyExtraItemsAdapter.MyViewHolder holder = new MyExtraItemsAdapter.MyViewHolder(view);
        return holder;


    }


    @Override
    public void onBindViewHolder(final MyExtraItemsAdapter.MyViewHolder holder, final int position) {


        final String itemName = dataList.get(position).getItemName();
        final String itemId = dataList.get(position).getItemId();
        final String price =dataList.get(position).getItemRate();


        int  flag=db.itemExitOrNot(itemId);
        if (flag==1)
        {
            holder.addedToCartButton.setText("Remove from cart");
        }
        else
        {
            holder.addedToCartButton.setText("Add To Cart");
        }


        holder.itemName.setText(itemName);
        holder.rate.setText(price);
        holder.addedToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int  flag=db.itemExitOrNot(itemId);
                if (flag==1)
                {
                    db.deleteExtraItem(itemId);
                    holder.addedToCartButton.setText("Add To Cart");
                }
                else
                {
                    db.addedToExtraItems(itemId, itemName, price,"true");
                    holder.addedToCartButton.setText("Remove from cart");
                }






                 //  holder.addedToCartButton.setText("Added To Cart");
                   // dataList.get(position).setItemAdded("false");
              //  }
            }
        });







    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public Filter getFilter() {

        if (filter==null)
        {
            filter=new CustomFillter(fillterList,this);


        }
        return filter;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView  itemName,rate;
        Button addedToCartButton;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.tvEngName);
            rate = (TextView) itemView.findViewById(R.id.rate);
            addedToCartButton = (Button) itemView.findViewById(R.id.added);



        }
    }


}
