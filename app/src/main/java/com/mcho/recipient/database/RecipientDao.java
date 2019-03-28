package com.mcho.recipient.database;

import com.mcho.recipient.model.Stocks;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RecipientDao {

    /*** News table query ***/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<Stocks> stocks);

    @Query("SELECT * FROM Stocks")
    LiveData<List<Stocks>> getAllStocks();

    @Query("SELECT COUNT() FROM Stocks")
    LiveData<Integer> getStocksRecordCount();

    @Query("DELETE FROM Stocks")
    void deleteAllStocks();
}
