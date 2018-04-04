package com.example.twomack.nycschooldataviewer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.twomack.nycschooldataviewer.CompositeDisposableModule;
import com.example.twomack.nycschooldataviewer.DetailedSchool;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by twomack on 3/28/18.
 */

public class MainViewModel extends ViewModel {
    private CompositeDisposableModule compositeDisposableModule;

    private MutableLiveData<List<DetailedSchool>> searchData;

    public MainViewModel(CompositeDisposableModule compositeDisposable){
        this.compositeDisposableModule = compositeDisposable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposableModule.dispose();
    }

    public CompositeDisposableModule getDisposable() {
        return compositeDisposableModule;
    }

    public void setSearchData(List<DetailedSchool> list){
        if (searchData == null){
            searchData = new MutableLiveData<>();
        }
        searchData.setValue(list);
    }

    public MutableLiveData<List<DetailedSchool>> getSearchData(){
        return searchData;
    }
}
