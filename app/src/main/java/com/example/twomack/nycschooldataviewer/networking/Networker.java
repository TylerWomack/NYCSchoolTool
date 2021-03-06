package com.example.twomack.nycschooldataviewer.networking;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.twomack.nycschooldataviewer.data.DetailedSchool;
import com.example.twomack.nycschooldataviewer.data.MainApplication;
import com.example.twomack.nycschooldataviewer.data.School;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by twomack on 3/22/18.
 */

public class Networker {

    private static Networker INSTANCE;
    private Retrofit retrofitInstance;

    public static Networker getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Networker();
        }
        return INSTANCE;
    }

    /**
     * Singleton style, returns an instance of Retrofit.Builder().
     * @return an instance of Retrofit.Builder() that is hooked up to data.cityofnewyork, an RxJava adapter, and uses GSON deserialization.
     */
    private Retrofit buildRetrofit(){
        if(retrofitInstance != null) {
            return retrofitInstance;
        }

        retrofitInstance = new Retrofit.Builder()
                .baseUrl("https://data.cityofnewyork.us/resource/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofitInstance;
    }

    /**
     * Uses Retrofit and RxJava to get a list of DetailedSchools, which are sorted alphabetically and used to set LiveData in the ApplicationDataModule.
     */
    @SuppressLint("CheckResult")
    public void fetchAllSchoolsDetailed(){
        Retrofit retrofit = buildRetrofit();

        NYCService postService = retrofit.create(NYCService.class);
        io.reactivex.Observable<List<DetailedSchool>> returnedSchools = postService.getAllSchoolsDetailed();

        //subscribeOn Schedulers.io - a background thread used for network calls.
        returnedSchools.subscribeOn(Schedulers.io())
                //sends results to the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<DetailedSchool>>() {
                    @Override
                    public void onNext(List<DetailedSchool> returnedSchools) {
                        //dispatches the value to any active observers
                        List<DetailedSchool> sortedList = sortListByName(returnedSchools);
                        MainApplication.getApplicationDataModule().setDetailedSchools(sortedList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", "Network failed to return detailed schools");
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    private List<DetailedSchool> sortListByName(List<DetailedSchool> list){
        Collections.sort(list, (DetailedSchool school, DetailedSchool t1) -> {
            if (school.getSchoolName().compareTo(t1.getSchoolName()) > 0 ){
                return 1;
            }else{
                return -1;
            }
        });
        return list;
    }

    /**
     * Uses Retrofit and RxJava to get a list of Schools (which contain little more than simple SAT data), which are used to set LiveData in the ApplicationDataModule.
     */
    @SuppressLint("CheckResult")
    public void fetchAllSchoolsSAT(){
        Retrofit retrofit = buildRetrofit();

        NYCService postService = retrofit.create(NYCService.class);
        io.reactivex.Observable<List<School>> returnedSchools = postService.getAllSchoolsSAT();
                returnedSchools.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<School>>() {
                    @Override
                    public void onNext(List<School> returnedSchools) {
                        //dispatches the value to any active observers
                        MainApplication.getApplicationDataModule().setSchools(returnedSchools);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", "Network failed to return simple schools");
                    }

                    @Override
                    public void onComplete() {}
                });
    }
}
