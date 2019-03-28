package com.mcho.recipient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mcho.recipient.adapter.AdapterEmptyStocks;
import com.mcho.recipient.adapter.AdapterErrorStocks;
import com.mcho.recipient.adapter.AdapterStocks;
import com.mcho.recipient.model.Stocks;
import com.mcho.recipient.network.ApiService;
import com.mcho.recipient.network.ServiceGenerator;
import com.mcho.recipient.utils.Global;
import com.mcho.recipient.utils.SharedPrefs;
import com.mcho.recipient.viewmodel.StockViewModel;
import com.mcho.recipient.vmfactory.VMFStocks;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerStock) RecyclerView recyclerView;
    @BindView(R.id.pLoading) ProgressBar progressBar;

    private AdapterStocks adapterStocks;
    private AdapterErrorStocks adapterErrorStocks;
    private AdapterEmptyStocks adapterEmptyStocks;
    private StockViewModel stockViewModel;
    private CompositeDisposable disposable;

    private SearchManager searchManager;
    private SearchView searchView;

    private SharedPrefs sf;
    private ApiService apiService;

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(String msg) {
        if(msg.equals("refreshAdapter"))
            loadStocks();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        sf = new SharedPrefs(this);
        disposable = new CompositeDisposable();

        initViews();

        stockViewModel = ViewModelProviders.of(this, new VMFStocks(this.getApplication(), disposable)).get(StockViewModel.class);

        loadStocks();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menusearch:
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                break;

            case R.id.menusync:
                loadStocks();
                break;

            case R.id.menulogout:
                confirmLogout();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menusearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint("Search medicine or supply...");

        return true;
    }

    public void retryGetStocks(){
        recyclerView.setAdapter(null);
        loadStocks();
    }

    private void setErrorAdapter(String err_msg){
        adapterErrorStocks = new AdapterErrorStocks(err_msg, this);
        recyclerView.setAdapter(adapterErrorStocks);
    }

    private void initViews(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void loadStocks(){
        progressBar.setVisibility(View.VISIBLE);
        stockViewModel.getStocks().observe(this, new Observer<List<Stocks>>() {
            @Override
            public void onChanged(List<Stocks> stocks) {
                progressBar.setVisibility(View.GONE);

                if(stocks.size() > 0) {
                    ArrayList<Stocks> stockList = new ArrayList<>(stocks);
                    adapterStocks = new AdapterStocks(getApplicationContext(), stockList, MainActivity.this);
                    recyclerView.setAdapter(adapterStocks);
                }else{
                    adapterEmptyStocks = new AdapterEmptyStocks();
                    recyclerView.setAdapter(adapterEmptyStocks);
                }

            }
        });
/*        stockViewModel.getStocksError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                progressBar.setVisibility(View.GONE);
                if(isError){
                    stockViewModel.getStocksThrowable().observe(MainActivity.this, new Observer<Throwable>() {
                        @Override
                        public void onChanged(Throwable throwable) {
                            String err_msg = Global.getResponseCode(throwable);
                            setErrorAdapter(err_msg);
                        }
                    });
                }
            }
        });*/
    }

    private void confirmLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you wanna logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutMe();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void logoutMe(){

        progressBar.setVisibility(View.VISIBLE);

        apiService = ServiceGenerator.createService(ApiService.class);

        apiService.logoutCredential(sf.getUserId(), sf.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(String response) {
                        progressBar.setVisibility(View.GONE);

                        if(response.equals("success")){
                            sf.clearToken();
                           navigateToLogin();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        String err_msg = Global.getResponseCode(e);
                        showSnackBar(err_msg, R.color.colorSnackBarError);
                    }
                });
    }

    private void showSnackBar(String err_msg, int colorId){
        Snackbar snackbar =  Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getResources().getColor(colorId));
        snackbar.show();
    }

    private void navigateToLogin(){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
