package com.example.mvvm.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.google.gson.annotations.SerializedName;

/*
añadir la etiqueta forignkey significa que nosotros creamos una
conexión entre esta entidad y alguna otra. En este caso la parenColumn
es la entidad padre con la que esta relacionada y la childçcoilumna
es la de esta entidad.
 */

@Entity(primaryKeys = {"repoName", "repoOwner", "login"},
        foreignKeys = @ForeignKey(entity = Repo.class,
                parentColumns = {"name", "owner_login"},
                childColumns = {"repoName", "repoOwner"},
                onUpdate = ForeignKey.CASCADE,  //( Cacade propaga la operación de actualización en la clave padre a las clases hijas dependientes.
                deferred = true))
public class Contributor {

    @SerializedName("login")
    @NonNull
    private final String login;

    @SerializedName("contributions")
    private final int contributions;

    @SerializedName("avatar_url")
    private final String avatarUrl;

    @NonNull
    private String repoName;

    @NonNull
    private String repoOwner;

    public Contributor(String login, int contributions, String avatarUrl) {
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public String getLogin() {
        return login;
    }

    public int getContributions() {
        return contributions;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoOwner() {
        return repoOwner;
    }
}

