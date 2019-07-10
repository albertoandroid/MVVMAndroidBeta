package com.example.mvvm.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.mvvm.AppExecutors;
import com.example.mvvm.api.ApiResponse;
import com.example.mvvm.api.WebServiceApi;
import com.example.mvvm.db.UserDao;
import com.example.mvvm.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/*
Repositorio para manejar nuestros objetos usuario
 */

@Singleton
public class UserRepository {
    private final UserDao userDao;
    private final WebServiceApi githubService;
    private final AppExecutors appExecutors;

    @Inject
    UserRepository(AppExecutors appExecutors, UserDao userDao, WebServiceApi githubService) {
        this.userDao = userDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<User>> loadUser(String login) {
        return new NetworkBoundResource<User,User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return githubService.getUser(login);
            }
        }.asLiveData();
    }
}

