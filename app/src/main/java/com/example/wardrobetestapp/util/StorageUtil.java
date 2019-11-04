package com.example.wardrobetestapp.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Nadimuddin on 4/11/19.
 */
public class StorageUtil {

    public static String getPublicDirectory()
    {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath();
    }

    public static File createTemporaryFile()throws Exception
    {
        File tempFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        tempFile = new File(tempFile.getAbsolutePath()+"/temp", "Picture.png");
        if(!tempFile.exists())
            tempFile.mkdirs();
        else if(tempFile.exists())
            tempFile.delete();
        tempFile.createNewFile();

        return tempFile;
    }

    public static Uri getImageUri(Context context, Bitmap inImage) {
        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);*/
        //Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String path = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}
