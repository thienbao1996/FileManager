package com.example.filemanagersimple.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class StorageHelper {
    public static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    public static void grantStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please grant our app access to all your files")
                        .setPositiveButton("Ok", (dialog, which) -> {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                                activity.startActivityForResult(intent, 201);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activity.startActivityForResult(intent, 201);
                            }
                        });
                builder.create().show();
            }
            Log.d("permission", Environment.isExternalStorageManager() + " ");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isExternalStorageWritable()) {
                Log.d("permission","Permission write is granted");
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }

            if (isExternalStorageReadable()) {
                Log.d("permission","Permission read is granted");
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission_group.STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("permission","Permission storage is granted");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please grant our app access to all your files in Permissions section")
                        .setPositiveButton("Ok", (dialog, which) -> {
                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                intent.setData(uri);
                                activity.startActivity(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                intent.setData(uri);
                                activity.startActivity(intent);
                            }
                        });
                builder.create().show();
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.d("permission","Permission is granted");

        }
    }
}
