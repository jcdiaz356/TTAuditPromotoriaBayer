package com.dataservicios.ttauditpromotoriabayer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by usuario on 11/02/2015.
 */
public class ConexionInternet {
    // Context
    private Context _context;
    public ConexionInternet(Context mContext){
        this._context = mContext;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)  _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
