package utilis;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.alnehal.chiniotestimator.ChiniotPackage.ChiniotCartActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Engr Shahbaz on 6/21/2016.
 */

public    class MyApplication extends Application {

  public static float totalAmount = 0;
  public static float userId = 0;
  public static String vrnoa_all = "0";
  public static String maxId_global = "0";

  public static String percent ="0";
  public static String discount ="0";

  public static String tax ="0";
  public static String noOfPersons ="0";

  public static String date ="0";
  public static String orderDate ="0";
  public static String insert_user ="0";
  public static String update_user ="0";
  public static String delete_user ="0";
  public static String print_user ="0";


  public static String URL_PAYABLE ="http://mis.alnaharsolution.com/index.php/report/fetchPayRecvReportData";


  public static String URL_PACKAGE_MENU ="http://cp.alnaharsolution.com/index.php/menu/fetchAllWithItems";
  public static String URL_EXTRA_ITEMS="http://cp.alnaharsolution.com/index.php/product/fetchExtraItems";
  public static String URL_SAVE_CART="http://cp.alnaharsolution.com/index.php/estimate/saveApp";
  public static String URL_VRNOA="http://cp.alnaharsolution.com/index.php/estimate/getMaxVrnoa";

  public static String URL_FETCH_VR="http://cp.alnaharsolution.com/index.php/estimate/fetch";
  public static String URL_DELETE_VR="http://cp.alnaharsolution.com/index.php/estimate/delete";



//  public static String URL_PACKAGE_MENU ="http://pakwan.alnaharsolution.com/index.php/menu/fetchAllWithItems";
//  public static String URL_EXTRA_ITEMS="http://pakwan.alnaharsolution.com/index.php/product/fetchExtraItems";
//  public static String URL_SAVE_CART="http://pakwan.alnaharsolution.com/index.php/estimate/saveApp";
//  public static String URL_VRNOA="http://pakwan.alnaharsolution.com/index.php/estimate/getMaxVrnoa";
//
//  public static String URL_FETCH_VR="http://pakwan.alnaharsolution.com/index.php/estimate/fetch";
//  public static String URL_DELETE_VR="http://pakwan.alnaharsolution.com/index.php/estimate/delete";







}
