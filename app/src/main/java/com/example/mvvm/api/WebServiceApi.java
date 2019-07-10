package com.example.mvvm.api;

import androidx.lifecycle.LiveData;

import com.example.mvvm.model.Contributor;
import com.example.mvvm.model.Repo;
import com.example.mvvm.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebServiceApi {

    /*
    Con retrofit podemos hacer nuestras conexiones HTTP de una forma sencilla a través de una interfaz,
    con todos los metodos que necesitamos.
    1.- Primero debemos anotar el metodo http que vamos a utilizar @GET, @POST, @PUT, @DELETE
    2.- Debemos anotar la ruta de acceso a nuestra api. El end-pint. Aunque puedes poner la ruta completa,
    lo ideal es excluir el dominio.
    3.- Cada función devuelve un objeto Call, que envuelve el objetio que queremos traernos con nuestro
    servicio. Es decir como viene la respuesta
    4.- Cada parametro de entrada tiene su propia anotación, para distinguir entre los diferentes
    parametros, como @Path,, @Query, @Body, Head.
    Lo bueno de retrofit es que vas a poder entender cualquier API de cualquier aplicación de Android
    y todo bien definido
     */


    /*
    Base Url = https://api.github.com/
     */

    //https://api.github.com/users/jakewharton
    @GET("users/{login}")
    LiveData<ApiResponse<User>> getUser(@Path("login") String login);

    //https://api.github.com/users/jakewharton/repos
    @GET("users/{login}/repos")
    LiveData<ApiResponse<List<Repo>>> getRepos(@Path("login") String login);
    //api.github.com/repos/JakeWharton/ActionBarSherlock
    @GET("repos/{owner}/{name}")
    LiveData<ApiResponse<Repo>> getRepo(@Path("owner") String owner, @Path("name") String name);
    //https://api.github.com/repos/JakeWharton/ActionBarSherlock/contributors
    @GET("repos/{owner}/{name}/contributors")
    LiveData<ApiResponse<List<Contributor>>> getContributors(@Path("owner") String owner, @Path("name") String name);
    //https://api.github.com/search/repositories?q=jakewharton
    //https://developer.github.com/v3/search/#search-repositories
    @GET("search/repositories")
    LiveData<ApiResponse<RepoSearchResponse>> searchRepos(@Query("q") String query);
    //https://api.github.com/search/repositories?q=jakewharton&page=2
    @GET("search/repositories")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query, @Query("page") int page);
}
