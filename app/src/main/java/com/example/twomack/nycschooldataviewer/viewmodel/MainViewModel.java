package com.example.twomack.nycschooldataviewer.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.twomack.nycschooldataviewer.CompositeDisposableModule;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by twomack on 3/28/18.
 */

public class MainViewModel extends ViewModel {
    private CompositeDisposableModule compositeDisposableModule;

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

}
