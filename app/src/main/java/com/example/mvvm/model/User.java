package com.example.mvvm.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

/*
Recordamos que Room nos permite crear un tabla dentro de la base de datos
con las clases a las que ponemos la etiqueta entity.

Cada entidad ha de tener al menos una primary key.
 */
@Entity(primaryKeys = "login")
public class User {
    @SerializedName("login")
    @NonNull
    public final String login;
    @SerializedName("avatar_url")
    public final String avatarUrl;
    @SerializedName("name")
    public final String name;
    @SerializedName("company")
    public final String company;
    @SerializedName("repos_url")
    public final String reposUrl;
    @SerializedName("blog")
    public final String blog;

    public User(String login, String avatarUrl, String name, String company,
                String reposUrl, String blog) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.company = company;
        this.reposUrl = reposUrl;
        this.blog = blog;
    }
}
