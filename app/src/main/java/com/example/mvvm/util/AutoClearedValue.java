package com.example.mvvm.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/*
A value holder que automaticamente nos limplia las referencias si una vista de fragment
es destruida
 */
public class AutoClearedValue<T> {
    private T value;
    public AutoClearedValue(Fragment fragment, T value) {
        FragmentManager fragmentManager = fragment.getFragmentManager();
        /*
        Este callback escuga los eventos del ciclo de vida del fragmen.
        Asi que cuando salta el FragmentViewDestroyed lo limpliamos.
         */
        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        if (f == fragment) {
                            AutoClearedValue.this.value = null;
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                        }
                    }
                },false);
        this.value = value;
    }

    public T get() {
        return value;
    }
}
