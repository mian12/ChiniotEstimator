package adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alnehal.chiniotestimator.R;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import model.CartModel;

/**
 * Created by Mian Shahbaz Idrees on 12-Mar-18.
 */

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {


    private LayoutInflater inflater;
    private Context context;
    ArrayList<CartModel> dataList;
    public SQLiteDatabaseHelper db;

    public MyCartAdapter(Context context, ArrayList<CartModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
        db = new SQLiteDatabaseHelper(context);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_cart, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;


    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        int counter=1;
         counter+=position;

                String itemName = dataList.get(position).getItemName();
                String uom = dataList.get(position).getUom();
                String price =dataList.get(position).getMenuRate();
                String vrnoa = dataList.get(position).getVrnoa();


        holder.sr.setText(counter+"");

                holder.itemName.setText(itemName);







    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView sr, itemName;


        public MyViewHolder(View itemView) {
            super(itemView);
            sr = (TextView) itemView.findViewById(R.id.tvSr);
            itemName = (TextView) itemView.findViewById(R.id.tvItemName);




        }
    }


}
