package com.tigerphp.sixscreensdemo.sixscreensdemo.app;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by luckycharm on 7/8/18.
 */

public class ImageLoader {
    public static void loadCircleImage(Context mContext, String file, ImageView imageView, int placeHolder) {

        Glide.with(mContext.getApplicationContext())
                .load(file)
                .asBitmap()
                .centerCrop()
                .transform(new CropCircleTransformation(mContext))
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(imageView);
    }

    public static void loadSimpleImage(Context mContext, File ImageUrl, ImageView imageView, int placeHolder, int dimens) {
        Glide.with(mContext.getApplicationContext())
                .load(ImageUrl)
                .asBitmap()
                .centerCrop()
                .placeholder(placeHolder)
                .error(placeHolder)
                .override(dimens, dimens)
                .into(imageView);
    }

    public static void loadSimpleImageWithoutDimens(Context mContext, File ImageUrl, ImageView imageView, int placeHolder) {
        Glide.with(mContext.getApplicationContext())
                .load(ImageUrl)
                .asBitmap()
                .centerCrop()
                .placeholder(placeHolder)
                .error(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }
}
