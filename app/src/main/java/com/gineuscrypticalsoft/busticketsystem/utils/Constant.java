package com.gineuscrypticalsoft.busticketsystem.utils;

import android.content.Context;
import com.kaopiz.kprogresshud.KProgressHUD;

public class Constant {

    public static final String[] CITY_LIST = new String[] {"Dhaka", "Faridpur", "Gazipur", "Gopalganj", "Kishoreganj", "Madaripur", "Manikganj", "Munshiganj", "Narayanganj", "Narsingdi", "Rajbari",
            "Shariatpur", "Tangail", "Bagerhat", "Chuadanga", "Jessore", "Jhenaidah", "Khulna", "Kushtia", "Magura", "Meherpur", "Narail", "Satkhira", "Jamalpur",
            "Mymensingh", "Netrokona", "Sherpur", "Bogra", "Joypurhat", "Naogaon", "Natore", "Chapainawabganj", "Pabna", "Rajshahi", "Sirajganj", "Dinajpur",
            "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari", "Panchagarh", "Rangpur", "Thakurgaon", "Habiganj", "Moulvibazar", "Sunamganj", "Sylhet",
            "Barguna", "Barisal", "Bhola", "Jhalokati", "Patuakhali", "Pirojpur", "Bandarban", "Brahmanbaria", "Chandpur", "Chittagong", "Comilla", "Cox's Bazar",
            "Feni", "Khagrachhari", "Lakshmipur", "Noakhali", "Rangamati"};

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
