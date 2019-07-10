package com.example.mvvm.di;

import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

/*

ViewModelKeys lo ayuda a asignar sus clases de ViewModel para que ViewModelFactory pueda proporcionarlos
/ inyectarlos correctamente. Aunque esto es un bit trivial, en Java, ViewModelKey se da como esto:

Tambien vamos a necesitar un MapKey, que es el que nos va  a ofrecer
    la clave especifica.
 */

/*
Nos vamos a crear la siguiente anotación
 */
@Documented // nos sirve para nuestro JavaDoc. Es decir para la documentación
//de nuestro código.
@Target({ElementType.METHOD}) //Target nos indica los lugares donde se puede aplicar
//esta anotación, en este caso en metodos, puede ser tambien para variables.
@Retention(RetentionPolicy.RUNTIME) // Le decimos que chequee en tiempo de ejecución.
@MapKey
@interface ViewModelKey {
    Class<? extends ViewModel> value();
}
