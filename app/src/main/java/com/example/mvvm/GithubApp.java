package com.example.mvvm;

import android.app.Activity;
import android.app.Application;

import com.example.mvvm.di.AppInjector;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/*
Como nuestra clase Application tiene activities, en este caso MainActivity
debemos implementar HasActivityInjector que nos proporciona AndroidInjector
En este caso debemos inyectar Dispachin Android injecotr que significa que nos va
a devolver ActivityInyector y por ello podmeos usar AndroidInjection.Inject
en las activities.
 */
public class GithubApp extends Application implements HasActivityInjector {

    /*
    Nos va a realizar la inyección en la activity.
     */
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
           // Timber.plant(new Timber.DebugTree());
        }
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
