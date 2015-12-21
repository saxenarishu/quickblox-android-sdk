package com.quickblox.sample.core.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.quickblox.sample.core.R;

import java.util.List;

public class ErrorUtils {

    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private ErrorUtils() {}

    public static void showErrorDialog(Context context, @StringRes int errorMessage, List<String> errors) {
        showErrorDialog(context, context.getString(errorMessage), errors);
    }

    private static void showErrorDialog(final Context context, final String errorMessage, final List<String> errors) {
        showErrorDialog(context, String.format("%s: %s", errorMessage, errors));
    }

    private static void showErrorDialog(final Context context, final String errorMessage) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.dlg_error)
                        .setMessage(errorMessage)
                        .create()
                        .show();
            }
        });
    }
}
