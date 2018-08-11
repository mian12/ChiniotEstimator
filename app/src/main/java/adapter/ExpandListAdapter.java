package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alnehal.chiniotestimator.R;

import java.util.ArrayList;

import DB.SQLiteDatabaseHelper;
import model.Child;
import model.Group;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> groups;
    public SQLiteDatabaseHelper db;


    public ExpandListAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        db = new SQLiteDatabaseHelper(context);
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Child child = (Child) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_child_item, null);
        }


        TextView name =convertView.findViewById(R.id.child_name);
        TextView rate =convertView.findViewById(R.id.child_rate);
        final Button addedToCartButton=convertView.findViewById(R.id.added);




        final String itemName = child.getName();
        final String itemId = child.getProduct_id();
        final String price =child.getPer_head_rate();



        name.setText(child.getName());


        int  flag=db.itemExitOrNot(itemId);
        if (flag==1)
        {
          addedToCartButton.setText("Remove from cart");
        }
        else
        {
           addedToCartButton.setText("Add To Cart");
        }

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

            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.row_group_item, null);
        }
        TextView tv =convertView.findViewById(R.id.group_name);
        tv.setText(group.getName());
       ImageView indicator=convertView.findViewById(R.id.indicator);


        if ( getChildrenCount( groupPosition ) == 0 ) {
            indicator.setVisibility( View.INVISIBLE );
        } else {
            indicator.setVisibility( View.VISIBLE );
            indicator.setImageResource( isExpanded ? R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp );
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
