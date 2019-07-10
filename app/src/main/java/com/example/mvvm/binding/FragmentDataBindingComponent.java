package com.example.mvvm.binding;

import androidx.databinding.DataBindingComponent;
import androidx.fragment.app.Fragment;
/*

Si vamos a utilizar Dagger 2 como es nuestro caso. Debemos crearnos una clase
que implemente de DataBindingComponent en el que podamos generar los getter de
nuestra clase FragmentBindingAdapter.
Implementación de un componente databingin para fragment


 */

public class FragmentDataBindingComponent implements DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    public FragmentBindingAdapters getFragmentBindingAdapters() {

        return adapter;
    }


}

