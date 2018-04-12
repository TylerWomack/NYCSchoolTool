package com.example.twomack.nycschooldataviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.twomack.nycschooldataviewer.data.MainApplication;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_screen);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //my way of refreshing the main RecyclerView with the entire list of search data (otherwise, it would display a list of 0 schools - that's why we're here, after all).
        MainApplication.getApplicationDataModule().setDisplaySchools(MainApplication.getApplicationDataModule().getSearchData());
        finish();
    }
}
