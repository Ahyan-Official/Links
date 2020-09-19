package com.links.app.a4x2;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;


public class MyApplication extends Application {



    private static MyApplication mInstense;




    public MyApplication(){


        mInstense = this;

    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstense = this;

        MobileAds.initialize(this);

    }



    public static synchronized  MyApplication getmInstense(){

        return mInstense;
    }





}
