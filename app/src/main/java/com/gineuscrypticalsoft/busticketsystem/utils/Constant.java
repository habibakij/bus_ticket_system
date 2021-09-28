package com.gineuscrypticalsoft.busticketsystem.utils;

import android.content.Context;
import com.kaopiz.kprogresshud.KProgressHUD;

public class Constant {

    public static void progressBar(Context context){
        KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("loading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }
}
