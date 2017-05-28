package com.dataservicios.ttauditpromotoriabayer.app;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.dataservicios.ttauditpromotoriabayer.Services.MonitoGPSServices;
import com.dataservicios.ttauditpromotoriabayer.Services.UpdateServices;
import com.dataservicios.ttauditpromotoriabayer.util.LruBitmapCache;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private boolean serviceRunningFlag;

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(TAG, "onCreated");
		startService(new Intent(this, UpdateServices.class));
		startService(new Intent(this, MonitoGPSServices.class));
		mInstance = this;
	}


	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public void setServiceRunningFlag(boolean serviceRunningFlag) {
		this.serviceRunningFlag = serviceRunningFlag;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminated");
		stopService(new Intent(this, UpdateServices.class));
		stopService(new Intent(this, MonitoGPSServices.class));
	}
}