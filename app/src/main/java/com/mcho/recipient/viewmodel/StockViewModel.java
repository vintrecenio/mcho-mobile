package com.mcho.recipient.viewmodel;

import android.app.Application;

import com.mcho.recipient.model.Stocks;
import com.mcho.recipient.repository.StocksRepo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.disposables.CompositeDisposable;

public class StockViewModel extends AndroidViewModel {

    private StocksRepo stocksRepo;

    public StockViewModel(@NonNull Application application, CompositeDisposable disposable) {
        super(application);
        stocksRepo = new StocksRepo(application.getApplicationContext(), disposable);
    }

    public LiveData<List<Stocks>> getStocks(){
        return stocksRepo.getStocks();
    }

    public LiveData<Boolean> getStocksError(){
        return  stocksRepo.getStocksError();
    }

    public LiveData<Throwable> getStocksThrowable(){
        return  stocksRepo.getStocksThrowable();
    }

}
