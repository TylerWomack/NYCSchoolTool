package com.example.twomack.nycschooldataviewer;

import android.app.Application;

import java.util.List;

/**
 * Created by twomack on 4/4/18.
 */

public class MainApplication extends Application {


    private static ApplicationDataModule module;

    @Override
    public void onCreate() {
        super.onCreate();
        module = new ApplicationDataModule();
    }

    public static ApplicationDataModule getApplicationDataModule() {
        return module;
    }

}
