package com.example.mvvm.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/*
La clase GithubViewModelFactory basicamente nos ayuda a crear de forma dinamica los viewmodels
de nuestras activities o fragment.

Es decir que la Fabrica nos va a ayudar a implementar como crear los viewModel
Además podemos hacerlo a través de la inyección de dependencias. De esta manera, transferimos
la responsabilidad de creador los viewmodel a una clase separada y por lo tanto nuestro codigo
esta más desacoplado y seguimos el principio de responsabilidad unica.
https://www.techyourchance.com/dependency-injection-viewmodel-with-dagger-2/

Es decir que nuestros ViewModel, RepoViewModel, SearchViewModel y UserViewModel serán creados a
través de ViewModelProvder que usa ViewModelProviderFactory para crear dichas instancias.
Esto se debe a que no se puede inyectar los viewmodel directamente y en su lugar debemos
crear una clase customFactory.

Este es un problema entre Dagger y ViewModel que ambos son productos de google, pero que para que funcionen
hay que hacerlo de esta manera. Que quizas sea una manera compleja pero vamos a ver su explicación.

 */

@Singleton
public class GithubViewModelFactory implements ViewModelProvider.Factory {
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;


    /*
    Dagger 2 pude asociar a un proveedor a una clave determinada
    e inyectarlo en un mapa.
    Esto se hace gracias a la anotación @IntoMap que produce el valor que
    queremos aosicar con una clave dada.
    Tambien vamos a necesitar un MapKey, que es el que nos va  a ofrecer
    la clave especifica.
     */


    @Inject
    public GithubViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }


    /*
    Ahora podemos usar el objeto Class que nos lleva con el metodo create(Class<T> modelClass)
    para recuperar un proveedor para ese ViewModel
    .
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //El posible provedor para el ViewModel dado lo obtenemos del mapa
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        /*
        Si nuestro mapa de Provedores no tiene esa clave especifica
        verficiamos si hay una subclase de ViewModel que nosotros
        debamos instanciar.
         */
        if (creator == null) {
            /*
            EntrySet se utiliza para crear un conjunto de los mismos elementos contenidos
            en el Map o almacenar un nuevo elemento en el mañ.
             */
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
                //isAssingalbeFrom sera verdadero cada vez que la clase modelClass sea
                //una superclase o interfaz de entry.getKey.
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        /*
        Si no hemos podido encontrar una subclse para obtener el proveedor
        valido, quiere decir que hemos fallado y por ello lanzamos una
        excepción de que no tenemos el viewmodel buscado.
         */
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        /*
        por último, vamos a decjar que dagger cree nuestro ViewModel
        invocado el método get en el objeto provedor, y lo casteamos
        con el tipo buscado.
         */
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
