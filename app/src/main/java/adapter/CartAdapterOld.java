package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alnehal.chiniotestimator.CartActivity;
import com.alnehal.chiniotestimator.R;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import model.ItemModel;

public class CartAdapterOld extends RecyclerView.Adapter<CartAdapterOld.MyViewHolder> {

    private ArrayList<ItemModel> itemModelArrayList;
    private Context context;
    SQLiteDatabaseHelper databaseHelper;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSr, tvUrduName,tvTitle, tvProductPrice, tvProductTotal, tvQty;
        ImageView ivIcon;
        Button btnAdd, btnLess;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvSr = (TextView) view.findViewById(R.id.tvSr);
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvUrduName = (TextView) view.findViewById(R.id.tvUrduName);
            tvProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);
            tvQty = (TextView) view.findViewById(R.id.tvQty);
            tvProductTotal = (TextView) view.findViewById(R.id.tvProductTotal);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            btnLess = (Button) view.findViewById(R.id.btnLess);
        }
    }

    public CartAdapterOld(Context context, ArrayList<ItemModel> itemModelArrayList, SQLiteDatabaseHelper databaseHelper) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_cart_old, parent, false));


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemModel itemModel = itemModelArrayList.get(position);
        holder.tvSr.setText("" + (position + 1));
        holder.tvTitle.setText(Html.fromHtml(itemModel.getName()));
        holder.tvUrduName.setText(Html.fromHtml(itemModel.getUrdu_name()));
        holder.tvProductPrice.setText(itemModel.getRate_expt_meat() + " X ");
        holder.tvQty.setText("" + itemModel.getItemCartQty());
        holder.tvProductTotal.setText(" = " + (itemModel.getItemCartQty() * itemModel.getRate_expt_meat())
                + context.getResources().getString(R.string._pkr));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double counter = Double.parseDouble(holder.tvQty.getText().toString());
                holder.tvQty.setText(String.valueOf(counter + 1));
                databaseHelper.insertCartCounter(itemModel.getProduct_id(), (int) counter + 1);
                itemModel.setItemCartQty(counter + 1);
                holder.tvProductTotal.setText(" = " + (itemModel.getItemCartQty() * itemModel.getRate_expt_meat())
                        + context.getResources().getString(R.string._pkr));
                ((CartActivity) context).updateTotal();
            }
        });

        holder.btnLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double counter = Double.parseDouble(holder.tvQty.getText().toString());
                if (counter > 1) {
                    holder.tvQty.setText(String.valueOf(counter - 1));
                    databaseHelper.insertCartCounter(itemModel.getProduct_id(), (int) counter - 1);
                    itemModel.setItemCartQty(counter - 1);
                    holder.tvProductTotal.setText(" = " + (itemModel.getItemCartQty() * itemModel.getRate_expt_meat())
                            + context.getResources().getString(R.string._pkr));
                    ((CartActivity) context).updateTotal();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }
}