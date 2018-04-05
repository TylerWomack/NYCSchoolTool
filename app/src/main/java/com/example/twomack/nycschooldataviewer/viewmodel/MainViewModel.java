package com.example.twomack.nycschooldataviewer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.twomack.nycschooldataviewer.CompositeDisposableModule;
import com.example.twomack.nycschooldataviewer.DetailedSchool;

import java.util.List;

/**
 * Created by twomack on 3/28/18.
 */

public class MainViewModel extends ViewModel {
    private CompositeDisposableModule compositeDisposableModule;

    private MutableLiveData<List<DetailedSchool>> searchData;

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

    public void setCompositeDisposableModule(CompositeDisposableModule compositeDisposableModule) {
        this.compositeDisposableModule = compositeDisposableModule;
    }

    public CompositeDisposableModule getCompositeDisposableModule() {
        return compositeDisposableModule;
    }
}
