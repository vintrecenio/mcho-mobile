package com.mcho.recipient;

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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mcho.recipient.model.LoginResponse;
import com.mcho.recipient.network.ApiService;
import com.mcho.recipient.network.ServiceGenerator;
import com.mcho.recipient.utils.Global;
import com.mcho.recipient.utils.SharedPrefs;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.usernameWrapper) TextInputLayout usernameWrapper;
    @BindView(R.id.passwordWrapper) TextInputLayout passwordWrapper;
    @BindView(R.id.username) TextInputEditText username;
    @BindView(R.id.password) TextInputEditText password;
    @BindView(R.id.login) Button login;
    @BindView(R.id.progressLogin) ProgressBar progressBar;

    private ApiService apiService;
    private SharedPrefs sf;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        sf = new SharedPrefs(this);
        disposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @OnClick(R.id.login)
    void onLoginClick(){
        validateInputs();
    }

    @OnTextChanged(R.id.username)
    void changeUsername(){
        if(!username.getText().toString().isEmpty()) {
            int unameLength = username.getText().length();
            if (unameLength > 0) {
                usernameWrapper.setErrorEnabled(false);
            }else{
                usernameWrapper.setErrorEnabled(true);
                usernameWrapper.setError(getResources().getString(R.string.usernameerror));
            }
        }else{
            usernameWrapper.setErrorEnabled(true);
            usernameWrapper.setError(getResources().getString(R.string.usernameerror));
        }
    }

    @OnTextChanged(R.id.password)
    void changePassword(){
        if(!password.getText().toString().isEmpty()) {
            int pwLength = password.getText().length();
            if (pwLength > 0) {
                passwordWrapper.setErrorEnabled(false);
            }else{
                passwordWrapper.setErrorEnabled(true);
                passwordWrapper.setError(getResources().getString(R.string.passworderror));
            }
        }else{
            passwordWrapper.setErrorEnabled(true);
            passwordWrapper.setError(getResources().getString(R.string.passworderror));
        }
    }

    private void validateInputs(){
        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();

        //Validate username first
        if(usernameStr.isEmpty() && passwordStr.isEmpty()) {
            usernameWrapper.setErrorEnabled(true);
            usernameWrapper.setError(getResources().getString(R.string.usernameerror));
            passwordWrapper.setErrorEnabled(true);
            passwordWrapper.setError(getResources().getString(R.string.passworderror));
        }else if(usernameStr.isEmpty()) {
            usernameWrapper.setErrorEnabled(true);
            usernameWrapper.setError(getResources().getString(R.string.usernameerror));
        }else if(passwordStr.isEmpty()) {
            passwordWrapper.setErrorEnabled(true);
            passwordWrapper.setError(getResources().getString(R.string.passworderror));
        }else{
            proceedLogin(usernameStr, passwordStr);
        }
    }

    private void proceedLogin(String username, String password){

        progressBar.setVisibility(View.VISIBLE);

        apiService = ServiceGenerator.createService(ApiService.class);

        apiService.loginCredential(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(LoginResponse response) {
                        progressBar.setVisibility(View.GONE);
                        if(response.getSuccess() == 1) {
                            sf.setRecipientId(response.getRecipientId());
                            sf.setUserId(response.getUser());
                            sf.setToken(response.getToken());

                            showSnackBar(response.getMsg(), R.color.colorSnackBarSuccess);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToMain();
                                }
                            }, 1000);

                        }else{
                            showSnackBar(response.getMsg(), R.color.colorSnackBarError);
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

    private void navigateToMain(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
