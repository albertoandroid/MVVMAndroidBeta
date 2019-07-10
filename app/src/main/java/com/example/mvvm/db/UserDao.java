package com.example.mvvm.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvm.model.User;
/*
Marcamos la clase como Objeto de Acceso a Datos Data Access Object.
Esta es nuestra clase principal que definie las interacciones con la base de datos.
En este caso con la Tabla User.
Podemos hacer todo tipo de metodos, Insert, Query, Delete, Update.
En este caso solo necesitamos Insert y Query.
 */
@Dao
public interface UserDao {
    //onConflictStrategy replaza los datos antiguos si existen y continua con la transaccion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM user WHERE login = :login")
    LiveData<User> findByLogin(String login);
}
