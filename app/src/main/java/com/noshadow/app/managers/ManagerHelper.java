package com.noshadow.app.managers;


import android.content.Context;

public class ManagerHelper {

    private static ManagerHelper _instance;

    private NetworkJobManager _networkJobManager;
    private ConfigurationManager _configManager;
    private BluetoothManager _bluetoothManager;
    private NetworkManager _networkManager;

    private boolean _initialized = false;
    private String _appName = "NOSHADOW.TCC";

    private ManagerHelper() {

    }

    public static ManagerHelper getInstance() {
        if (_instance == null)
            _instance = new ManagerHelper();
        return _instance;
    }

    public void initialize(Context context, String appName) {

        if (!_initialized) {
            _initialized = true;
            _appName = appName;
            _configManager = new ConfigurationManager(context, _appName);
            _networkJobManager = new NetworkJobManager(context);
            _bluetoothManager = new BluetoothManager(context);
            _networkManager = new NetworkManager(context);
        }
    }

    public ConfigurationManager getConfigManager() {
        return _configManager;
    }

    public String getBaseUrl() {
        return "https://noshadow-api-dev.azurewebsites.net/";
    }

    public boolean isInitialized() {
        return _initialized;
    }

    public NetworkJobManager getNetworkJobManager() {
        return _networkJobManager;
    }

    public BluetoothManager getBluetoothManager() {
        return _bluetoothManager;
    }

    public NetworkManager getNetworkManager() {
        return _networkManager;
    }
}