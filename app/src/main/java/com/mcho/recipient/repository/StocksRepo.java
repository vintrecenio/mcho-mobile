package com.mcho.recipient.repository;

import android.content.Context;
import android.util.Log;

import com.mcho.recipient.database.DatabaseHelper;
import com.mcho.recipient.model.Stocks;
import com.mcho.recipient.network.ApiService;
import com.mcho.recipient.network.ServiceGenerator;
import com.mcho.recipient.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StocksRepo {

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Stocks>> mutableStocks;
    private MutableLiveData<Boolean> stocksError;
    private MutableLiveData<Throwable> throwableMutableLiveData;
    private DatabaseHelper helper;
    private SharedPrefs sf;

    public StocksRepo(Context context, CompositeDisposable disposable) {
        this.compositeDisposable = disposable;
        mutableStocks = new MutableLiveData<>();
        stocksError = new MutableLiveData<>();
        throwableMutableLiveData = new MutableLiveData<>();
        sf = new SharedPrefs(context);
        helper = new DatabaseHelper(context);
    }

    public LiveData<List<Stocks>> getStocks(){

        apiService = ServiceGenerator.createService(ApiService.class);

        apiService.getStocks(sf.getRecipientId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<Stocks>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<Stocks> stocks) {
                        mutableStocks.setValue(stocks);
                        stocksError.setValue(false);
                        helper.deleteStocks();
                        helper.insertStocks(stocks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        stocksError.setValue(true);
                        throwableMutableLiveData.setValue(e);
                        Log.e("API_ERR", e.toString());
                    }
                });

            return helper.getStocks();
    }

    public LiveData<Boolean> getStocksError(){
        return stocksError;
    }

    public LiveData<Throwable> getStocksThrowable(){
        return throwableMutableLiveData;
    }
}
