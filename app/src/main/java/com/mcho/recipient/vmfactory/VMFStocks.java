package com.mcho.recipient.vmfactory;

import android.app.Application;

import com.mcho.recipient.viewmodel.StockViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;

public class VMFStocks implements ViewModelProvider.Factory {
    private Application mApplication;
    private CompositeDisposable mCompositeDisposable;

    public VMFStocks(Application application, CompositeDisposable disposable) {
        mApplication = application;
        mCompositeDisposable = disposable;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new StockViewModel(mApplication, mCompositeDisposable);
    }
}
