package com.alnehal.chiniotestimator.customFillter;

import android.widget.Filter;

import java.util.ArrayList;

import adapter.MyExtraItemsAdapter;

import adapter.MyExtraItemsBaseAdapter;
import model.ExtraDetailCartModel;

/**
 * Created by Engr Shahbaz Idrees on 8/10/2016.
 */

public class CustomFillter extends Filter {
    //MyExtraItemsBaseAdapter adapter;
    MyExtraItemsAdapter adapter;

    ArrayList<ExtraDetailCartModel> filterList;

    public CustomFillter(  ArrayList<ExtraDetailCartModel> filterList, MyExtraItemsAdapter adapter)
    {

        this.adapter=adapter;
        this.filterList=filterList;



    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results=new FilterResults();

        if (constraint!=null && constraint.length()>0)
        {

            constraint=constraint.toString().toUpperCase();

            ArrayList<ExtraDetailCartModel> fillteredExtraItems=new ArrayList<>();

                for (int i=0; i<filterList.size(); i++)

                {
                    if(filterList.get(i).getItemName().contains(constraint))
                    {
                        fillteredExtraItems.add(filterList.get(i));
                    }
                }

            results.count=fillteredExtraItems.size();
            results.values=fillteredExtraItems;


        }

        else
        {

            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.dataList=(ArrayList<ExtraDetailCartModel>)filterResults.values;
        adapter.notifyDataSetChanged();

    }
}
