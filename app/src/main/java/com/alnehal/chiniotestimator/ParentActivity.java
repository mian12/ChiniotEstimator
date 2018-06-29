package com.alnehal.chiniotestimator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DB.SQLiteDatabaseHelper;
import Web.WebRequestHandlerInstance;
import adapter.CartAdapter;
import adapter.CategoriesAdapter;
import adapter.ItemsAdapter;
import adapter.SubCategoriesAdapter;
import app.AppConfig;
import cz.msebera.android.httpclient.Header;
import model.CategoriesModel;
import model.ItemModel;
import model.SubCategoriesModel;
import utilis.MyApplication;
import utilis.SharedPreferenceClass;

public class ParentActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnLogIn;
    public EditText etUserName, etPersons, etIncDecPercent;
    RadioGroup rgIncDecPer;
    RadioButton rbNoIncDec, rbIncPer, rbDecPer;
    public EditText etPassword;
    public TextInputLayout tilName, tilPassword;
    public int REQUEST_TYPE = -1;
    public String URL_SUFFIX = "";
    public boolean IS_REQUEST_IN_PROCESS = false;
    public RecyclerView rvItems, rvCategories, rvSubCategories;
    public Context context;
    public ItemsAdapter itemsAdapter;
    public ArrayList<ItemModel> itemModelArrayList;
    public ArrayList<CategoriesModel> categoriesModelArrayList;
    public ArrayList<SubCategoriesModel> subCategoriesModelArrayList;
    public ProgressDialog dialog;
    public SQLiteDatabaseHelper databaseHelper;
    public CategoriesAdapter categoriesAdapter;
    public SubCategoriesAdapter subCategoriesAdapter;
    public Bundle extras;
    RecyclerView rvCart;
    MenuItem additem;
    CartAdapter cartAdapter;
    RelativeLayout rlTotal;
    LinearLayout llTotal;
    Button btnGetEstimate, btnPrint, btnClearCart;
    TextView tvNotFound, tvTotalPrice;
    MenuItem item;
    View view;
    BadgeStyle badgeStyle;
    ActionItemBadge.ActionItemBadgeListener listener;
    SearchView searchView;
    int numberOfPersons = 1;
    boolean shouldIncDec = false;
    double incDecPer = 0;
    ArrayList<Integer> cartProductIDsArrayList;
    public TextView tvTotalSr, tvTotalQty, tvTotalAmount, tvTotalWeight;
    double grandTotal = 0;
    Button btnSave, btnSearch;
    public JSONArray estimatedResponse;
    TextView tvVRNOA;

    //filter variables

    private ItemsFilterAdapter itemsFilterAdapter;
    ArrayList<ItemModel> itemsListBeforeFilter;
    ArrayList<ItemModel> itemsListAfterFilter;

    public SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout llOne, llTwo, llThree, llFour, llFive, llSix, llSeven, llEight, llNine;
    LinearLayout[] linearLayouts = {llOne, llTwo, llThree, llFour, llFive, llSix, llSeven, llEight, llNine};
    int[] linearLayoutsIds = {R.id.llOne, R.id.llTwo, R.id.llThree, R.id.llFour, R.id.llFive, R.id.llSix, R.id.llSeven, R.id.llEight, R.id.llNine};


    RelativeLayout rlOne, rlTwo, rlThree, rlFour, rlFive, rlSix, rlSeven, rlEight, rlNine, rlTen, rlEleven, rlTwelve,
            rlThirteen, rlFourteen, rlFifteen, rlSixteen, rlSeventeen, rlEighteen, rlNinteen, rlTwenty, rlTwentyOne,
            rlTwentyTwo, rlTwentyThree, rlTwentyFour, rlTwentyFive;


    RelativeLayout[] realtiveLayout = {rlOne, rlTwo, rlThree, rlFour, rlFive, rlSix, rlSeven, rlEight, rlNine, rlTen, rlEleven, rlTwelve, rlThirteen, rlFourteen, rlFifteen, rlSixteen, rlSeventeen, rlEighteen, rlNinteen, rlTwenty,
            rlTwentyOne, rlTwentyTwo, rlTwentyThree, rlTwentyFour, rlTwentyFive};
    int[] realtiveLayoutIds = {R.id.rlOne, R.id.rlTwo, R.id.rlThree, R.id.rlFour, R.id.rlFive, R.id.rlSix, R.id.rlSeven, R.id.rlEight, R.id.rlNine, R.id.rlTen, R.id.rlEleven, R.id.rlTwelve, R.id.rlThirteen, R.id.rlFourteen,
            R.id.rlFifteen, R.id.rlSixteen, R.id.rlSeventeen, R.id.rlEighteen, R.id.rlNinteen, R.id.rlTwenty, R.id.rlTwentyOne, R.id.rlTwentyTwo, R.id.rlTwentyThree, R.id.rlTwentyFour, R.id.rlTwentyFive};


    ImageView ivOne, ivTwo, ivThree, ivFour, ivFive, ivSix, ivSeven, ivEight, ivNine, ivTen, ivEleven, ivTwelve, ivThieteen, ivFourteen, ivFifteen, ivSixteen, ivSeventeen, ivEighteen, ivNinteen, ivTwenty, ivTwentyOne, ivTwentyTwo, ivTwentyThree, ivTwentyFour, ivTwentyFive;

    ImageView imageView[] = {ivOne, ivTwo, ivThree, ivFour, ivFive, ivSix, ivSeven, ivEight, ivNine, ivTen, ivEleven, ivTwelve, ivThieteen, ivFourteen, ivFifteen, ivSixteen, ivSeventeen, ivEighteen, ivNinteen, ivTwenty, ivTwentyOne, ivTwentyTwo, ivTwentyThree, ivTwentyFour, ivTwentyFive};
    int[] imageViewIds = {R.id.ivOne, R.id.ivTwo, R.id.ivThree, R.id.ivFour, R.id.ivFive, R.id.ivSix, R.id.ivSeven, R.id.ivEight, R.id.ivNine, R.id.ivTen,
            R.id.ivEleven, R.id.ivTwelve, R.id.ivThirteen, R.id.ivFourteen, R.id.ivFifteen, R.id.ivSixteen, R.id.ivSeventeen, R.id.ivEighteen, R.id.ivNinteen, R.id.ivTwenty,
            R.id.ivTwentyOne, R.id.ivTwentyTwo, R.id.ivTwentyThree, R.id.ivTwentyFour, R.id.ivTwentyFive};


    TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvTen, tvEleven, tvTwelve, tvThirteen, tvFourteen, tvFifteen, tvSixteen, tvSeventeen, tvEighteen, tvNinteen, tvTwenty, tvTwentyOne, tvTwentyTwo, tvTwentyThree, tvTwentyFour, tvTwentyFive;
    TextView textView[] = {tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvTen, tvEleven, tvTwelve, tvThirteen, tvFourteen, tvFifteen, tvSixteen, tvSeventeen, tvEighteen, tvNinteen, tvTwenty, tvTwentyOne, tvTwentyTwo, tvTwentyThree, tvTwentyFour, tvTwentyFive};
    int[] textViewIds = {R.id.tvOne, R.id.tvTwo, R.id.tvThree, R.id.tvFour, R.id.tvFive, R.id.tvSix, R.id.tvSeven, R.id.tvEight, R.id.tvNine, R.id.tvTen,
            R.id.tvEleven, R.id.tvTwelve, R.id.tvThirteen, R.id.tvFourteen, R.id.tvFifteen, R.id.tvSixteen, R.id.tvSeventeen, R.id.tvEighteen, R.id.tvNinteen, R.id.tvTwenty,
            R.id.tvTwentyOne, R.id.tvTwentyTwo, R.id.tvTwentyThree, R.id.tvTwentyFour, R.id.tvTwentyFive};




    String party_uname = "", party_mobile = "", party_city = "", party_address = "", party_limit = "0.0", party_cityarea = "",
            date_time = "", party_name = "", oid = "0", vrno = "0", vrnoa = "0", vrdate = "", etype = "estimate", party_id = "1", order_date = "",
            persons = "1", per_head = "0", total_amount = "0", advance = "0", balance = "0", datetime = "", remarks = "", status1 = "",
            phone = "", uid = "1", ph = "", total_weight = "";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void showProgressBar(String message) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.setCancelable(false);
            dialog.setMessage(message);
            dialog.show();
        }
    }

    public void hideProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean validationForUserName() {
        if (getEditTextValue(etUserName).isEmpty()) {
            etUserName.setError("Enter User Name");
            requestFocus(etUserName);
            return false;
        } else {
            etUserName.setError(null);
        }
        return true;
    }

    public boolean validationForPassword() {
        if (getEditTextValue(etPassword).isEmpty()) {
            etPassword.setError("Enter Password");
            requestFocus(etPassword);
            return false;
        } else {
            etPassword.setError(null);
        }
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Parent Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        if (context instanceof CategoriesActivity || context instanceof SubCategoriesActivity || context instanceof CategoriesGridMenu
                || context instanceof ItemsActivity) {
            // Inflate the menu; this adds items to the action bar if it is present.
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main_menu, menu);

            item = menu.findItem(R.id.action_cart);


            badgeStyle = ActionItemBadge.BadgeStyles.GREY.getStyle();
            badgeStyle.setTextColor(Color.WHITE);
            listener = new ActionItemBadge.ActionItemBadgeListener() {
                @Override
                public boolean onOptionsItemSelected(MenuItem menu) {
                    Intent cartIntent = new Intent(context, CartActivity.class);
                    cartIntent.putExtra("editAble", false);
                    startActivity(cartIntent);
                    return false;
                }
            };

            ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, 0);


            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final MenuItem searchItem = menu.findItem(R.id.search);
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean queryTextFocused) {
                    if (!queryTextFocused) {
                        searchItem.collapseActionView();
                        searchView.setQuery("", false);
                    }
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    public void showItemSearchDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_item);

        EditText etSearchItem = (EditText) dialog.findViewById(R.id.etSearchItem);
        ListView lvItems = (ListView) dialog.findViewById(R.id.lvItems);
        itemsListBeforeFilter = databaseHelper.getAllProducts();
        itemsListAfterFilter = itemsListBeforeFilter;
        itemsFilterAdapter = new ItemsFilterAdapter(this);
        lvItems.setAdapter(itemsFilterAdapter);
        etSearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemsFilterAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemModel itemModel = itemsListAfterFilter.get(position);

                if (cartAdapter.isFetchedItem) {
                    itemModel.setItemCartQty(1);
                    double amount = ((itemModel.getRate_expt_meat() * itemModel.getItemCartQty()) + (itemModel.getItemCartQty() * itemModel.getMrate() * itemModel.getWeight_meat()));
                    itemModel.setAmountTotal(amount);
                    itemModelArrayList.add(itemModel);
                    updateTotal();
                    cartAdapter.notifyDataSetChanged();
                } else {
                    databaseHelper.insertCartCounter(itemModel.getProduct_id(), 1);
                    AppConfig.cartUpdated = true;
                    itemModelArrayList.clear();
                    itemModelArrayList.addAll(databaseHelper.getCartProducts());
                    cartAdapter.isEstimated = false;
                    if (itemModelArrayList.size() > 0) {
                        setTvNotFoundVisibility(false);
                    } else {
                        setTvNotFoundVisibility(true);
                    }
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class ItemsFilterAdapter extends BaseAdapter implements Filterable {

        private LayoutInflater mInflater;

        private ItemsFilter itemsFilter;

        public ItemsFilterAdapter(Context context) {
            mInflater = LayoutInflater.from(context);

            getFilter();
        }

        //How many items are in the data set represented by this Adapter.
        @Override
        public int getCount() {

            return itemsListAfterFilter.size();
        }

        //Get the data item associated with the specified position in the data set.
        @Override
        public Object getItem(int position) {

            return itemsListAfterFilter.get(position);
        }

        //Get the row id associated with the specified position in the list.
        @Override
        public long getItemId(int position) {

            return position;
        }

        //Get a View that displays the data at the specified position in the data set.
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder viewHolder;

            if (convertView == null) {

                viewHolder = new Holder();

                convertView = mInflater.inflate(R.layout.lv_items_filter, null);

                viewHolder.tvEngName = (TextView) convertView.findViewById(R.id.tvEngName);

                convertView.setTag(viewHolder);

            } else {

                viewHolder = (Holder) convertView.getTag();
            }

            viewHolder.tvEngName.setText(itemsListAfterFilter.get(position).getName());

            return convertView;
        }

        private class Holder {

            TextView tvEngName;
        }

        //Returns a filter that can be used to constrain data with a filtering pattern.
        @Override
        public Filter getFilter() {

            if (itemsFilter == null) {

                itemsFilter = new ItemsFilter();
            }

            return itemsFilter;
        }


        private class ItemsFilter extends Filter {


            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {

                    ArrayList<ItemModel> filterList = new ArrayList<>();

                    for (int i = 0; i < itemsListBeforeFilter.size(); i++) {

                        if (itemsListBeforeFilter.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filterList.add(itemsListBeforeFilter.get(i));
                        }
                    }


                    results.count = filterList.size();

                    results.values = filterList;

                } else {

                    results.count = itemsListBeforeFilter.size();

                    results.values = itemsListBeforeFilter;

                }

                return results;
            }


            //Invoked in the UI thread to publish the filtering results in the user interface.
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                itemsListAfterFilter = (ArrayList<ItemModel>) results.values;
                notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class CustomTextWatcher implements TextWatcher {

        public View view;

        public CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etUserName:
                    validationForUserName();
                    break;
                case R.id.etPassword:
                    validationForPassword();
                    break;
            }
        }
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public String getEditTextValue(EditText editText) {
        if (editText != null && editText.length() > 0) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(c.getTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogIn:
                hideKeyboard((AppCompatActivity) context);
                if (getEditTextValue(etUserName).isEmpty()) {
                    etUserName.setError("Enter User Name");
                    return;
                }
                if (getEditTextValue(etPassword).isEmpty()) {
                    etPassword.setError("Enter Password");
                    return;
                }
                reqLogin();

                break;
        }
    }

    public void resetVariables() {
        party_uname = "";
        party_mobile = "";
        party_city = "";
        party_address = "";
        party_limit = "0.0";
        party_cityarea = "";
        date_time = "";
        party_name = "";
        oid = "0";
        vrno = "0";
        vrnoa = "0";
        vrdate = "";
        etype = "estimate";
        party_id = "1";
        order_date = "";
        persons = "1";
        per_head = "0";
        total_amount = "0";
        advance = "0";
        balance = "0";
        datetime = "";
        remarks = "";
        status1 = "";
        phone = "";
        uid = "1";
        ph = "";
        total_weight = "";

        reqFetchMaxVRNOA();
        cartAdapter.isEstimated = false;
        cartAdapter.isFetchedItem = false;
    }

    public void gotoActivity(Class mClass, Bundle extras) {
        Intent intent = new Intent(context, mClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }




    public void reqLogin() {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 0;
            URL_SUFFIX = "welcome/has_matchMobile";
            RequestParams params = new RequestParams();
            params.put("uname", etUserName.getText().toString().trim());
            params.put("pass", etPassword.getText().toString().trim());
            WebRequestHandlerInstance.post(URL_SUFFIX, params, new LoopJRequestHandler());
        }
    }

    public void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchView.clearFocus();
            String query = intent.getStringExtra(SearchManager.QUERY);
            showToast(query, Toast.LENGTH_LONG, Gravity.CENTER);
        }
    }

    public void showToast(String message, int length, int gravity) {
        Toast toast = Toast.makeText(context, message, length);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public void fetchAllProductsRequest() {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 1;
            URL_SUFFIX = "product/fetchAll";
            RequestParams params = new RequestParams();
            params.put("active", "1");
            params.put("tabitem", "1");
            WebRequestHandlerInstance.post(URL_SUFFIX, params, new LoopJRequestHandler());
        }
    }

    public void saveVoucherRequest(String estimate, String estimatedetial) {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 6;
            URL_SUFFIX = "estimate/saveapp";
            RequestParams params = new RequestParams();
            params.put("vrnoa", vrnoa);
            if (vrnoa.equalsIgnoreCase("0")) {
                params.put("voucher_type_hidden", "new");
            } else {
                params.put("voucher_type_hidden", "edit");
            }
            params.put("etype", "estimate");
            params.put("estimate", estimate);
            params.put("estimatedetail", estimatedetial);
            WebRequestHandlerInstance.post(URL_SUFFIX, params, new LoopJRequestHandler());
        }
    }

    public void fetchVoucherRequest(int voucherNo) {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 5;
            URL_SUFFIX = "estimate/fetchapp";
            RequestParams params = new RequestParams();
            params.put("vrnoa", voucherNo);
            params.put("etype", "estimate");
            WebRequestHandlerInstance.post(URL_SUFFIX, params, new LoopJRequestHandler());
        }
    }

    public void getEstimate(String detail) {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 4;
            URL_SUFFIX = "estimate/GetEstmate";
            RequestParams params = new RequestParams();
            params.put("detail", detail);
            WebRequestHandlerInstance.post(URL_SUFFIX, params, new LoopJRequestHandler());
        }
    }

    public void fetchAllCategoriesRequest() {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 2;
            URL_SUFFIX = "product/fetchAllCategories";
            WebRequestHandlerInstance.post(URL_SUFFIX, null, new LoopJRequestHandler());
        }
    }

    public void fetchAllSubCategoriesRequest() {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 3;
            URL_SUFFIX = "product/fetchAllSubCategoriesapp";
            WebRequestHandlerInstance.post(URL_SUFFIX, null, new LoopJRequestHandler());
        }
    }

    public void fetchAllSubCategoriesByCategoryRequest(int catID) {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            REQUEST_TYPE = 8;
            RequestParams params = new RequestParams();
            params.put("catid", catID);
            URL_SUFFIX = "product/fetchAllSubCategoriesapp";
            WebRequestHandlerInstance.post(URL_SUFFIX, params, new LoopJRequestHandler());
        }
    }

    public void reqFetchMaxVRNOA() {
        if (IS_REQUEST_IN_PROCESS) {
            showToast("Request Already In Process", Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            RequestParams params = new RequestParams();
            params.put("etype", "estimate");
            REQUEST_TYPE = 7;
            URL_SUFFIX = "estimate/getMaxVrnoaApp";

            WebRequestHandlerInstance.post(URL_SUFFIX, params, new LoopJRequestHandler());
        }
    }

    public class LoopJRequestHandler extends JsonHttpResponseHandler {

        @Override
        public void onStart() {
            super.onStart();
            IS_REQUEST_IN_PROCESS = true;
            if (REQUEST_TYPE == 7) {
                showProgressBar("Fetching Max VRNOA...");
            } else {
                showProgressBar("Loading...");
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            IS_REQUEST_IN_PROCESS = false;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            showToast("Something wrong happened. Try again!", Toast.LENGTH_LONG, Gravity.CENTER);
            hideProgress();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            showToast("timeout", 1, Gravity.CENTER);
            super.onFailure(statusCode, headers, throwable, errorResponse);
            hideProgress();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            showToast("Something wrong happened. Try again!", Toast.LENGTH_LONG, Gravity.CENTER);
            hideProgress();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            if (response != null) {
                switch (REQUEST_TYPE) {
                    case 0:
                        handleLoginResponse(response);
                        break;
                    case 5:
                        handleFetchVoucherRequestResponse(response);
                        break;
                    case 6:
                        responseSaveVoucherRequest(response);
                        break;
                    case 7:
                        responseFetchMaxVRNOA(response);
                        break;
                }
            } else {
                showToast("Something wrong happened. Try again!", Toast.LENGTH_LONG, Gravity.CENTER);
                hideProgress();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            super.onSuccess(statusCode, headers, responseString);
            showToast("Something wrong happened. Try again!", Toast.LENGTH_LONG, Gravity.CENTER);
            hideProgress();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);

            if (response != null) {
                switch (REQUEST_TYPE) {
                    /*case 0:
                        handleLoginResponse(response);
                        break;*/
                    case 1:
                        handleFetchAllItemsRequestResponse(response);
                        break;
                    case 2:
                        handleFetchAllCategoriesRequestResponse(response);
                        break;
                    case 3:
                        handleFetchAllSubCategoriesRequestResponse(response);
                        break;
                    case 4:
                        handleGetEstimateRequestResponse(response);
                        break;
                    case 8:
                        handleFetchAllCategoriesByCategoryRequestResponse(response);
                        break;
                    default:
                        hideProgress();
                        break;
                }
            } else {
                showToast("Something wrong happened. Try Again Later", Toast.LENGTH_LONG, Gravity.CENTER);
                hideProgress();
            }
        }
    }

    private void responseFetchMaxVRNOA(JSONObject response) {
        try {
            tvVRNOA.setText(context.getResources().getString(R.string.voucher_no) + response.getString("vrnoa"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideProgress();
    }

    private void responseSaveVoucherRequest(JSONObject response) {
        if (response.toString().contains("vrnoa") && !response.toString().contains("-1")) {
            showToast("Saved successfully!", 1, Gravity.CENTER);
            if (itemModelArrayList.size() > 0) {
                String IDsString = "";
                for (int i = 0; i < itemModelArrayList.size(); i++) {
                    IDsString = IDsString + itemModelArrayList.get(i).getProduct_id() + ",";
                }
                IDsString = IDsString.substring(0, IDsString.length() - 1);
                databaseHelper.clearCart(IDsString);
                itemModelArrayList.clear();
                AppConfig.cartUpdated = true;
                updateTotal();
                cartAdapter.notifyDataSetChanged();
                setTvNotFoundVisibility(true);
            }
            cartAdapter.isFetchedItem = false;
            cartAdapter.isEstimated = false;
            resetVariables();
        } else {
            showToast("Something wrong happend. Try again!", 1, Gravity.CENTER);
        }
        hideProgress();
    }

    public void setTvNotFoundVisibility(boolean shouldVisible) {
        if (shouldVisible) {
            rvCart.setVisibility(View.GONE);
            //  btnGetEstimate.setVisibility(View.GONE);
//            rlTotal.setVisibility(View.GONE);
            tvNotFound.setVisibility(View.VISIBLE);
        } else {
            rvCart.setVisibility(View.VISIBLE);
            //  btnGetEstimate.setVisibility(View.GONE);
//            rlTotal.setVisibility(View.VISIBLE);
            tvNotFound.setVisibility(View.GONE);
        }

    }

    private void handleFetchVoucherRequestResponse(JSONObject response) {
        if (itemModelArrayList.size() > 0) {
            String IDsString = "";
            for (int i = 0; i < itemModelArrayList.size(); i++) {
                IDsString = IDsString + itemModelArrayList.get(i).getProduct_id() + ",";
            }
            IDsString = IDsString.substring(0, IDsString.length() - 1);
            databaseHelper.clearCart(IDsString);
            AppConfig.cartUpdated = true;
        }
        itemModelArrayList.clear();
        try {
            JSONArray estimateArray = response.getJSONArray("estimate");
            JSONArray estimateDetailArray = response.getJSONArray("estimatedetail");
            JSONObject estimateObject = estimateArray.getJSONObject(0);
            party_uname = estimateObject.getString("party_uname");
            party_mobile = estimateObject.getString("party_mobile");
            party_city = estimateObject.getString("party_city");
            party_address = estimateObject.getString("party_address");
            party_limit = estimateObject.getString("party_limit");
            party_cityarea = estimateObject.getString("party_cityarea");
            date_time = estimateObject.getString("date_time");
            party_name = estimateObject.getString("party_name");
            oid = estimateObject.getString("oid");
            vrno = estimateObject.getString("vrno");
            vrnoa = estimateObject.getString("vrnoa");
            vrdate = estimateObject.getString("vrdate");
            etype = estimateObject.getString("etype");
            party_id = estimateObject.getString("party_id");
            order_date = estimateObject.getString("order_date");
            persons = estimateObject.getString("persons");
            try {
                etPersons.setText(persons);
            } catch (Exception e) {
                e.printStackTrace();
            }
            per_head = estimateObject.getString("per_head");
            total_amount = estimateObject.getString("total_amount");
            advance = estimateObject.getString("advance");
            datetime = estimateObject.getString("datetime");
            remarks = estimateObject.getString("remarks");
            status1 = estimateObject.getString("status1");
            phone = estimateObject.getString("phone");
            uid = estimateObject.getString("uid");
            ph = estimateObject.getString("ph");
            total_weight = estimateObject.getString("total_weight");
            double totalQty = 0;
            for (int i = 0; i < estimateDetailArray.length(); i++) {
                JSONObject estimateDetailObject = estimateDetailArray.getJSONObject(i);
                ItemModel itemModel = new ItemModel();
                itemModel.setProduct_id(estimateDetailObject.getInt("prid"));
                itemModel.setName(estimateDetailObject.getString("products_name"));
                itemModel.setUrdu_name(estimateDetailObject.getString("products_uname"));
                itemModel.setUom(estimateDetailObject.getString("uom"));
                itemModel.setCatid(estimateDetailObject.getInt("catid"));
                itemModel.setCategory_name(estimateDetailObject.getString("categoy_name"));
                itemModel.setServing(estimateDetailObject.getDouble("products_serving"));
                itemModel.setItemCartQty(estimateDetailObject.getDouble("qty"));
                itemModel.setRate_expt_meat(estimateDetailObject.getDouble("rate"));
                itemModel.setMrate(estimateDetailObject.getDouble("mrate"));
                itemModel.setAmountTotal(estimateDetailObject.getDouble("amount"));
                itemModel.setPer_person(estimateDetailObject.getDouble("per"));
                itemModel.setWeight_meat(estimateDetailObject.getDouble("weight"));
//                itemModel.setProduct_id(estimateDetailObject.getString("gweight"));
                itemModel.setDate_time(estimateDetailObject.getString("date_time"));
                itemModel.setUrdu_name(estimateDetailObject.getString("user_name"));
//                itemModel.setAmountTotal(estimateDetailObject.getDouble("total_amount"));
              //  databaseHelper.insertCartCounter(estimateDetailObject.getInt("prid");

                itemModel.setRound(estimateDetailObject.getInt("products_round"));
                itemModel.setUrdu_uom(estimateDetailObject.getString("urdu_uom"));
                totalQty = totalQty + itemModel.getItemCartQty();
                itemModelArrayList.add(itemModel);
/*
                        estimateDetailObject.getDouble("qty"));
*/
            }
            updateTotal();
            cartAdapter.isFetchedItem = true;
            tvVRNOA.setVisibility(View.VISIBLE);
            tvVRNOA.setText(getString(R.string.voucher_no) + " " + vrnoa);


            cartAdapter.notifyDataSetChanged();
            if (itemModelArrayList.size() < 1) {
                setTvNotFoundVisibility(true);
            } else {
                setTvNotFoundVisibility(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideProgress();
    }

    //// here login successful///////////////
    private void handleLoginResponse(JSONObject response) {
        try {
            if (response.getString("id_message").equalsIgnoreCase("false")) {
                showToast("Wrong Login Credentials!", 1, Gravity.CENTER);
            } else {


                String res=response.toString();
                Log.d("res",res);

                SharedPreferenceClass.getInstance(context.getApplicationContext(), "ChiniotEstimater", MODE_PRIVATE);


                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
//
//
//                long CURRENT_TIME_MILLIS = System.currentTimeMillis();
//
//                Date instant = new Date(CURRENT_TIME_MILLIS);
//                SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm" );
//                String time = sdf.format( instant );

                Date  dateee=getCurrentDate();

                SharedPreferenceClass.setSessionFunc("session", "true");
                SharedPreferenceClass.setSessionTimeFunc("time",dateee+"");



                // parse  the  json object for  intilizations  the global varibales  like ,delete,updae,read and print
                parseEstimateVoucherRights(res);


                //gotoActivity(CategoriesGridMenu.class, null);

                gotoActivity(HomeActivity.class, null);

                finish();
            }
            hideProgress();
        } catch (JSONException e) {
            e.printStackTrace();
            hideProgress();
        }
    }

    public  Date  getCurrentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = new Date();

        return  date;

    }

    private void parseEstimateVoucherRights(String res) {


        try {
            JSONObject jsonObject =new JSONObject(res);

           JSONObject id_message_JsonObject= (JSONObject) jsonObject.get("id_message");
            String desc_JsonObject= (String) id_message_JsonObject.get("desc");

            JSONObject jsonObject1 =new JSONObject(desc_JsonObject);


            JSONObject objVoucher= (JSONObject) jsonObject1.get("vouchers");
            JSONObject estimateObj= (JSONObject) objVoucher.get("estimatevoucher");

            MyApplication.insert_user=estimateObj.getString("insert");
            MyApplication.update_user=estimateObj.getString("update");
            MyApplication.delete_user=estimateObj.getString("delete");
            MyApplication.print_user=estimateObj.getString("print");



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void handleGetEstimateRequestResponse(JSONArray response) {
        //showToast(response.toString(), 1, Gravity.CENTER);
        try {
            String nopStr = etPersons.getText().toString().trim();
            if (nopStr.length() > 0) {
                numberOfPersons = Integer.parseInt(nopStr);
                if (numberOfPersons == 0) numberOfPersons = 1;
            } else
                numberOfPersons = 1;

            if (rbDecPer.isChecked() || rbIncPer.isChecked()) {
                shouldIncDec = true;
                String incDecPerStr = etIncDecPercent.getText().toString().trim();
                if (incDecPerStr.length() > 0 && !incDecPerStr.equalsIgnoreCase("."))
                    incDecPer = Double.parseDouble(incDecPerStr);
                else incDecPer = 0;

            } else {
                shouldIncDec = false;
            }
            ItemModel itemModel = null;
            double ratio = 0;
            int product_id = -1;
            ArrayList<ItemModel> estimatedItemsModelArrayList = new ArrayList<>();
            ItemModel estimatedProductModel;

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject ratioObject = response.getJSONObject(i);
                    ratio = ratioObject.getDouble("ratio");
                    product_id = ratioObject.getInt("product_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                double qty = 0;
                itemModel = itemModelArrayList.get(cartProductIDsArrayList.indexOf(product_id));
                double per_person = itemModel.getPer_person();
                double serving = itemModel.getServing();
                if (serving == 0)
                    serving = 1;

                if (ratio > 0) {
                    qty = numberOfPersons * ratio;
                } else {
                    qty = numberOfPersons * per_person;
                }

                if (shouldIncDec && incDecPer > 0) {
                    if (rbIncPer.isChecked()) {
                        qty += ((qty * incDecPer) / 100);
                    } else {
                        qty -= ((qty * incDecPer) / 100);
                    }
                }

                qty = qty / serving;

                if (itemModel.getRound() == 1) {
                    if (qty % 5 <= 2 && qty % 5 > 0) {
                        qty = qty - qty % 5;
                    } else if (qty % 5 >= 3) {
                        qty = qty + (5 - qty % 5);
                    }
                }
                qty = (int) (qty + .5);

                double weight = itemModel.getWeight_meat();
                double mRate = itemModel.getMrate();
                double rate = itemModel.getRate_expt_meat();
                double amountOne = qty * rate;
                double weightTotal = qty * weight;
                double amountTwo = mRate * weightTotal;
                double amountTotal = amountOne + amountTwo;
                grandTotal = grandTotal + amountTotal;

                estimatedProductModel = new ItemModel();
                estimatedProductModel.setName(itemModel.getName());
                estimatedProductModel.setItemCartQty(qty);
                estimatedProductModel.setProduct_id(itemModel.getProduct_id());
                estimatedProductModel.setUom(itemModel.getUom());
                estimatedProductModel.setRate_expt_meat(rate);
                estimatedProductModel.setMrate(mRate);
                estimatedProductModel.setWeight_meat(weight);
                estimatedProductModel.setCategory_name(itemModel.getCategory_name());
                estimatedProductModel.setPer_person(per_person);
                estimatedProductModel.setAmountTotal(amountTotal);
                estimatedItemsModelArrayList.add(estimatedProductModel);
            }
            itemModelArrayList.clear();
            cartAdapter.isEstimated = true;
            estimatedResponse = response;
            itemModelArrayList.addAll(estimatedItemsModelArrayList);
            cartAdapter.notifyDataSetChanged();
            updateTotal();


            // showToast(grandTotal + "", 1, Gravity.CENTER);
            hideProgress();
        } catch (Exception e) {
            e.printStackTrace();
            hideProgress();
        }
    }

    public void updateTotal() {
        double totalQty = 0, totalAmount = 0, totalWeight = 0;
        for (int i = 0; i < itemModelArrayList.size(); i++) {
            ItemModel itemModel = itemModelArrayList.get(i);
            totalQty += itemModel.getItemCartQty();
            totalWeight += itemModel.getWeight_meat() * itemModel.getItemCartQty();
            if (cartAdapter.isEstimated || cartAdapter.isFetchedItem) {
                totalAmount += itemModel.getAmountTotal();
            } else {

                double amount = ((itemModel.getRate_expt_meat() * itemModel.getItemCartQty()) + (itemModel.getItemCartQty() * itemModel.getMrate() * itemModel.getWeight_meat()));
                totalAmount += amount;
            }
        }

        tvTotalPrice.setText(totalAmount + getString(R.string._pkr));
        tvTotalSr.setText("" + itemModelArrayList.size());
        tvTotalQty.setText(totalQty + "");
        tvTotalAmount.setText(totalAmount + "");
        tvTotalWeight.setText(totalWeight + "");
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void handleFetchAllCategoriesRequestResponse(JSONArray response) {
        categoriesModelArrayList = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject each = response.getJSONObject(i);
                CategoriesModel categoriesModel;
                categoriesModel = new CategoriesModel();
                categoriesModel.setCatid(each.getInt("catid"));
                categoriesModel.setName(each.getString("name"));
                categoriesModel.setImgUrl(each.getString("photo"));
                try {
                    categoriesModel.setDescription(each.getString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    categoriesModel.setDescription("");
                }
                try {
                    categoriesModel.setUid(each.getInt("uid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    categoriesModel.setUid(-1);
                }
                categoriesModelArrayList.add(categoriesModel);
                swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (categoriesModelArrayList.size() > 0) {
            new InsertCategoriesAsync().execute();
        } else {
            hideProgress();
            showToast("No data Found", Toast.LENGTH_LONG, Gravity.CENTER);
        }
    }

    private void handleFetchAllCategoriesByCategoryRequestResponse(JSONArray response) {
        subCategoriesModelArrayList = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject each = response.getJSONObject(i);
                SubCategoriesModel subCategoriesModel;
                subCategoriesModel = new SubCategoriesModel();
                subCategoriesModel.setCatid(each.getInt("catid"));
                subCategoriesModel.setName(each.getString("name"));
                subCategoriesModel.setCategory_name(each.getString("category_name"));
                subCategoriesModel.setSubcatid(each.getInt("subcatid"));
                try {
                    subCategoriesModel.setDescription(each.getString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    subCategoriesModel.setDescription("");
                }
                try {
                    subCategoriesModel.setUid(each.getInt("uid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    subCategoriesModel.setUid(-1);
                }
                subCategoriesModelArrayList.add(subCategoriesModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (subCategoriesModelArrayList.size() > 0) {
            subCategoriesAdapter = new SubCategoriesAdapter(context, subCategoriesModelArrayList);
            rvSubCategories.setAdapter(subCategoriesAdapter);
            hideProgress();
        } else {
            hideProgress();
            showToast("No data Found", Toast.LENGTH_LONG, Gravity.CENTER);
        }
    }

    private void handleFetchAllSubCategoriesRequestResponse(JSONArray response) {
        subCategoriesModelArrayList = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject each = response.getJSONObject(i);
                SubCategoriesModel subCategoriesModel;
                subCategoriesModel = new SubCategoriesModel();
                subCategoriesModel.setCatid(each.getInt("catid"));
                subCategoriesModel.setName(each.getString("name"));
                subCategoriesModel.setCategory_name(each.getString("category_name"));
                subCategoriesModel.setSubcatid(each.getInt("subcatid"));
                try {
                    subCategoriesModel.setDescription(each.getString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    subCategoriesModel.setDescription("");
                }
                try {
                    subCategoriesModel.setUid(each.getInt("uid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    subCategoriesModel.setUid(-1);
                }
                subCategoriesModelArrayList.add(subCategoriesModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (subCategoriesModelArrayList.size() > 0) {
            new InsertSubCategoriesAsync().execute();
        } else {
            hideProgress();
            showToast("No data Found", Toast.LENGTH_LONG, Gravity.CENTER);
        }
    }

    private void handleFetchAllItemsRequestResponse(JSONArray response) {
        itemModelArrayList = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject each = response.getJSONObject(i);
                ItemModel itemModel;
                itemModel = new ItemModel();
                itemModel.setProduct_id(each.getInt("product_id"));
                itemModel.setMeat_type(each.getInt("meat_type"));
                itemModel.setDate(each.getString("date"));
                itemModel.setName(each.getString("name"));
                itemModel.setUrdu_name(each.getString("urdu_name"));
                itemModel.setUom(each.getString("uom"));
                try {
                    itemModel.setLabour(each.getString("labour"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setLabour("");
                }
                try {
                    itemModel.setPay_mode(each.getString("pay_mode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setPay_mode("");
                }
                try {
                    itemModel.setParty_id(each.getInt("party_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setParty_id(-1);
                }
                itemModel.setRate_expt_meat(each.getDouble("rate_expt_meat"));
                itemModel.setWeight_meat(each.getDouble("weight_meat"));
                itemModel.setUrdu_uom(each.getString("urdu_uom"));
                try {
                    itemModel.setSpecification(each.getString("specification"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setSpecification("");
                }
                itemModel.setPer_person(each.getDouble("per_person"));
                itemModel.setStatus(each.getInt("status"));
                try {
                    itemModel.setId(each.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setId(-1);
                }
                itemModel.setServing(each.getDouble("serving"));
                itemModel.setRound(each.getInt("round"));
                itemModel.setUid(each.getInt("uid"));
                itemModel.setDate_time(each.getString("date_time"));
                itemModel.setCatid(each.getInt("catid"));
                try {
                    itemModel.setSubcatid(each.getInt("subcatid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setSubcatid(-1);
                }
                try {
                    itemModel.setKitchenid(each.getInt("kitchenid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setKitchenid(-1);
                }
                itemModel.setPhoto(each.getString("photo"));
                itemModel.setCategory_name(each.getString("category_name"));
                try {
                    itemModel.setSubcategory_name(each.getString("subcategory_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    itemModel.setSubcategory_name("");
                }
                itemModel.setKitchen_name(each.getString("kitchen_name"));
                // itemModel.setKitchen_name("");
                itemModel.setMrate(each.getDouble("mrate"));

                itemModelArrayList.add(itemModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (itemModelArrayList.size() > 0) {
            new InsertProductsAsync().execute();
        } else {
            hideProgress();
            showToast("No data Found", Toast.LENGTH_LONG, Gravity.CENTER);
        }
    }

    public class InsertCategoriesAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            databaseHelper.insertCategories(categoriesModelArrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (databaseHelper.getTotalCategories() > 1) {
                /*categoriesModelArrayList = databaseHelper.getCategories();
                categoriesAdapter = new CategoriesAdapter(context, categoriesModelArrayList);
                rvCategories.setAdapter(categoriesAdapter);*/
                int cat_check=categoriesModelArrayList.size();

                for (int i = 0; i < textView.length; i++) {


                    if (cat_check>i)
                    {
                        textView[i].setText(categoriesModelArrayList.get(i).getName());

                        if (categoriesModelArrayList.get(i).getImgUrl().equalsIgnoreCase("null"))
                        {
                            Glide.with(context).load(R.drawable.logo).into(imageView[i]);

                        }
                        else
                        {
                            String picpath=categoriesModelArrayList.get(i).getImgUrl();
                            String baseUrl="http://cp.alnaharsolution.com/assets/uploads/catagories/";
                            StringBuilder stringBuilder=new StringBuilder(baseUrl).append(picpath);

                            Glide.with(context).load(stringBuilder.toString()).into(imageView[i]);

                            // Glide.with(context).load(R.drawable.logo).into(imageView[i]);
                        }

                    }
                    else
                    {
                        textView[i].setText("");
                        Glide.with(context).load("").into(imageView[i]);
                    }


                }

                hideProgress();
            } else {
                hideProgress();
                showToast("Something wrong happened. Try again!", Toast.LENGTH_LONG, Gravity.CENTER);
            }
            super.onPostExecute(aVoid);
        }
    }

    public class InsertSubCategoriesAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            databaseHelper.insertSubCategories(subCategoriesModelArrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (databaseHelper.getTotalSubCategories() > 1) {
                subCategoriesModelArrayList = databaseHelper.getSubCategoriesByCatID(getIntent().getExtras().getInt("catID"));
                subCategoriesAdapter = new SubCategoriesAdapter(context, subCategoriesModelArrayList);
                rvSubCategories.setAdapter(subCategoriesAdapter);
                hideProgress();
            } else {
                hideProgress();
                showToast("Something wrong happened. Try again!", Toast.LENGTH_LONG, Gravity.CENTER);
            }
            super.onPostExecute(aVoid);
        }
    }

    public class InsertProductsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            databaseHelper.insertProducts(itemModelArrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (databaseHelper.getTotalProducts() > 1) {

                if (context instanceof SubCategoriesActivity) {
                    subCategoriesModelArrayList = databaseHelper.getSubCategoriesByCatID(getIntent().getExtras().getInt("catID"));
                    subCategoriesAdapter = new SubCategoriesAdapter(context, subCategoriesModelArrayList);
                    rvSubCategories.setAdapter(subCategoriesAdapter);
                } else if (context instanceof ItemsActivity) {
                    itemModelArrayList = databaseHelper.getProductsByCatAndSubCat(extras.getInt("catID"), extras.getInt("subCatID"));
//                    itemModelArrayList.addAll(databaseHelper.getProductsByCategory(extras.getInt("catID")));
                    itemsAdapter = new ItemsAdapter(context, itemModelArrayList, databaseHelper);
                    rvItems.setAdapter(itemsAdapter);
                }

                hideProgress();
            } else {
                hideProgress();
                showToast("Something wrong happened. Try again!", Toast.LENGTH_LONG, Gravity.CENTER);
            }
            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onResume() {
        if (!(context instanceof LogInActivity)) {
            if (context instanceof ItemsActivity && AppConfig.cartUpdated) {
                itemModelArrayList.clear();
                itemModelArrayList.addAll(databaseHelper.getProductsByCatAndSubCat(extras.getInt("catID"), extras.getInt("subCatID")));
//                itemModelArrayList.addAll(databaseHelper.getProductsByCategory(extras.getInt("catID")));
                itemsAdapter.notifyDataSetChanged();
            }
            if (item != null)
                updateCartCounter();
            //  showToast("parent this", Toast.LENGTH_LONG, Gravity.CENTER);
        }
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        long totalCartItems = sqliteDatabaseHelper.getTotalGridOrderItems();
        if (context instanceof CategoriesActivity || context instanceof SubCategoriesActivity || context instanceof CategoriesGridMenu
                || context instanceof ItemsActivity) {
            updateCartCounter();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateCartCounter() {
        int totalCartItems = databaseHelper.getTotalCartCounter();
        if (totalCartItems > 0) {
            badgeStyle.setColor(Color.GRAY);
            ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, totalCartItems, listener);
            /*tvCounter.setVisibility(View.VISIBLE);
            tvCounter.setText(String.valueOf(totalCartItems));*/
        } else {
            badgeStyle.setColor(Color.TRANSPARENT);
            ActionItemBadge.update(this, item, getResources().getDrawable(R.mipmap.ic_action_cart), badgeStyle, null, listener);

//            badgeStyle.setColor(Color.parseColor("#00000000"));
            // tvCounter.setVisibility(View.GONE);
        }
    }

}
