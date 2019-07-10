package com.example.mvvm.di;

import com.example.mvvm.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/*
We can use @ContributesAndroidInjector para generar nuestros potenciales clientes de Dagger.
Es decir aqui decimos que estos Fragment o Activities van a poder ser injectados con cualquier depedencia.
Antes con dagger había que hacer mucho más codigo, pero ahora es todo más sencillo.
 */

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();
}
