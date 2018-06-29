package adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alnehal.chiniotestimator.ParentActivity;
import com.alnehal.chiniotestimator.R;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import app.AppConfig;
import model.ItemModel;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private ArrayList<ItemModel> itemModelArrayList;
    private Context context;
    private ListPopupWindow popup;
    private SQLiteDatabaseHelper databaseHelper;
    private boolean popupShowing;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEngName, tvUrduName, tvUOM, tvPerPerson, tvMeatWeight;
        Button btnAddToCart;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvEngName = (TextView) view.findViewById(R.id.tvEngName);
           /* tvUrduName = (TextView) view.findViewById(R.id.tvUrduName);
            tvUOM = (TextView) view.findViewById(R.id.tvUOM);
            tvPerPerson = (TextView) view.findViewById(R.id.tvPerPerson);
            tvMeatWeight = (TextView) view.findViewById(R.id.tvMeatWeight);*/
            btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);
        }
    }

    public ItemsAdapter(Context context, ArrayList<ItemModel> itemModelArrayList, SQLiteDatabaseHelper databaseHelper) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_items, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemModel itemModel = itemModelArrayList.get(position);
        holder.tvEngName.setText(Html.fromHtml(itemModel.getName()) + "");
        /*holder.tvUrduName.setText(Html.fromHtml(itemModel.getUrdu_name()) + "");
        holder.tvUOM.setText(Html.fromHtml(itemModel.getUom()) + " (" + Html.fromHtml(itemModel.getUrdu_uom()) + ")");
        holder.tvPerPerson.setText(itemModel.getPer_person() + "");
        holder.tvMeatWeight.setText(itemModel.getWeight_meat() + "");
*/
        if (itemModel.getIsAddedToCart() == 0) {
            holder.btnAddToCart.setText(R.string.add_to_cart);
        } else {
            holder.btnAddToCart.setText(R.string.remove_from_cart);
        }

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemModel.getIsAddedToCart() == 1) {
                    databaseHelper.insertCartCounter(itemModel.getProduct_id(), 0);
                    holder.btnAddToCart.setText(R.string.add_to_cart);
                    itemModel.setIsAddedToCart(0);
                    AppConfig.cartUpdated = true;
                    ((ParentActivity) context).updateCartCounter();
                } else if (popup != null && popupShowing) {
                    popup.dismiss();
                    popupShowing = false;
                } else {
                    databaseHelper.insertCartCounter(itemModel.getProduct_id(), 1);
                    holder.btnAddToCart.setText(R.string.remove_from_cart);
                    itemModel.setIsAddedToCart(1);
                    AppConfig.cartUpdated = true;
                    ((ParentActivity) context).updateCartCounter();
                }
//                    showListPopup(context, v, holder, position, itemModel);
            }
        });

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popup != null && popupShowing) {
                    popup.dismiss();
                    popupShowing = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public void showListPopup(final Context context, View anchor, final MyViewHolder holder, int productPosition, final ItemModel itemModel) {

        if (popup == null) {
            popup = new ListPopupWindow(context);
            ArrayList<Integer> arrayList = new ArrayList();
            for (int i = 1; i < 11; i++) {
                arrayList.add(i);
            }
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayList);
            popup.setAdapter(adapter);
        }

        popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                databaseHelper.insertCartCounter(itemModel.getProduct_id(), position + 1);
                holder.btnAddToCart.setText(R.string.remove_from_cart);
                itemModel.setIsAddedToCart(1);
                AppConfig.cartUpdated = true;
                ((ParentActivity) context).updateCartCounter();
                popup.dismiss();
                popupShowing = false;
            }
        });
        popup.setAnchorView(anchor);
        popupShowing = true;
        popup.show();
    }
}
