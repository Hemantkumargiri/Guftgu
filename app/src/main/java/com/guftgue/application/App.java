package com.guftgue.application;

import android.app.Application;

import com.dorianmusaj.cryptolight.CryptoLight;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CryptoLight.init(this);
    }
}