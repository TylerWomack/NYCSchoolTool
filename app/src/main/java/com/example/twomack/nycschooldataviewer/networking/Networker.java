package com.example.twomack.nycschooldataviewer.networking;

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
    //on Schools vs detailedSchools vs displaySchools: schools are simple objects created from an API call that gives us the school name, SAT score, the # of test takers and a dbn.
    //DetailedSchools provide much, much more information - the API call they are generated from provides nearly 100 data points. I create both school and detailedSchool lists,
    //identify when their dbns are the same, and add the SAT data to the detailedSchool list, which then contains all the external data we need for the rest of the app's lifecycle.
    //displaySchools are the schools that I want to display in the RecyclerView, updating displaySchools triggers an observer that updates that View.
    //note that once the app has made its initial network requests and collected the data, schools and detailedSchools are no longer used.

    //todo: Get a better understanding of disposables...

    /*private MutableLiveData<List<School>> schools;
    private MutableLiveData<List<DetailedSchool>> detailedSchools;
    private MutableLiveData<List<DetailedSchool>> displaySchools;*/

    public Networker(){
        /*MutableLiveData<List<School>> schools = new MutableLiveData<>();
        this.schools = schools;

        MutableLiveData<List<DetailedSchool>> detailedSchool = new MutableLiveData<>();
        this.detailedSchools = detailedSchool;

        MutableLiveData<List<DetailedSchool>> displaySchools = new MutableLiveData<>();
        this.displaySchools = displaySchools;*/
    }

    /*public LiveData<List<School>> getSATSchoolList() {
        return schools;
    }
    public LiveData<List<DetailedSchool>> getSimpleSchools(){ return detailedSchools;}
    public LiveData<List<DetailedSchool>> getDisplaySchoolList(){ return  displaySchools;}*/

    /*public void setDisplaySchoolList(List<DetailedSchool> list){ displaySchools.setValue(list);
    }*/

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

    public void allSchoolsDetailed(){
        Retrofit retrofit = buildRetrofit();

        NYCService postService = retrofit.create(NYCService.class);
        io.reactivex.Observable<List<DetailedSchool>> returnedSchools = postService.getAllSchoolsDetailed();

        returnedSchools.subscribeOn(Schedulers.io())
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

    public void allSchoolsSAT(){
        Retrofit retrofit = buildRetrofit();

        NYCService postService = retrofit.create(NYCService.class);
        io.reactivex.Observable<List<School>> returnedSchools = postService.getAllSchools();
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
