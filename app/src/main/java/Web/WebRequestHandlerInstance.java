package Web;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.alnehal.chiniotestimator.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebRequestHandlerInstance {

    private static final String BASE_URL = "http://cp.alnaharsolution.com/index.php/";
  //  private static final String BASE_URL = "http://pakwan.alnaharsolution.com/index.php/";
    private static int DEFAULT_TIMEOUT = 20 * 1000;
    private static AsyncHttpClient client = new AsyncHttpClient();

    private static void setTimeOut() {
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        clearHeaders();
        addHeaders();
        setTimeOut();
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        clearHeaders();
        addHeaders();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static void clearHeaders() {
        client.removeAllHeaders();
    }

    private static void addHeaders() {
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
    }
}