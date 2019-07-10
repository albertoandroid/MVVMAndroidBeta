package com.example.mvvm.di;

import android.app.Application;

import com.example.mvvm.GithubApp;
import com.example.mvvm.api.WebServiceApi;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


/*
El componete es el punto entre los modulos que hemos crado y la parte
del codigo que solicita dichos objetos para hacer la inyección de
depenencias. Asi que primero le indicamos lo modulos que va a tratar.
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class
})
public interface AppComponent {
    /*
    Un constructor para un componente.
    Los componentes pudente tener una interza anotado con ComponentBuilder.
    En este caso es posible vincular alguna instancia al componente.
    En este caso queremos una instancia de Application.

     */
    @Component.Builder
    interface Builder {
        /*
        BinsInstance marca un metodo en un generador de compoonetes
        que nos permite añadir una instancia al componente.
         */
        @BindsInstance
        Builder application(Application application);
        AppComponent build();

        //Asi que cuando creemo nuestro componente de Dagger, le vamos a pasar
        //una instancia de la aplicación
    }

    /*
    Indicamos donde lo vamos a inyectar en este caso en nuestra GithubApp
    y luego vamos a appInyector y añadimos esto
    DaggerAppComponent.builder().application(githubApp)
                .build().inject(githubApp);
     */
    void inject(GithubApp githubApp);
}
