package adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import app.AppConfig;
import model.ItemModel;

import com.alnehal.chiniotestimator.CartActivity;
import com.alnehal.chiniotestimator.ParentActivity;
import com.alnehal.chiniotestimator.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private ArrayList<ItemModel> itemModelArrayList;
    private Context context;
    SQLiteDatabaseHelper databaseHelper;
    public boolean isEstimated;
    public boolean isFetchedItem;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSr, tvEngName, tvUOM, tvCategory, tvQty, tvRate, tvMRate, tvAmount, tvPer, tvWeight;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvSr = (TextView) view.findViewById(R.id.tvSr);
            tvEngName = (TextView) view.findViewById(R.id.tvEngName);
            tvUOM = (TextView) view.findViewById(R.id.tvUOM);
            tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            tvQty = (TextView) view.findViewById(R.id.tvQty);
            tvRate = (TextView) view.findViewById(R.id.tvRate);
            tvMRate = (TextView) view.findViewById(R.id.tvMRate);
            tvAmount = (TextView) view.findViewById(R.id.tvAmount);
            tvPer = (TextView) view.findViewById(R.id.tvPer);
            tvWeight = (TextView) view.findViewById(R.id.tvWeight);
        }
    }

    public CartAdapter(Context context, ArrayList<ItemModel> itemModelArrayList, SQLiteDatabaseHelper databaseHelper) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_cart, parent, false));


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemModel itemModel = itemModelArrayList.get(position);
        holder.tvSr.setText("" + (position + 1));
        holder.tvEngName.setText(Html.fromHtml(itemModel.getName()));
        holder.tvUOM.setText(Html.fromHtml(itemModel.getUom()));
        holder.tvCategory.setText(itemModel.getCategory_name());
        holder.tvQty.setText(itemModel.getItemCartQty() + "");
        holder.tvRate.setText(itemModel.getRate_expt_meat() + "");
        holder.tvMRate.setText(itemModel.getMrate() + "");
        holder.tvPer.setText(itemModel.getPer_person() + "");
        if (isEstimated || isFetchedItem) {
            holder.tvAmount.setText(itemModel.getAmountTotal() + "");
            holder.tvWeight.setText(itemModel.getWeight_meat() * itemModel.getItemCartQty() + "");
        } else {
            double amount = ((itemModel.getRate_expt_meat() * itemModel.getItemCartQty()) + (itemModel.getItemCartQty() * itemModel.getMrate() * itemModel.getWeight_meat()));
            holder.tvAmount.setText(amount + "");
            itemModel.setAmountTotal(amount);
            holder.tvWeight.setText(itemModel.getWeight_meat() * itemModel.getItemCartQty() + "");
        }

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_cart_items_options);

                ListView lvOptions = (ListView) dialog.findViewById(R.id.lvOptions);
                String[] options = {"Edit", "Delete"};
                ArrayAdapter optionsAdapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line,
                        options);
                lvOptions.setAdapter(optionsAdapter);
                lvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int optionPosition, long id) {
                        switch (optionPosition) {
                            case 0:
                                final Dialog editDialog = new Dialog(context);
                                editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                editDialog.setCancelable(true);
                                editDialog.setContentView(R.layout.dialog_cart_items_edit);
                                editDialog.setTitle("Enter Quantity!");
                                final EditText etQty = (EditText) editDialog.findViewById(R.id.etQty);
                                Button btnCancel = (Button) editDialog.findViewById(R.id.btnCancel);
                                Button btnDone = (Button) editDialog.findViewById(R.id.btnDone);
                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((CartActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                        editDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                });
                                btnDone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (etQty.getText().toString().trim().length() < 0) {
                                            showToast("Enter Quantity!", 1, Gravity.CENTER);
                                            return;
                                        }
                                        double editedQty = Double.parseDouble(etQty.getText().toString().trim());
                                        if (editedQty < 0) {
                                            showToast("Enter Quantity!", 1, Gravity.CENTER);
                                            return;
                                        }
                                        if (isFetchedItem) {
                                            itemModel.setItemCartQty(editedQty);
                                            double amount = ((itemModel.getRate_expt_meat() * itemModel.getItemCartQty()) + (itemModel.getItemCartQty() * itemModel.getMrate() * itemModel.getWeight_meat()));
                                            itemModel.setAmountTotal(amount);
/*
                                            try {
                                                itemModel.setWeight_meat(databaseHelper.getProductDetail(itemModel.getProduct_id()).getWeight_meat());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
*/

                                            /*if (isEstimated) {
                                                ((CartActivity) context).handleGetEstimateRequestResponse(((CartActivity) context).estimatedResponse);
                                            } else {
                                                ((CartActivity) context).updateTotal();
                                                notifyDataSetChanged();
                                            }*/
                                            ((CartActivity) context).updateTotal();
                                            notifyDataSetChanged();
                                        } else {
                                            databaseHelper.insertCartCounter(itemModel.getProduct_id(), editedQty);
                                            AppConfig.cartUpdated = true;
                                            itemModelArrayList.clear();
                                            itemModelArrayList.addAll(databaseHelper.getCartProducts());

                                            // isEstimated = false;
                                          /*  if (isEstimated) {
                                                ((CartActivity) context).handleGetEstimateRequestResponse(((CartActivity) context).estimatedResponse);
                                            } else {
                                                ((CartActivity) context).updateTotal();
                                                notifyDataSetChanged();
                                            }*/

                                            isEstimated = false;
                                            notifyDataSetChanged();
                                            ((CartActivity) context).updateTotal();

                                        }
                                        ((CartActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                        editDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                });
                                editDialog.show();
                                break;
                            case 1:
                                if (isFetchedItem) {
                                    itemModelArrayList.remove(itemModel);
                                    ((CartActivity) context).updateTotal();
                                    notifyDataSetChanged();
                                    if (itemModelArrayList.size() < 1) {
                                        ((CartActivity) context).setTvNotFoundVisibility(true);
                                    }
                                } else {
                                    databaseHelper.insertCartCounter(itemModel.getProduct_id(), 0);
                                    itemModelArrayList.remove(position);
                                    AppConfig.cartUpdated = true;
                                    notifyDataSetChanged();
                                    if (itemModelArrayList.size() < 1) {
                                        ((CartActivity) context).setTvNotFoundVisibility(true);
                                    }
                                    ((CartActivity) context).updateTotal();
                                }
                                dialog.dismiss();

                                break;
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public void showToast(String message, int length, int gravity) {
        Toast toast = Toast.makeText(context, message, length);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }
}
