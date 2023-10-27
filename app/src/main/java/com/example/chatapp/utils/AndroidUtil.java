package com.example.chatapp.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

//SETAR IMAGEM
public class AndroidUtil {

    public static  void showToast(Context context,String message){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }



}
