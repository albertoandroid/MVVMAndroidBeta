package com.example.mvvm.model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

/*
Añadir un Index a nuestra entity nos ofrece más velocidad en las
consultas de seleccion, es decir en la Select Queries, pero nos
relentiza las consultas de insercción o actualización.
Por lo tanto se ha de hacer con ojo, en este caso, se
ha tomado estos dos parametros como index.
 */

@Entity(indices = {@Index("id"), @Index("owner_login")},
        primaryKeys = {"name", "owner_login"})
public class Repo {
    public static final int UNKNOWN_ID = -1;
    public final int id;
    @SerializedName("name")
    @NonNull
    public final String name;
    @SerializedName("full_name")
    public final String fullName;
    @SerializedName("description")
    public final String description;
    @SerializedName("stargazers_count")
    public final int stars;
    @SerializedName("owner")
    //Se utiliza la etiqueta @Enbedded para anteponer un prefijo a
    // la columna de los campos incrustados. Es decir la columan
    // login de owner sera owner_login y onwner_url
    @Embedded(prefix = "owner_")
    @NonNull
    public final Owner owner;

    public Repo(int id, String name, String fullName, String description, Owner owner, int stars) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.owner = owner;
        this.stars = stars;
    }

    public static class Owner {
        @SerializedName("login")
        @NonNull
        public final String login;
        @SerializedName("url")
        public final String url;

        public Owner(@NonNull String login, String url) {
            this.login = login;
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Owner owner = (Owner) o;

            if (login != null ? !login.equals(owner.login) : owner.login != null) {
                return false;
            }
            return url != null ? url.equals(owner.url) : owner.url == null;
        }

        @Override
        public int hashCode() {
            int result = login != null ? login.hashCode() : 0;
            result = 31 * result + (url != null ? url.hashCode() : 0);
            return result;
        }
    }
}

