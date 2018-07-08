package com.tigerphp.sixscreensdemo.sixscreensdemo.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tigerphp.sixscreensdemo.sixscreensdemo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by luckycharm on 7/8/18.
 */

public class AppHelper {
    /**
     * method for get a custom CustomToast
     *
     * @param Message this is the second parameter for CustomToast  method
     */
    public static void CustomToast(Context mContext, String Message) {

        LinearLayout CustomToastLayout = new LinearLayout(mContext.getApplicationContext());
        CustomToastLayout.setBackgroundResource(R.drawable.bg_custom_toast);
        CustomToastLayout.setGravity(Gravity.TOP);
        TextView message = new TextView(mContext.getApplicationContext());
        message.setTextColor(Color.WHITE);
        message.setTextSize(13);
        message.setPadding(20, 20, 20, 20);
        message.setGravity(Gravity.CENTER);
        message.setText(Message);
        CustomToastLayout.addView(message);
        Toast toast = new Toast(mContext.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(CustomToastLayout);
        toast.setGravity(Gravity.CENTER, 0, 50);
        toast.show();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getFilePath(Context context, Uri _uri){
        try {
            InputStream attachment = context.getContentResolver().openInputStream(_uri);
            if (attachment != null) {
                String filename = getContentName(context.getContentResolver(), _uri);
                if (filename != null) {

                    File file = new File(context.getCacheDir(), filename);
                    FileOutputStream tmp = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    while (attachment.read(buffer) > 0) {
                        tmp.write(buffer);
                    }
                    tmp.close();
                    attachment.close();
                    return file.getAbsolutePath();
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static String getContentName(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
        if (nameIndex >= 0) {
            String name = cursor.getString(nameIndex);
            cursor.close();
            return name;
        }
        return null;
    }
}
