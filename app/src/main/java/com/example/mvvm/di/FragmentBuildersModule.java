package com.example.mvvm.di;

/*
We can use @ContributesAndroidInjector para generar nuestros potenciales clientes de Dagger.
Es decir aqui decimos que estos Fragment van a poder ser injectados con cualquier depedencia.
Antes con dagger había que hacer mucho más codigo, pero ahora es todo más sencillo.
 */

import com.example.mvvm.ui.repo.RepoFragment;
import com.example.mvvm.ui.search.SearchFragment;
import com.example.mvvm.ui.user.UserFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract RepoFragment contributeRepoFragment();

    @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
