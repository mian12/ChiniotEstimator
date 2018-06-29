package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alnehal.chiniotestimator.SubCategoriesActivity;

import java.util.ArrayList;

import model.CartModel;
import model.CategoriesModel;
import model.ExtraDetailCartModel;
import model.ItemModel;
import model.SubCategoriesModel;


/**
 * Created by Muhammad_Shahid on 25-Jul-16.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    //database
    private static final String DATABASE_NAME = "CHINIOT_ESTIMATOR.db";
    //tables
    private static final String CART = "cart";
    private static final String PRODUCTS = "products";
    private static final String CATEGORIES = "categories";
    private static final String SUB_CATEGORIES = "sub_categories";

    /// chiniot package table  name & fields  start here
    private static final String PACKAGE_CART = "package_cart";
    private static final String PACKAGE_EXTRA_ITEMS = "package_extra_items";

    private static final String ITEM_ID ="item_id";
    private static final String ITEM_NAME ="item_name";
    private static final String ITEM_VRNOA ="vrnoa";
    private static final String ITEM_UOM ="uom";
    private static final String ITEM_PACKAGE="package";
    private static final String ITEM_PRICE ="price";
    private static final String ITEM_ADDED="item_added";
    private static final String ITEM_MENU_ID="menu_id";
    /// chiniot package table fields end here//

    private static final String PRODUCT_ID = "product_id";
    private static final String MEAT_TYPE = "meat_type";
    private static final String DATE = "date";
    private static final String NAME = "name";
    private static final String URDU_NAME = "urdu_name";
    private static final String UOM = "uom";
    private static final String LABOUR = "labour";
    private static final String PAY_MODE = "pay_mode";
    private static final String PARTY_ID = "party_id";
    private static final String RATE_EXPT_MEAT = "rate_expt_meat";
    private static final String WEIGHT_MEAT = "weight_meat";
    private static final String URDU_UOM = "urdu_uom";
    private static final String SPECIFICATION = "specification";
    private static final String PER_PERSON = "per_person";
    private static final String STATUS = "status";
    private static final String ID = "id";
    private static final String SERVING = "serving";
    private static final String ROUND = "round";
    private static final String UID = "uid";
    private static final String DATE_TIME = "date_time";
    private static final String CATID = "catid";
    private static final String SUBCATID = "subcatid";
    private static final String KITCHENID = "kitchenid";
    private static final String PHOTO = "photo";
    private static final String CATEGORY_NAME = "category_name";
    private static final String SUBCATEGORY_NAME = "subcategory_name";
    private static final String KITCHEN_NAME = "kitchen_name";
    private static final String CART_ID = "cartID";
    private static final String PRODUCT__CART_QTY = "productCartQTY";
    private static final String MRATE = "mrate";
    private static final String DESCRIPTION = "description";
    private static final String IS_ADDED_TO_CART = "isAddedToCart";
    private static final String URL_PATH = "imageUrl";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " + PRODUCTS + "(" + PRODUCT_ID + " Integer, " + MEAT_TYPE + " Integer," + DATE + " text,"
                + NAME + " text," + URDU_NAME + " text," + UOM + " text," + LABOUR + " text,"
                + PAY_MODE + " text," + PARTY_ID + " integer," + RATE_EXPT_MEAT + " integer,"
                + WEIGHT_MEAT + " integer," + URDU_UOM + " text," + SPECIFICATION + " text,"
                + PER_PERSON + " integer ," + STATUS + " integer ," + ID + " integer ,"
                + SERVING + " integer ," + ROUND + " integer ," + UID + " integer ,"
                + DATE_TIME + " text," + CATID + " integer ," + SUBCATID + " integer ,"
                + KITCHENID + " integer ," + PHOTO + " text," + CATEGORY_NAME + " text,"
                + SUBCATEGORY_NAME + " text," + KITCHEN_NAME + " text, " + MRATE + " integer," + IS_ADDED_TO_CART + " integer DEFAULT 0)");

        db.execSQL("Create Table " + CART + "(" + CART_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + PRODUCT_ID + " Integer, " +
                PRODUCT__CART_QTY + " Integer)");
        db.execSQL("Create Table " + CATEGORIES + "(" + CATID + " Integer, " + NAME + " text," + DESCRIPTION + " text,"+ URL_PATH + " text," + UID + " integer)");

        db.execSQL("Create Table " + SUB_CATEGORIES + "(" + CATID + " Integer, " + NAME + " text," + DESCRIPTION + " text," + UID + " integer," +
                SUBCATID + " integer," + CATEGORY_NAME + " text)");

        db.execSQL("Create Table " + PACKAGE_CART + "(" + ITEM_ID + " text, " + ITEM_NAME + " text," + ITEM_PRICE + " text," + ITEM_UOM + " text," + ITEM_VRNOA+ " text,"+ ITEM_MENU_ID+ " text,"+ ITEM_PACKAGE + " text)");
        db.execSQL("Create Table " + PACKAGE_EXTRA_ITEMS + "(" + ITEM_ID + " text, " + ITEM_NAME + " text," + ITEM_PRICE + " text," + ITEM_ADDED + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + SUB_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + CART);
        db.execSQL("DROP TABLE IF EXISTS " + PACKAGE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + PACKAGE_EXTRA_ITEMS);
        onCreate(db);
    }



    public int packageExitsOrNot() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT     *  FROM " + PACKAGE_CART, null);
        int count = itemsCursor.getCount();
        itemsCursor.close();
        db.close();
        return count;
    }



    public int  itemExitOrNot(String itemId)
    {

        String item_Id="'"+itemId+"'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + ITEM_ID + " FROM " + PACKAGE_EXTRA_ITEMS + " WHERE " + ITEM_ID + "="+item_Id, null);
        ContentValues S = new ContentValues();
        if (itemsCursor.getCount() > 0)
        {

            db.close();
            return 1;
        }else
        {
            db.close();
            return 0;
        }

    }

    public  void deleteExtraItem(String itemId)
    {
        //String item_Id="'"+itemId+"'";
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("delete from "+PACKAGE_EXTRA_ITEMS+" where ITEM_ID='"+itemId+"'");
        db.close();
    }

    public void addedToExtraItems(String itemId, String name, String price,String itemAdded) {




        SQLiteDatabase db = getWritableDatabase();
        ContentValues S = new ContentValues();

             S.put(ITEM_ID, itemId);
            S.put(ITEM_NAME, name);
            S.put(ITEM_ADDED, itemAdded);
            S.put(ITEM_PRICE, price);
            db.insert(PACKAGE_EXTRA_ITEMS, null, S);
            db.close();


    }

    public boolean isAddedToCartPackage(String itemId, String name, String price, String uom,String itemPackage,String itemVrnoa,String itemMenuId) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues S = new ContentValues();
            S.put(ITEM_ID, itemId);
            S.put(ITEM_NAME, name);
            S.put(ITEM_UOM, uom);
            S.put(ITEM_PACKAGE, itemPackage);
            S.put(ITEM_VRNOA, itemVrnoa);
            S.put(ITEM_MENU_ID, itemMenuId);
            S.put(ITEM_PRICE, price);
            db.insert(PACKAGE_CART, null, S);
            db.close();
            return false;
    }


    public  void deleteCart()
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(PACKAGE_CART, null, null);
        db.close();
    }

    public  void deleteExtraItemsCart()
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(PACKAGE_EXTRA_ITEMS, null, null);
        db.close();
    }

    public void claerAllItems() {
        SQLiteDatabase db = getWritableDatabase();

        // db.delete(ITEMS, null, null);


        db.delete(PACKAGE_CART, null, null);
        db.delete(PACKAGE_EXTRA_ITEMS, null, null);

        db.close();
    }



    public ArrayList<ExtraDetailCartModel> getExtraItemsCartVales() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ExtraDetailCartModel> extraCartItemsModelArrayList = new ArrayList<>();
        ExtraDetailCartModel object = null;
        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PACKAGE_EXTRA_ITEMS , null);

        while (itemsCursor.moveToNext()) {
            object = new ExtraDetailCartModel();

            object.setItemId(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_ID)));
            object.setItemName(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_NAME)));
            object.setItemRate(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_PRICE)));

            extraCartItemsModelArrayList.add(object);
        }
        itemsCursor.close();
        db.close();
        return extraCartItemsModelArrayList;
    }


    public ArrayList<CartModel> getCartVales() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
        CartModel cartModel = null;
        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PACKAGE_CART , null);

        while (itemsCursor.moveToNext()) {
            cartModel = new CartModel();

            cartModel.setVrnoa(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_VRNOA)));
            cartModel.setItemName(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_NAME)));
            cartModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_UOM)));
            cartModel.setItemPackage(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_PACKAGE)));
            cartModel.setMenuRate(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_PRICE)));
            cartModel.setItemId(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_ID)));
            cartModel.setMenuId(itemsCursor.getString(itemsCursor.getColumnIndex(ITEM_MENU_ID)));

            cartModelArrayList.add(cartModel);
        }
        itemsCursor.close();
        db.close();
        return cartModelArrayList;
    }



    public void insertProducts(ArrayList<ItemModel> itemModelArrayList) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues S = new ContentValues();
        ItemModel itemModel = null;

        for (int i = 0; i < itemModelArrayList.size(); i++) {
            itemModel = itemModelArrayList.get(i);
            S.put(PRODUCT_ID, itemModel.getProduct_id());
            S.put(MEAT_TYPE, itemModel.getMeat_type());
            S.put(DATE, itemModel.getDate());
            S.put(NAME, itemModel.getName());
            S.put(URDU_NAME, itemModel.getUrdu_name());
            S.put(UOM, itemModel.getUom());
            S.put(LABOUR, itemModel.getLabour());
            S.put(PAY_MODE, itemModel.getPay_mode());
            S.put(PARTY_ID, itemModel.getParty_id());
            S.put(RATE_EXPT_MEAT, itemModel.getRate_expt_meat());
            S.put(WEIGHT_MEAT, itemModel.getWeight_meat());
            S.put(URDU_UOM, itemModel.getUrdu_uom());
            S.put(SPECIFICATION, itemModel.getSpecification());
            S.put(PER_PERSON, itemModel.getPer_person());
            S.put(STATUS, itemModel.getStatus());
            S.put(ID, itemModel.getId());
            S.put(SERVING, itemModel.getServing());
            S.put(ROUND, itemModel.getRound());
            S.put(UID, itemModel.getUid());
            S.put(DATE_TIME, itemModel.getDate_time());
            S.put(CATID, itemModel.getCatid());
            S.put(SUBCATID, itemModel.getSubcatid());
            S.put(KITCHENID, itemModel.getKitchenid());
            S.put(PHOTO, itemModel.getPhoto());
            S.put(CATEGORY_NAME, itemModel.getCategory_name());
            S.put(SUBCATEGORY_NAME, itemModel.getSubcategory_name());
            S.put(KITCHEN_NAME, itemModel.getKitchen_name());
            S.put(MRATE, itemModel.getMrate());
            db.insert(PRODUCTS, null, S);
        }
        db.close();

       /* Cursor subCategoriesCursor = db.rawQuery("select Distinct " + CATID + "," + NAME + "," + UID + "," + CATEGORY_NAME + ","
                + SUBCATID + " from " + PRODUCTS, null);
        SubCategoriesModel subCategoriesModel;
        ArrayList<SubCategoriesModel> subCategoriesModelArrayList = new ArrayList<>();
        while (subCategoriesCursor.moveToNext()) {
            subCategoriesModel = new SubCategoriesModel();
            subCategoriesModel.setCatid(subCategoriesCursor.getInt(subCategoriesCursor.getColumnIndex(CATID)));
            subCategoriesModel.setName(subCategoriesCursor.getString(subCategoriesCursor.getColumnIndex(NAME)));
           // subCategoriesModel.setDescription(subCategoriesCursor.getString(subCategoriesCursor.getColumnIndex(DESCRIPTION)));
            subCategoriesModel.setUid(subCategoriesCursor.getInt(subCategoriesCursor.getColumnIndex(UID)));
            subCategoriesModel.setCategory_name(subCategoriesCursor.getString(subCategoriesCursor.getColumnIndex(CATEGORY_NAME)));
            subCategoriesModel.setSubcatid(subCategoriesCursor.getInt(subCategoriesCursor.getColumnIndex(SUBCATID)));
            subCategoriesModelArrayList.add(subCategoriesModel);
        }
        subCategoriesCursor.close();
        db.close();
        insertSubCategories(subCategoriesModelArrayList);*/
    }

    public void insertCategories(ArrayList<CategoriesModel> categoriesModelArrayList) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(CATEGORIES, null, null);

        ContentValues S = new ContentValues();
        CategoriesModel categoriesModel;

        for (int i = 0; i < categoriesModelArrayList.size(); i++) {
            categoriesModel = categoriesModelArrayList.get(i);
            S.clear();
            S.put(CATID, categoriesModel.getCatid());
            S.put(NAME, categoriesModel.getName());
            S.put(DESCRIPTION, categoriesModel.getDescription());
            S.put(UID, categoriesModel.getUid());
            S.put(URL_PATH, categoriesModel.getImgUrl());
            db.insert(CATEGORIES, null, S);
        }
        db.close();
    }

    public void insertSubCategories(ArrayList<SubCategoriesModel> subCategoriesModelArrayList) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues S = new ContentValues();
        SubCategoriesModel subCategoriesModel;

        for (int i = 0; i < subCategoriesModelArrayList.size(); i++) {
            subCategoriesModel = subCategoriesModelArrayList.get(i);
            S.clear();
            S.put(CATID, subCategoriesModel.getCatid());
            S.put(NAME, subCategoriesModel.getName());
            S.put(DESCRIPTION, subCategoriesModel.getDescription());
            S.put(UID, subCategoriesModel.getUid());
            S.put(SUBCATID, subCategoriesModel.getSubcatid());
            S.put(CATEGORY_NAME, subCategoriesModel.getCategory_name());
            db.insert(SUB_CATEGORIES, null, S);
        }
        db.close();
    }

    public void insertCartCounter(long productID, double counter) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues S = new ContentValues();

        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_ID + " FROM " + CART + " WHERE " + PRODUCT_ID + "=" + productID, null);

        if (counter > 0) {
            // if already have then update other wise insert
            if (itemsCursor.getCount() > 0) {
                S.put(PRODUCT__CART_QTY, counter);
                db.update(CART, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
            } else {
                S.put(PRODUCT_ID, productID);
                S.put(PRODUCT__CART_QTY, counter);
                db.insert(CART, null, S);
            }
            S.clear();
            S.put(IS_ADDED_TO_CART, 1);
            db.update(PRODUCTS, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
        } else {
            //delete
            db.delete(CART, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
            S.clear();
            S.put(IS_ADDED_TO_CART, 0);
            db.update(PRODUCTS, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
        }
    }

    public int getTotalCartCounter() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_ID + " FROM " + CART, null);
        int count = itemsCursor.getCount();
        itemsCursor.close();
        db.close();
        return count;
    }

    public int getTotalProducts() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_ID + " FROM " + PRODUCTS, null);
        int count = itemsCursor.getCount();
        itemsCursor.close();
        db.close();
        return count;
    }

    public ArrayList<ItemModel> getProductsByCategory(int catID) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
        ItemModel itemModel = null;
        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " WHERE " + CATID + "=" + catID, null);

        while (itemsCursor.moveToNext()) {
            itemModel = new ItemModel();
            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));

            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));

            productModelArrayList.add(itemModel);
        }
        itemsCursor.close();
        db.close();
        return productModelArrayList;
    }

    public ArrayList<ItemModel> getProductsByCatAndSubCat(int catID, int subCatID) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
        ItemModel itemModel = null;
        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " WHERE " + CATID + "=" + catID + " AND " + SUBCATID + "=" + subCatID, null);

        while (itemsCursor.moveToNext()) {
            itemModel = new ItemModel();
            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));

            productModelArrayList.add(itemModel);
        }
        itemsCursor.close();
        db.close();
        return productModelArrayList;
    }

    public ArrayList<ItemModel> getProductsBySubCategory(int subCatID) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
        ItemModel itemModel = null;
        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " WHERE " + SUBCATID + "=" + subCatID, null);

        while (itemsCursor.moveToNext()) {
            itemModel = new ItemModel();
            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));

            productModelArrayList.add(itemModel);
        }
        itemsCursor.close();
        db.close();
        return productModelArrayList;
    }

    public ArrayList<CategoriesModel> getCategories() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<>();
        CategoriesModel categoriesModel = null;
        Cursor itemsCursor = db.rawQuery("select * from " + CATEGORIES, null);

        while (itemsCursor.moveToNext()) {
            categoriesModel = new CategoriesModel();
            categoriesModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            categoriesModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            categoriesModel.setDescription(itemsCursor.getString(itemsCursor.getColumnIndex(DESCRIPTION)));
            categoriesModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            categoriesModel.setImgUrl(itemsCursor.getString(itemsCursor.getColumnIndex(URL_PATH)));
            categoriesModelArrayList.add(categoriesModel);
        }
        itemsCursor.close();
        db.close();
        return categoriesModelArrayList;
    }

    public ArrayList<SubCategoriesModel> getSubCategoriesByCatID(int catID) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<SubCategoriesModel> subCategoriesModelArrayList = new ArrayList<>();
        SubCategoriesModel subCategoriesModel = null;
        Cursor itemsCursor = db.rawQuery("select * from " + SUB_CATEGORIES + " Where " + CATID + "=" + catID, null);

        while (itemsCursor.moveToNext()) {
            subCategoriesModel = new SubCategoriesModel();
            subCategoriesModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            subCategoriesModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            subCategoriesModel.setDescription(itemsCursor.getString(itemsCursor.getColumnIndex(DESCRIPTION)));
            subCategoriesModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            subCategoriesModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
            subCategoriesModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
            subCategoriesModelArrayList.add(subCategoriesModel);
        }
        itemsCursor.close();
        db.close();
        return subCategoriesModelArrayList;
    }

    public int getTotalCategories() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + CATID + " FROM " + CATEGORIES, null);
        int count = itemsCursor.getCount();
        itemsCursor.close();
        db.close();
        return count;
    }

    public int getTotalSubCategories() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + SUBCATID + " FROM " + SUB_CATEGORIES, null);
        return itemsCursor.getCount();
    }

    public ItemModel getProductDetail(long productID) {
        SQLiteDatabase db = getWritableDatabase();
        ItemModel itemModel = new ItemModel();
        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " Where " + PRODUCT_ID + "=" + productID, null);
        while (itemsCursor.moveToNext()) {
            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));
        }
        itemsCursor.close();
        db.close();
        return itemModel;
    }

    public boolean isAddedToCart(long productID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor productCursor = db.rawQuery("SELECT  " + PRODUCT_ID + " FROM " + CART + " WHERE " + PRODUCT_ID + "=" + productID, null);

        if (productCursor.getCount() > 0) {
            // if already have then update other wise insert
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public ArrayList<ItemModel> getCartProducts() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        ItemModel itemModel = null;
        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCTS + ".*," + CART + "." + PRODUCT__CART_QTY + " FROM " + PRODUCTS +
                " Inner Join " + CART + " ON " + PRODUCTS + "." + PRODUCT_ID + "=" + CART + "." + PRODUCT_ID + " Order by " + PRODUCT_ID + " ASC", null);

        while (itemsCursor.moveToNext()) {
            itemModel = new ItemModel();
            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));
            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));
            itemModel.setItemCartQty(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT__CART_QTY)));
            itemModelArrayList.add(itemModel);
        }
        itemsCursor.close();
        db.close();
        return itemModelArrayList;
    }

    public void clearCart(String IDsString) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues S = new ContentValues();
        S.put(IS_ADDED_TO_CART, 0);
        db.update(PRODUCTS, S, PRODUCT_ID + " IN (" + IDsString + ")", null);

        db.delete(CART, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + CART + "'");
        db.close();
    }

    public ArrayList<ItemModel> getAllProducts() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ItemModel> productModelArrayList = new ArrayList<>();
        ItemModel itemModel = null;
        Cursor itemsCursor = db.rawQuery("SELECT  * FROM " + PRODUCTS + " where " + PRODUCTS + "." + IS_ADDED_TO_CART + "=0", null);

        while (itemsCursor.moveToNext()) {
            itemModel = new ItemModel();
            itemModel.setProduct_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_ID)));
            itemModel.setMeat_type(itemsCursor.getInt(itemsCursor.getColumnIndex(MEAT_TYPE)));
            itemModel.setDate(itemsCursor.getString(itemsCursor.getColumnIndex(DATE)));
            itemModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(NAME)));
            itemModel.setUrdu_name(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_NAME)));
            itemModel.setUom(itemsCursor.getString(itemsCursor.getColumnIndex(UOM)));
            itemModel.setLabour(itemsCursor.getString(itemsCursor.getColumnIndex(LABOUR)));
            itemModel.setPay_mode(itemsCursor.getString(itemsCursor.getColumnIndex(PAY_MODE)));
            itemModel.setParty_id(itemsCursor.getInt(itemsCursor.getColumnIndex(PARTY_ID)));
            itemModel.setRate_expt_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(RATE_EXPT_MEAT)));
            itemModel.setWeight_meat(itemsCursor.getDouble(itemsCursor.getColumnIndex(WEIGHT_MEAT)));
            itemModel.setUrdu_uom(itemsCursor.getString(itemsCursor.getColumnIndex(URDU_UOM)));
            itemModel.setSpecification(itemsCursor.getString(itemsCursor.getColumnIndex(SPECIFICATION)));
            itemModel.setPer_person(itemsCursor.getDouble(itemsCursor.getColumnIndex(PER_PERSON)));
            itemModel.setStatus(itemsCursor.getInt(itemsCursor.getColumnIndex(STATUS)));
            itemModel.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(ID)));
            itemModel.setServing(itemsCursor.getDouble(itemsCursor.getColumnIndex(SERVING)));
            itemModel.setRound(itemsCursor.getInt(itemsCursor.getColumnIndex(ROUND)));
            itemModel.setUid(itemsCursor.getInt(itemsCursor.getColumnIndex(UID)));
            itemModel.setDate_time(itemsCursor.getString(itemsCursor.getColumnIndex(DATE_TIME)));
            itemModel.setCatid(itemsCursor.getInt(itemsCursor.getColumnIndex(CATID)));
            itemModel.setSubcatid(itemsCursor.getInt(itemsCursor.getColumnIndex(SUBCATID)));
            itemModel.setKitchenid(itemsCursor.getInt(itemsCursor.getColumnIndex(KITCHENID)));
            itemModel.setPhoto(itemsCursor.getString(itemsCursor.getColumnIndex(PHOTO)));
            itemModel.setCategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(CATEGORY_NAME)));
            itemModel.setSubcategory_name(itemsCursor.getString(itemsCursor.getColumnIndex(SUBCATEGORY_NAME)));
            itemModel.setKitchen_name(itemsCursor.getString(itemsCursor.getColumnIndex(KITCHEN_NAME)));
            itemModel.setMrate(itemsCursor.getDouble(itemsCursor.getColumnIndex(MRATE)));

            itemModel.setIsAddedToCart(itemsCursor.getInt(itemsCursor.getColumnIndex(IS_ADDED_TO_CART)));

            productModelArrayList.add(itemModel);
        }
        itemsCursor.close();
        db.close();
        return productModelArrayList;
    }
}
