package com.example.mvvm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.mvvm.ui.common.NavigationController;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


import javax.inject.Inject;

/*
1.- Hacemos una presentación de la App. Y como hay que comenzar por algo, pues comenzamos por
la api que nos dará una visión global de que peticiones vamos a realizar y que modelo de datos vamos
a necesitar para nuestra aplicación.xxxxxxxxx--------xxxxxxxxxx
2.- WebServiceApi xxxxxxxxx--------xxxxxxxxxx
3.- User xxxxxxxxx--------xxxxxxxxxx
4.- ApiResponse xxxxxxxxx--------xxxxxxxxxx
5.- Repo  xxxxxxxxx--------xxxxxxxxxx
6.- Contributor xxxxxxxxx--------xxxxxxxxxx
7.- RepoSearchResponse xxxxxxxxx--------xxxxxxxxxx
8.- GithubTypeConverters xxxxxxxxx--------xxxxxxxxxx
9.- RepoSearchResponse xxxxxxxxx--------xxxxxxxxxx
10- UserDao xxxxxxxxx--------xxxxxxxxxx
11.- RepoDao xxxxxxxxx--------xxxxxxxxxx
12.- GitHubDb xxxxxxxxx--------xxxxxxxxxx
13.- AppExecutors xxxxxxxxx--------xxxxxxxxxx
14.- NetworkBoundResource xxxxxxxxx--------xxxxxxxxxx
15.- Resource xxxxxxxxx--------xxxxxxxxxx
16.- Status xxxxxxxxx--------xxxxxxxxxx
17.- UserRepository xxxxxxxxx--------xxxxxxxxxx
18.- RepoRepository xxxxxxxxx--------xxxxxxxxxx
19.- RateLimiter xxxxxxxxx--------xxxxxxxxxx
20.- FetchNextSearchPageTask xxxxxxxxx--------xxxxxxxxxx
21.- AbsentLiveData xxxxxxxxx--------xxxxxxxxxx

22.- NavigationController  99999999999999999999999
23.- Injectable xxxxxxxxx--------xxxxxxxxxx
24.- RepoFragment -> Vacio xxxxxxxxx--------xxxxxxxxxx
25.- RepoFragment -> Vacio xxxxxxxxx--------xxxxxxxxxx
26.- RepoFragment -> Vacio xxxxxxxxx--------xxxxxxxxxx
27.- UserViewModel xxxxxxxxx--------xxxxxxxxxx
28.- UserFragment 99999999999999999999999
29.- Añadir en Gradle que usamos databinding xxxxxxxxx--------xxxxxxxxxx
30.- FragmentDataBindingComponent xxxxxxxxx--------xxxxxxxxxx
31.- FragmentBindingAdapters
FragmentBindingAdapters -> posible error he quitado equtipa overrride xxxxxxxxx--------xxxxxxxxxx
32.- Fragment_User.xml xxxxxxxxx--------xxxxxxxxxx
33.- RetryCallback xxxxxxxxx--------xxxxxxxxxx
34.- dimens.xml xxxxxxxxx--------xxxxxxxxxx
35.- Strings.xml xxxxxxxxx--------xxxxxxxxxx
36.- Loading_state.xml xxxxxxxxx--------xxxxxxxxxx
37.- Repo_item.xml xxxxxxxxx--------xxxxxxxxxx
38.- AutoClearedValue xxxxxxxxx--------xxxxxxxxxx
39.- DataBoundViewHolder xxxxxxxxx--------xxxxxxxxxx
40.- DataBoundListAdapte xxxxxxxxx--------xxxxxxxxxx
41.- RepoListAdapter xxxxxxxxx--------xxxxxxxxxx
42.- SearchViewModel xxxxxxxxx--------xxxxxxxxxx
43.- SearchFragment
44.- Search_fragment.xml xxxxxxxxx--------xxxxxxxxxx
45.- MainActivity
46.- BindingAdapters xxxxxxxxx--------xxxxxxxxxx
46.a.-LiveDataCallAdapter xxxxxxxxx--------xxxxxxxxxx
47.b.-LiveDataCallAdapterFactory xxxxxxxxx--------xxxxxxxxxx
47.b.- Contributo_item.xm xxxxxxxxx--------xxxxxxxxxx
47.c.-ContributorAdapter xxxxxxxxx--------xxxxxxxxxx
48.d.-RepoViewModel xxxxxxxxx--------xxxxxxxxxx
47.e.-RepoFragment.xml xxxxxxxxx--------xxxxxxxxxx
//Empezamos Dagger
47.f.GithubApp xxxxxxxxx--------xxxxxxxxxx
47.- MainActivityModule xxxxxxxxx--------xxxxxxxxxx
48.- FragmentBuildersModule xxxxxxxxx--------xxxxxxxxxx
49.- GithubViewModelFactory xxxxxxxxx--------xxxxxxxxxx
50.- ViewModelKey xxxxxxxxx--------xxxxxxxxxx
51.- AppModule xxxxxxxxx--------xxxxxxxxxx
52.- AppComponentxxxxxxxxx--------xxxxxxxxxx
53.- ViewModelModule xxxxxxxxx--------xxxxxxxxxx



51.- AppInjector

 */


/*
HasSupportFragmentInjector -> Como vamos a inyectar en nuestro fragments, tenemos que implementar
la interfaz HasSupportFrgamenteInjector.

Esto sigue una regla, nuestra clase GitHubApp tiene que implements HasActivityInjector
(el inyector de activities)
y la activity tiene que implementar  HasSupportFragmentInjector
(el inyector de los frgaments)

Los injector son la solución que nos da dagger para inyectar de forma facil
en nuestras activities o fragments.
 */
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    /*
    DispachinAndroidInjector es los que finalmente nos va a permitir inyectar nuestras framework class
    es decir las activities, fragmentes o servicios.
    Para este caso los frgaments, igual que en la github app nos lo hace para
    la activity
    Nuestra aplicación GithubApp nos proporciona a través de la interfaz HasActivityInjector
     */
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    NavigationController navigationController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            navigationController.navigateToSearch();
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
