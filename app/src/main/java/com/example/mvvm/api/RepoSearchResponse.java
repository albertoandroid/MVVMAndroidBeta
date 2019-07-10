package com.example.mvvm.api;

import androidx.annotation.NonNull;

import com.example.mvvm.model.Repo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
Creamos este Pojo para mantener Las respuesta del RepoSearch en nuestras busquedas.
Por ello esta clase no es una entidad de nuestra base de datos,
puesto que no vamos a guardarla de esta manera.
 */
public class RepoSearchResponse {
    @SerializedName("total_count")
    private int total;
    @SerializedName("items")
    private List<Repo> items;
    private Integer nextPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    @NonNull
    public List<Integer> getRepoIds() {
        List<Integer> repoIds = new ArrayList<>();
        for (Repo repo : items) {
            repoIds.add(repo.id);
        }
        return repoIds;
    }
}
