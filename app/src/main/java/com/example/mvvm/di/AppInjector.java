package com.example.mvvm.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.mvvm.GithubApp;
import com.example.mvvm.api.WebServiceApi;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Helper class to automatically inject fragments if they implement {@link Injectable}.
 */
public class AppInjector {
    private AppInjector() {}
    public static void init(GithubApp githubApp) {
        DaggerAppComponent.builder().application(githubApp)
                .build().inject(githubApp);
        githubApp
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        handleActivity(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
    }

    private static void handleActivity(Activity activity) {
        if (activity instanceof HasSupportFragmentInjector) {
            /*
            Utilizamos el meotod androidinjection.inject para todas las activities
            que queramos usar dagger en este caso activity.
             */
            AndroidInjection.inject(activity);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            new FragmentManager.FragmentLifecycleCallbacks() {
                                @Override
                                public void onFragmentCreated(FragmentManager fm, Fragment f,
                                                              Bundle savedInstanceState) {
                                    if (f instanceof Injectable) {
                                        /*
                                        Uitilizamos este metodo para todos los fragments
                                        en los que quermos usar dagger.
                                         */
                                        AndroidSupportInjection.inject(f);
                                    }
                                }
                            }, true);
        }
    }
}
