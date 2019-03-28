package com.mcho.recipient.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.mcho.recipient.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

public interface Global {
     static Drawable createAvatarSupply(Context context){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor (context.getResources().getColor(R.color.colorSupply));
        return  drawable;
    }

     static Drawable createAvatarMedicine(Context context){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor (context.getResources().getColor(R.color.colorMedicine));
        return  drawable;
    }

     static String getResponseCode(Throwable e){
        String error_msg = "";
        //Server error
        if(e instanceof HttpException) {
            error_msg =  "Server error encountered";
            //Timeout Error
        }else if(e instanceof SocketTimeoutException){
            error_msg = "Connection timeout reached";
            //No internet
        }else if(e instanceof UnknownHostException){
            error_msg = "Internet connection required";
            //SSL Handshake error
        }else if(e instanceof SSLHandshakeException){
            error_msg = "Server connection error";
        }else if(e instanceof ConnectException){
            error_msg = "Cannot connect to the server";
        }

        return error_msg;
    }
}
