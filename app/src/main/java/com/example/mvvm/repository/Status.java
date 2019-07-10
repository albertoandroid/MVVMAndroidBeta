package com.example.mvvm.repository;

/*
Estado de un recurso que se proporciona a la interfaz de usuario.
Es decir que en este caso caundo vamos a solicitar información a través del repositorio,
queremos saber si el estado ha sido correcto, error o si esta en estado de carga, por ejemplo
para mostrar un spinner que esta cargando.
Es decir nos muestra el estado de carga.
 */

public enum Status {
    SUCCESS,
    ERROR,
    LOADING
}
