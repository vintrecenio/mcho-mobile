package com.mcho.recipient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mcho.recipient.fragment.FragmentRequisition;
import com.mcho.recipient.model.DeductionResponse;
import com.mcho.recipient.network.ApiService;
import com.mcho.recipient.network.ServiceGenerator;
import com.mcho.recipient.utils.Global;
import com.mcho.recipient.utils.SharedPrefs;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DispatchActivity extends AppCompatActivity {

    @BindView(R.id.dispatchAvatar) ImageView avatar;
    @BindView(R.id.dispatchStock) TextView stock;
    @BindView(R.id.dispatchUnit) TextView unit;
    @BindView(R.id.dispatchDescription) TextView description;
    @BindView(R.id.deductionCountWrapper) TextInputLayout qtyWrapper;
    @BindView(R.id.dispatchQty) TextInputEditText qty;
    @BindView(R.id.dispatchRemarks) TextInputEditText remarks;
    @BindView(R.id.progressDeduction) ProgressBar deductionProgess;
    @BindView(R.id.btnDeduct) Button deduct;
    @BindView(R.id.btnRequest) Button request;

    private SharedPrefs sf;
    private CompositeDisposable disposable;
    private ApiService apiService;
    private String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

        disposable = new CompositeDisposable();
        sf = new SharedPrefs(this);

        ButterKnife.bind(this);

        getIntentExtras();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btnDeduct)
    void onClickProceed(){

        if(!qty.getText().toString().equals("")) {
            int tmpQty = Integer.valueOf(qty.getText().toString());

            if(tmpQty > 0) {
                proceedDeduction();
            }else{
                qtyWrapper.setErrorEnabled(true);
                qtyWrapper.setError(getResources().getString(R.string.mustnotzero));
            }
        }else{
            qtyWrapper.setErrorEnabled(true);
            qtyWrapper.setError(getResources().getString(R.string.enterquantity));
        }
    }

/*    @OnClick(R.id.btnDeduct)
    void onValidateDeduction(){
        Intent i = new Intent(DispatchActivity.this, SignatureActivity.class);
        startActivity(i);
    }*/

    @OnClick(R.id.btnRequest)
    void onClickRequest(){

        showRequisition();
    }

    @OnTextChanged(R.id.dispatchQty)
    void changeQty() {

        if(!qty.getText().toString().isEmpty()) {
            int tmpQty = Integer.valueOf(qty.getText().toString());
            if (tmpQty > 0) {
                qtyWrapper.setErrorEnabled(false);
            }else{
                qtyWrapper.setErrorEnabled(true);
                qtyWrapper.setError(getResources().getString(R.string.mustnotzero));
            }
        }else{
            qtyWrapper.setErrorEnabled(true);
            qtyWrapper.setError(getResources().getString(R.string.enterquantity));
        }
    }

    private void getIntentExtras(){

        Bundle extras = getIntent().getExtras();
        item_id = extras.getString("item");
        description.setText(extras.getString("description"));
        stock.setText(String.valueOf(extras.getInt("stock")));
        unit.setText(extras.getString("unit"));


        Glide.with(this)
                .load(extras.getString("category").equals("medicine") ? Global.createAvatarMedicine(this) : Global.createAvatarSupply(this))
                .apply(RequestOptions.circleCropTransform())
                .into(avatar);

        if(extras.getInt("stock") == 0){
            request.setVisibility(View.VISIBLE);
            disableFocusControls();
            disableControls();
        }
    }
    
    private void disableControls(){
        qty.setEnabled(false);
        remarks.setEnabled(false);
        deduct.setEnabled(false);
    }

    private void enableControls(){
        qty.setEnabled(true);
        remarks.setEnabled(true);
        deduct.setEnabled(true);
    }

    private void disableFocusControls(){
        qty.setFocusable(false);
        remarks.setFocusable(false);
    }

    private void showSnackBar(String err_msg){

       Snackbar snackbar =  Snackbar.make(findViewById(android.R.id.content), err_msg, Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorSnackBarError));
                snackbar.show();
    }

    private void proceedDeduction(){
        
        disableControls();
        deductionProgess.setVisibility(View.VISIBLE);

        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.deductStocks(sf.getUserId(), item_id, sf.getRecipientId(), remarks.getText().toString(), Integer.valueOf(qty.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DeductionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(DeductionResponse response) {
                        enableControls();
                        deductionProgess.setVisibility(View.GONE);

                        if(response.getSuccess() == 1) {
                            stock.setText(response.getRemaining() + "");
                            EventBus.getDefault().post("refreshAdapter");
                        }else{
                            qtyWrapper.setErrorEnabled(true);
                            qtyWrapper.setError(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        enableControls();
                        deductionProgess.setVisibility(View.GONE);
                        String err_msg = Global.getResponseCode(e);
                        showSnackBar(err_msg);
                    }
                });
    }

    private void showRequisition(){
        FragmentRequisition fragmentRequisition = FragmentRequisition.getInstance();

        Bundle bundle = new Bundle();
        bundle.putString("stock_desc", description.getText().toString());
        fragmentRequisition.setArguments(bundle);

        fragmentRequisition.show(getSupportFragmentManager(), "request_stock");
    }

}
