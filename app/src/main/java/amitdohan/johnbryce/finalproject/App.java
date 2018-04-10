package amitdohan.johnbryce.finalproject;

import android.app.Application;
import android.content.Context;
import android.view.View;

public class App extends Application {

    private static Context mContext;
    private static View mView;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        App.mContext = mContext;
    }

    public static View getmView() {
        return mView;
    }

    public static void setmView(View mView) {
        App.mView = mView;
    }
}