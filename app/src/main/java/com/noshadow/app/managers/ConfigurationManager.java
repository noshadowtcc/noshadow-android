package com.noshadow.app.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Liga on 27/06/2017.
 */

public class ConfigurationManager extends BaseManager {

    private final Gson _gson = new Gson();
    private SharedPreferences _authPreferences;
    private SharedPreferences _configPreferences;

    public ConfigurationManager(Context context, String appName) {
        _context = context;
        _appName = appName;

        _authPreferences = _context.getSharedPreferences(_appName + ".PREFERENCE_AUTH_FILE_KEY",
                Context.MODE_PRIVATE);

        _configPreferences = _context.getSharedPreferences(_appName + ".PREFERENCE_CONFIG_FILE_KEY",
                Context.MODE_PRIVATE);
    }

    public boolean isAuthenticate() {

        String token = _authPreferences.getString(_appName + ".PREFERENCE_USER_KEY", "");

        boolean isAuthenticated = token != "";

        return isAuthenticated;
    }

    public void saveLog(String log){
        String l = _authPreferences.getString(_appName + ".PREFERENCE_LOG", "");

        l += "\n " + log;

        setAuthConfig(_appName + ".PREFERENCE_LOG", l);
    }

    public Map<String, String> getConfig() {

        String configs = _configPreferences.getString(_appName + ".PREFERENCE_CONFIG_KEY", "");

        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        return _gson.fromJson(configs, type);
    }

    public boolean needGetConfig() {

        String configs = _configPreferences.getString(_appName + ".PREFERENCE_CONFIG_KEY", "");
        return TextUtils.isEmpty(configs);
    }

    public void setAuthConfig(String key, Object value) {

        SharedPreferences.Editor editor = _authPreferences.edit();
        editor.putString(key, _gson.toJson(value));
        editor.commit();
    }

    public void setAuthConfig(String key, String value) {

        SharedPreferences.Editor editor = _authPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setAuthConfig(String key, boolean value) {

        SharedPreferences.Editor editor = _authPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setAuthConfig(String key, int value) {
        SharedPreferences.Editor editor = _authPreferences.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public void setConfig(String key, Object value) {
        SharedPreferences.Editor editor = _configPreferences.edit();

        editor.putString(key, _gson.toJson(value));
        editor.apply();
    }

    public void setConfig(String key, String value) {
        SharedPreferences.Editor editor = _configPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

	public void setConfig(@StringRes int key, String value) {
		setConfig( getString(key), value );
	}

    public void setConfig(String key, boolean value) {
        SharedPreferences.Editor editor = _configPreferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setConfig(String key, int value) {
        SharedPreferences.Editor editor = _configPreferences.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public String getConfigString(@StringRes int key) {
        return _configPreferences.getString(getString(key), "");
    }

    public String getConfigString(String key) {
        return _configPreferences.getString(key, "");
    }

    public int getConfigInt(String key) {
        return _configPreferences.getInt(key, -1);
    }

    public void removeKey(String key)
    {
	    _configPreferences.edit().remove(key).apply();
    }

	public void removeKey(@StringRes int key)
	{
		_configPreferences.edit().remove( getString(key) ).apply();
	}
}

