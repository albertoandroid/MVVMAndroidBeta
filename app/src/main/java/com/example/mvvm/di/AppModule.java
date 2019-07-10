package com.example.mvvm.di;

import android.app.Application;

import androidx.room.Room;

import com.example.mvvm.api.WebServiceApi;
import com.example.mvvm.db.GitHubDb;
import com.example.mvvm.db.RepoDao;
import com.example.mvvm.db.UserDao;
import com.example.mvvm.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    WebServiceApi provideGithubService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(WebServiceApi.class);
    }

    @Singleton @Provides
    GitHubDb provideDb(Application app) {
        return Room.databaseBuilder(app, GitHubDb.class,"github.db").build();
    }

    @Singleton @Provides
    UserDao provideUserDao(GitHubDb db) {
        return db.userDao();
    }

    @Singleton @Provides
    RepoDao provideRepoDao(GitHubDb db) {
        return db.repoDao();
    }
}
