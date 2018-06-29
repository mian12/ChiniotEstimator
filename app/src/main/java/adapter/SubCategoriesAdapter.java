package adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alnehal.chiniotestimator.CategoriesActivity;
import com.alnehal.chiniotestimator.ItemsActivity;
import com.alnehal.chiniotestimator.R;
import com.alnehal.chiniotestimator.SubCategoriesActivity;

import java.util.ArrayList;

import model.CategoriesModel;
import model.SubCategoriesModel;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.MyViewHolder> {

    private ArrayList<SubCategoriesModel> subCategoriesModelArrayList;
    private Context context;

    private void showQuantityPickerDialog() {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvSr;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvSr = (TextView) view.findViewById(R.id.tvSr);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    public SubCategoriesAdapter(Context context, ArrayList<SubCategoriesModel> subCategoriesModelArrayList) {
        this.context = context;
        this.subCategoriesModelArrayList = subCategoriesModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_subcategories, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SubCategoriesModel subCategoriesModel = subCategoriesModelArrayList.get(position);
        holder.tvSr.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(Html.fromHtml(subCategoriesModel.getName()));

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putInt("subCatID", subCategoriesModel.getSubcatid());
                extras.putInt("catID", ((SubCategoriesActivity) context).getIntent().getIntExtra("catID", 1));
                ((SubCategoriesActivity) context).gotoActivity(ItemsActivity.class, extras);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoriesModelArrayList.size();
    }
}
