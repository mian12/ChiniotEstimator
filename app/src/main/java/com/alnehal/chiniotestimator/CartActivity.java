package com.alnehal.chiniotestimator;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import DB.SQLiteDatabaseHelper;
import adapter.CartAdapter;
import app.AppConfig;
import model.ItemModel;

public class CartActivity extends ParentActivity implements PermissionCallback, ErrorCallback {

//    SwipeRefreshLayout srlCart;

    LinearLayout llIncDecPer;
    private static final int EXTERNAL_STORAGE_PERMISSION = 701;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        context = this;
        dialog = new ProgressDialog(context);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new SQLiteDatabaseHelper(this);
        rvCart = (RecyclerView) findViewById(R.id.rvCart);
//        srlCart = (SwipeRefreshLayout) findViewById(R.id.srlCart);
        etPersons = (EditText) findViewById(R.id.etPersons);
        etIncDecPercent = (EditText) findViewById(R.id.etIncDecPercent);
        rgIncDecPer = (RadioGroup) findViewById(R.id.rgIncDecPer);
        rbNoIncDec = (RadioButton) findViewById(R.id.rbNoIncDec);
        rbIncPer = (RadioButton) findViewById(R.id.rbIncPer);
        rbDecPer = (RadioButton) findViewById(R.id.rbDecPer);
        tvNotFound = (TextView) findViewById(R.id.tvNotFound);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        tvVRNOA = (TextView) findViewById(R.id.tvVRNOA);
        tvTotalSr = (TextView) findViewById(R.id.tvTotalSr);
        tvTotalQty = (TextView) findViewById(R.id.tvTotalQty);
        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
        tvTotalWeight = (TextView) findViewById(R.id.tvTotalWeight);
        btnGetEstimate = (Button) findViewById(R.id.btnGetEstimate);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnClearCart = (Button) findViewById(R.id.btnClearCart);
        rlTotal = (RelativeLayout) findViewById(R.id.rlTotal);
        llIncDecPer = (LinearLayout) findViewById(R.id.llIncDecPer);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vrdate = date_time = order_date = datetime = df.format(c.getTime());


        rbNoIncDec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llIncDecPer.setVisibility(View.GONE);
                } else {
                    llIncDecPer.setVisibility(View.VISIBLE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemModelArrayList.size() > 0) {
                    try {
                        JSONObject estimateObject = new JSONObject();
//                    estimateObject.put("party_uname", party_uname);
//                    estimateObject.put("party_mobile", party_mobile);
//                    estimateObject.put("party_city", party_city);
//                    estimateObject.put("party_address", party_address);
//                    estimateObject.put("party_limit", party_limit);
//                    estimateObject.put("party_cityarea", party_cityarea);
//                    estimateObject.put("date_time", date_time);
//                    estimateObject.put("party_name", party_name);
//                    estimateObject.put("oid", oid);
                        estimateObject.put("vrnoa", vrnoa);
                        estimateObject.put("vrno", vrno);
                        estimateObject.put("vrdate", vrdate);
                        estimateObject.put("etype", etype);
                        estimateObject.put("party_id", party_id);
                        estimateObject.put("order_date", order_date);
                        persons = etPersons.getText().toString().trim();
                        if (persons.length() > 1 && (Integer.parseInt(persons) > 0)) {
                        } else {
                            persons = "1";
                        }
                        estimateObject.put("rate_calc", "0");
                        estimateObject.put("persons", persons);
                        estimateObject.put("per_head", per_head);
                        estimateObject.put("total_amount", tvTotalAmount.getText().toString().trim());
                        estimateObject.put("advance", advance);
                        estimateObject.put("balance", balance);
//                    estimateObject.put("datetime", datetime);
                        estimateObject.put("remarks", remarks);
                        estimateObject.put("status1", status1);
//                    estimateObject.put("phone", phone);
                        estimateObject.put("uid", uid);
                        estimateObject.put("ph", ph);
                        estimateObject.put("total_weight", tvTotalWeight.getText().toString().trim());

                        JSONArray estimateDetailArray = new JSONArray();
                        JSONObject estimateDetailObject;
                        for (int i = 0; i < itemModelArrayList.size(); i++) {
                            estimateDetailObject = new JSONObject();
                            ItemModel itemModel = itemModelArrayList.get(i);
                            estimateDetailObject.put("oid", "0");
                            estimateDetailObject.put("prid", itemModel.getProduct_id());
//                        estimateDetailObject.put("products_name", itemModel.getName());
//                        estimateDetailObject.put("products_uname", itemModel.getUrdu_name());
                            estimateDetailObject.put("uom", itemModel.getUom());
                            estimateDetailObject.put("category", itemModel.getCatid());
//                        estimateDetailObject.put("categoy_name", itemModel.getCategory_name());
//                        estimateDetailObject.put("products_serving", itemModel.getServing());
                            estimateDetailObject.put("qty", itemModel.getItemCartQty());
                            estimateDetailObject.put("rate", itemModel.getRate_expt_meat());
                            estimateDetailObject.put("mrate", itemModel.getMrate());
                            estimateDetailObject.put("amount", itemModel.getAmountTotal());
                            estimateDetailObject.put("per", itemModel.getPer_person());
                            estimateDetailObject.put("weight", itemModel.getWeight_meat());
                            estimateDetailObject.put("gweight", tvTotalWeight.getText().toString().trim());
//                        estimateDetailObject.put("date_time", date_time);
//                        estimateDetailObject.put("user_name", "");
//                        estimateDetailObject.put("total_amount", grandTotal);
//                        estimateDetailObject.put("products_round", itemModel.getRound());
//                        estimateDetailObject.put("urdu_uom", itemModel.getUrdu_uom());
                            estimateDetailArray.put(estimateDetailObject);
                        }

                        saveVoucherRequest(estimateObject.toString(), estimateDetailArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("Add atleast one item!", 1, Gravity.CENTER);
                }
            }
        });

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemModelArrayList.size() > 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Clear...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want clear cart?");

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Write your code here to invoke YES event
                            String IDsString = "";
                            for (int i = 0; i < itemModelArrayList.size(); i++) {
                                IDsString = IDsString + itemModelArrayList.get(i).getProduct_id() + ",";
                            }
                            IDsString = IDsString.substring(0, IDsString.length() - 1);
                            databaseHelper.clearCart(IDsString);
                            itemModelArrayList.clear();
                            cartAdapter.notifyDataSetChanged();
                            setTvNotFoundVisibility(true);
                            AppConfig.cartUpdated = true;
                            resetVariables();
                            updateTotal();
                            dialog.dismiss();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.dismiss();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(CartActivity.this);
                if (itemModelArrayList.size() > 0 && cartAdapter.isEstimated) {
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        getPermissionToReadExternalStorage();
                    } else {
                        EstimatePrintFormA4Eng estimatePrintFormA4Eng = new EstimatePrintFormA4Eng(context, itemModelArrayList,
                                getDate(), "Estimated Purchase Voucher", "ABC", String.valueOf(grandTotal), numberOfPersons);
                    }
//                    reqPermission();
                } else {
                    showToast("First get Estimate!", 1, Gravity.CENTER);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog editDialog = new Dialog(context);
                editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                editDialog.setCancelable(true);
                editDialog.setContentView(R.layout.dialog_cart_search_voucher);
                editDialog.setTitle("Enter Voucher No");
                final EditText etVoucherNo = (EditText) editDialog.findViewById(R.id.etVoucherNo);
                Button btnCancel = (Button) editDialog.findViewById(R.id.btnCancel);
                Button btnDone = (Button) editDialog.findViewById(R.id.btnDone);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CartActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        editDialog.dismiss();
                    }
                });
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CartActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        if (etVoucherNo.getText().toString().trim().length() < 0) {
                            showToast("Enter Voucher No!", 1, Gravity.CENTER);
                            return;
                        }
                        int voucherNo = Integer.parseInt(etVoucherNo.getText().toString().trim());
                        if (voucherNo < 1) {
                            showToast("Enter Voucher No!", 1, Gravity.CENTER);
                            return;
                        }
                        fetchVoucherRequest(voucherNo);
                        editDialog.dismiss();
                    }
                });
                editDialog.show();
            }
        });
        btnGetEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cartAdapter.isFetchedItem) {
                    itemModelArrayList.clear();
                    itemModelArrayList.addAll(databaseHelper.getCartProducts());
                }
//                itemModelArrayList = databaseHelper.getCartProducts();
                hideKeyboard(CartActivity.this);
                ArrayList<ItemModel> dryItemsModelArrayList = new ArrayList<>(), sweatsItemsModelArrayList = new ArrayList<>(), riceItemsModelArrayList = new ArrayList<>(),
                        othersItemsModelArrayList = new ArrayList<>(), bakeryItemsModelArrayList = new ArrayList<>(), chineseItemsModelArrayList = new ArrayList<>(), sauceItemsModelArrayList = new ArrayList<>();

                String drystr = "", sweatsStr = "", riceStr = "", othersStr = "", bakeryStr = "", chineseStr = "", sauceStr = "";
                cartProductIDsArrayList = new ArrayList<>();
                for (int i = 0; i < itemModelArrayList.size(); i++) {
                    ItemModel itemModel = itemModelArrayList.get(i);
                    String catName = itemModel.getCategory_name();
                    if (catName.equalsIgnoreCase("Dry") || catName.equalsIgnoreCase("Greavy") || catName.equalsIgnoreCase("Gravy")
                            || catName.equalsIgnoreCase("Dry BBQ") || catName.equalsIgnoreCase("Qourma Gravy")) {
                        dryItemsModelArrayList.add(itemModel);
                        drystr = drystr + itemModel.getProduct_id() + ",";
                    } else if (catName.equalsIgnoreCase("Sweet")) {
                        sweatsItemsModelArrayList.add(itemModel);
                        sweatsStr = sweatsStr + itemModel.getProduct_id() + ",";
                    } else if (catName.equalsIgnoreCase("Sauce")) {
                        sauceItemsModelArrayList.add(itemModel);
                        sauceStr = sauceStr + itemModel.getProduct_id() + ",";
                    } else if (catName.equalsIgnoreCase("Rice") || catName.equalsIgnoreCase("Biryani Rice")) {
                        riceItemsModelArrayList.add(itemModel);
                        riceStr = riceStr + itemModel.getProduct_id() + ",";
                    } else if (catName.equalsIgnoreCase("Bakery")) {
                        bakeryItemsModelArrayList.add(itemModel);
                        bakeryStr = bakeryStr + itemModel.getProduct_id() + ",";
                    } else if (catName.equalsIgnoreCase("Chinese Gravy")) {
                        chineseItemsModelArrayList.add(itemModel);
                        chineseStr = chineseStr + itemModel.getProduct_id() + ",";
                    } else if (catName.equalsIgnoreCase("Salad") || catName.equalsIgnoreCase("Tea") || catName.equalsIgnoreCase("Drink")) {
                        othersItemsModelArrayList.add(itemModel);
                        othersStr = othersStr + itemModel.getProduct_id() + ",";
                    } else {
                        othersItemsModelArrayList.add(itemModel);
                        othersStr = othersStr + itemModel.getProduct_id() + ",";
                    }
                    cartProductIDsArrayList.add(itemModel.getProduct_id());
                }

                try {
                    drystr = drystr.substring(0, drystr.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    sweatsStr = sweatsStr.substring(0, sweatsStr.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    riceStr = riceStr.substring(0, riceStr.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    othersStr = othersStr.substring(0, othersStr.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    bakeryStr = bakeryStr.substring(0, bakeryStr.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    chineseStr = chineseStr.substring(0, chineseStr.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    sauceStr = sauceStr.substring(0, sauceStr.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String detailStr = "";
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < dryItemsModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("combination", drystr);
                        jsonObject.put("productid", dryItemsModelArrayList.get(i).getProduct_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                for (int i = 0; i < sweatsItemsModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("combination", sweatsStr);
                        jsonObject.put("productid", sweatsItemsModelArrayList.get(i).getProduct_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                for (int i = 0; i < riceItemsModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("combination", riceStr);
                        jsonObject.put("productid", riceItemsModelArrayList.get(i).getProduct_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                for (int i = 0; i < othersItemsModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("combination", othersStr);
                        jsonObject.put("productid", othersItemsModelArrayList.get(i).getProduct_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                for (int i = 0; i < bakeryItemsModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("combination", bakeryStr);
                        jsonObject.put("productid", bakeryItemsModelArrayList.get(i).getProduct_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                for (int i = 0; i < chineseItemsModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("combination", chineseStr);
                        jsonObject.put("productid", chineseItemsModelArrayList.get(i).getProduct_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                for (int i = 0; i < sauceItemsModelArrayList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("combination", sauceStr);
                        jsonObject.put("productid", sauceItemsModelArrayList.get(i).getProduct_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }

                detailStr = jsonArray.toString();
                getEstimate(detailStr);

             /*   String IDsString = "";
                for (int i = 0; i < itemModelArrayList.size(); i++) {
                    IDsString = IDsString + itemModelArrayList.get(i).getProduct_id() + ",";
                }
                IDsString = IDsString.substring(0, IDsString.length() - 1);
                databaseHelper.clearCart(IDsString);
                itemModelArrayList.clear();
                cartAdapter.notifyDataSetChanged();
                setTvNotFoundVisibility();
                Toast toast = Toast.makeText(CartActivity.this, "Order submitted successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                AppConfig.cartUpdated = true;*/
            }
        });

        itemModelArrayList = databaseHelper.getCartProducts();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(linearLayoutManager);

        cartAdapter = new CartAdapter(this, itemModelArrayList, databaseHelper);
        rvCart.setAdapter(cartAdapter);

        if (cartAdapter.getItemCount() == 0) {
            setTvNotFoundVisibility(true);
        } else {
            setTvNotFoundVisibility(false);
        }

        //  initiateSwipe();
        getSupportActionBar().setTitle(R.string.order_detail);
        reqFetchMaxVRNOA();
        updateTotal();
    }

    public void getPermissionToReadExternalStorage() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(CartActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            EstimatePrintFormA4Eng estimatePrintFormA4Eng = new EstimatePrintFormA4Eng(context, itemModelArrayList,
                    getDate(), "Estimated Purchase Voucher", "ABC", String.valueOf(grandTotal), numberOfPersons);

            // We have access. Life is good.
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(CartActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CartActivity.this);
            builder.setMessage("This permission is required to access your external Storage.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);
                        }
                    });
            android.app.AlertDialog alert = builder.create();
            alert.show();
            // We've been denied once before. Explain why we need the permission, then ask again.
        } else {

            // We've never asked. Just do it.
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        // Make sure it's our original READ_CONTACTS request
        if (requestCode == EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                EstimatePrintFormA4Eng estimatePrintFormA4Eng = new EstimatePrintFormA4Eng(context, itemModelArrayList,
                        getDate(), "Estimated Purchase Voucher", "ABC", String.valueOf(grandTotal), numberOfPersons);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void reqPermission() {
        new AskPermission.Builder(this).setPermissions(Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setCallback(this)
                .setErrorCallback(this)
                .request(EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Permissions Denied.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;

            case R.id.additem:
                showItemSearchDialog();
                break;
        }

        return false;
    }

    private void initiateSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                databaseHelper.insertCartCounter(itemModelArrayList.get(position).getProduct_id(), 0);
                itemModelArrayList.remove(position);
                cartAdapter.notifyDataSetChanged();
                updateTotal();
                if (itemModelArrayList.isEmpty()) {
                    setTvNotFoundVisibility(true);
                } else {
                    setTvNotFoundVisibility(false);
                }
                AppConfig.cartUpdated = true;
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvCart);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.search);
        additem = menu.findItem(R.id.additem);
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
        return super.onCreateOptionsMenu(menu);
    }

}
