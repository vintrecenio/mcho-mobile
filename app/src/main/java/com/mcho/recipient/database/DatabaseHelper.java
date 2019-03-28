package com.mcho.recipient.database;


import android.content.Context;
import com.mcho.recipient.model.Stocks;
import java.util.List;
import androidx.lifecycle.LiveData;

public class DatabaseHelper {

    private RecipientDao recipientDao;

    public DatabaseHelper(Context context) {
        recipientDao = AppDatabase.getInstance(context).getNewsDao();
    }

    public LiveData<List<Stocks>> getStocks(){
        return recipientDao.getAllStocks();
    }

    public void insertStocks(List<Stocks> stocks){
        recipientDao.saveAll(stocks);
    }

    public LiveData<Integer> getStocksRecordCount(){
        return recipientDao.getStocksRecordCount();
    }

    public void deleteStocks(){
        recipientDao.deleteAllStocks();
    }

}
