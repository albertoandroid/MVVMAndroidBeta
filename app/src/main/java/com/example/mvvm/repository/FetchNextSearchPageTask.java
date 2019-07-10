package com.example.mvvm.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm.api.ApiResponse;
import com.example.mvvm.api.RepoSearchResponse;
import com.example.mvvm.api.WebServiceApi;
import com.example.mvvm.db.GitHubDb;
import com.example.mvvm.model.RepoSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/*
Una tarea que lee el resultado de "Search" en la base de datos y recupera la pagina siguiente
si es que la hay.
 */
public class FetchNextSearchPageTask implements Runnable {
    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String query;
    private final WebServiceApi githubService;
    private final GitHubDb db;

    FetchNextSearchPageTask(String query, WebServiceApi githubService, GitHubDb db) {
        this.query = query;
        this.githubService = githubService;
        this.db = db;
    }

    @Override
    public void run() {
        RepoSearchResult current = db.repoDao().findSearchResult(query);
        if(current == null) {
            liveData.postValue(null);
            return;
        }
        final Integer nextPage = current.next;
        if (nextPage == null) {
            liveData.postValue(Resource.success(false));
            return;
        }
        try {
            Response<RepoSearchResponse> response = githubService
                    .searchRepos(query, nextPage).execute();
            ApiResponse<RepoSearchResponse> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                // we merge all repo ids into 1 list so that it is easier to fetch the result list.
                List<Integer> ids = new ArrayList<>();
                ids.addAll(current.repoIds);
                //noinspection ConstantConditions
                ids.addAll(apiResponse.body.getRepoIds());
                RepoSearchResult merged = new RepoSearchResult(query, ids,
                        apiResponse.body.getTotal(), apiResponse.getNextPage());
                try {
                    db.beginTransaction();
                    db.repoDao().insert(merged);
                    db.repoDao().insertRepos(apiResponse.body.getItems());
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                liveData.postValue(Resource.success(apiResponse.getNextPage() != null));
            } else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, true));
            }
        } catch (IOException e) {
            liveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    LiveData<Resource<Boolean>> getLiveData() {
        return liveData;
    }
}
