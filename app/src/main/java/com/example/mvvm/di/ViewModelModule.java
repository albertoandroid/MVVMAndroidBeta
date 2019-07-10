package com.example.mvvm.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvm.ui.repo.RepoViewModel;
import com.example.mvvm.ui.search.SearchViewModel;
import com.example.mvvm.ui.user.UserViewModel;
import com.example.mvvm.viewmodel.GithubViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
/*
Recordamos que los modulos se encargan de proveer las instancias que necesitamos,
en este caso las instancias de ViewModel.
 */
@Module
abstract class ViewModelModule {


    /*
    Aqui le decimos que inyecte este objeto en un mapa a través de @IntoMap
    En este caso la clave será UserViewModel.class que es a través @ViewModelKey
    Y le decimos en este caso caso que vamos a proveer un objeto de tipo
    UserviewModel a través de la etiqueta Bind.

     */
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(RepoViewModel.class)
    abstract ViewModel bindRepoViewModel(RepoViewModel repoViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(GithubViewModelFactory factory);
}

