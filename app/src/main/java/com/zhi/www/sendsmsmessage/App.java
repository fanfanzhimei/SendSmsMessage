package com.zhi.www.sendsmsmessage;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/10/9.
 */
public class App extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
