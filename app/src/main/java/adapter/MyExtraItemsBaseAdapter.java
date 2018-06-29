package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.alnehal.chiniotestimator.R;
import com.alnehal.chiniotestimator.customFillter.CustomFillter;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import model.ExtraDetailCartModel;

/**
 * Created by Mian Shahbaz Idrees on 26-Mar-18.
 */

public class MyExtraItemsBaseAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<ExtraDetailCartModel> dataList,fillterList;
    public SQLiteDatabaseHelper db;

    CustomFillter filter;

   public MyExtraItemsBaseAdapter(Context context, ArrayList<ExtraDetailCartModel> dataList){

       this.context = context;
       this.dataList = dataList;
       this.fillterList = dataList;
       inflater = LayoutInflater.from(context);
       db = new SQLiteDatabaseHelper(context);

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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.row_alert_extra_items, parent, false);

        TextView name,rate;
        final Button addedToCartButton;

        name = (TextView) view.findViewById(R.id.tvEngName);
        rate = (TextView) view.findViewById(R.id.rate);
        addedToCartButton = (Button) view.findViewById(R.id.added);


        final String itemName = dataList.get(position).getItemName();
        final String itemId = dataList.get(position).getItemId();
        final String price =dataList.get(position).getItemRate();


        int  flag=db.itemExitOrNot(itemId);
        if (flag==1)
        {
            addedToCartButton.setText("Remove from cart");
        }


        name.setText(itemName);
        rate.setText(price);
        addedToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int  flag=db.itemExitOrNot(itemId);
                if (flag==1)
                {
                    db.deleteExtraItem(itemId);
                    addedToCartButton.setText("Add To Cart");
                }
                else
                {
                    db.addedToExtraItems(itemId, itemName, price,"true");
                    addedToCartButton.setText("Remove from cart");
                }






                //  holder.addedToCartButton.setText("Added To Cart");
                // dataList.get(position).setItemAdded("false");
                //  }
            }
        });





        return view;
    }


//    public Filter getFilter() {
//
//        if (filter==null)
//        {
//            filter=new CustomFillter(fillterList,this);
//
//
//        }
//        return filter;
//    }

}
