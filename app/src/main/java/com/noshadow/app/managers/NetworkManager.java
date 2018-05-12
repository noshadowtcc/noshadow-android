package com.noshadow.app.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.noshadow.app.api.ApiFactory;

/**
 * Created by Liga on 27/06/2017.
 */

public class NetworkManager extends BaseManager {

    public NetworkManager(Context context) {
        _context = context;
    }

    public <T> T createRetrofitService(final Class<T> clazz, final String baseUrl) {
        return ApiFactory.createRetrofitService(clazz, baseUrl);
    }

	public <T> T createRetrofitService(final Class<T> clazz, final String baseUrl, Integer timeoutSeconds) {
		return ApiFactory.createRetrofitService(clazz, baseUrl, timeoutSeconds);
	}

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
