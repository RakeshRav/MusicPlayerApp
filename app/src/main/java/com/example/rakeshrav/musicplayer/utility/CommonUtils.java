/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.rakeshrav.musicplayer.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.rakeshrav.musicplayer.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rao on .
 */

public final class CommonUtils {

    private static final String TAG = "CommonUtils";
    private static boolean dismiss = false;

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Activity context) {


        ProgressDialog progressDialog = new ProgressDialog(context, R.style.Theme_AppCompat_Translucent);

        progressDialog.show();
//            WindowManager.LayoutParams layoutParams = progressDialog.getWindow().getAttributes();
//            layoutParams.dimAmount = 0.7f;
//            progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.layout_indicator_whole_color);

        return progressDialog;
    }

    public static ProgressDialog showCustomDialog(Activity context,
                                                  @LayoutRes int layoutRes) {

        ProgressDialog progressDialog = new ProgressDialog(context, R.style.Theme_AppCompat_ERR);
        progressDialog.show();
        progressDialog.setContentView(layoutRes);

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;

    }

    private static void animateLoader(final ImageView imageView) {

        if (dismiss) {
            return;
        }

        imageView.animate()
                .rotationBy(360)
                .setDuration(1000)
                .setInterpolator(new LinearInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        if (dismiss) {
                            return;
                        }

                        animateLoader(imageView);
                    }
                }).start();
    }


    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {

        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }

    public static String getFormatDate(String dateStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.d(TAG, "except " + e.getMessage());
            e.printStackTrace();
        }

        long millis = date.getTime();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
        return sdf1.format(new Date(millis));

    }

    public static String getDayOfWeek(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.d(TAG, "except " + e.getMessage());
            e.printStackTrace();
            date = new Date();
        }
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

        return simpleDateformat.format(date);
    }


    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty()) {
            return false;
        }

        return true;
    }
}
