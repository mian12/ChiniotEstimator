package app;

import android.app.Application;

import DB.SQLiteDatabaseHelper;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    public SQLiteDatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
}
