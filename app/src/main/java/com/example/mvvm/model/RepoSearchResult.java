package com.example.mvvm.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.mvvm.db.GithubTypeConverters;

import java.util.List;
/*
Type Converter es una etiqueta muy util en Room que nos permite
guardar datos personalizados en nuestra base de datos.
Es decir se convierte un tipo desconocido, en este ejemplo una lista
que integer que no so podría guardar en la base de datos,
en un tipo conocido para la base de datos.
Para ello tendremos que crear la clase GithubTypeConverters
 */
@Entity(primaryKeys = {"query"})
@TypeConverters(GithubTypeConverters.class)
public class RepoSearchResult {
    @NonNull
    public final String query;
    public final List<Integer> repoIds;
    public final int totalCount;
    @Nullable
    public final Integer next;

    public RepoSearchResult(@NonNull String query, List<Integer> repoIds, int totalCount,
                            @Nullable Integer next) {
        this.query = query;
        this.repoIds = repoIds;
        this.totalCount = totalCount;
        this.next = next;
    }
}

