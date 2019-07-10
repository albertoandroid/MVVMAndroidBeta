package com.example.mvvm.ui.user;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.model.Repo;
import com.example.mvvm.model.User;
import com.example.mvvm.repository.RepoRepository;
import com.example.mvvm.repository.Resource;
import com.example.mvvm.repository.UserRepository;
import com.example.mvvm.util.AbsentLiveData;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class UserViewModel extends ViewModel {

    final MutableLiveData<String> login = new MutableLiveData<>();

    private final LiveData<Resource<List<Repo>>> repositories;
    private final LiveData<Resource<User>> user;
    @SuppressWarnings("unchecked")
    @Inject
    public UserViewModel(UserRepository userRepository, RepoRepository repoRepository) {
        //Transformation.switcMap nos devuelve un liveData.
        //En este caso a través de la interfaz funcional Fuction, se le pasa el parametro de entrada
        //y el de salida. El parametro de entrada es login y el de salida LiveData<Resource<User>>
        //y se aplica una función, que sera si login es null crear un AbsentLiveData
        //y sino devolver una userRepositoriy.loadUserlogion.
        user = Transformations.switchMap(login, new Function<String, LiveData<Resource<User>>>() {
            @Override
            public LiveData<Resource<User>> apply(String login) {
                if (login == null) {
                    return AbsentLiveData.create();
                } else {
                    return userRepository.loadUser(login);
                }
            }
        });
        repositories = Transformations.switchMap(login, login -> {
            if (login == null) {
                return AbsentLiveData.create();
            } else {
                return repoRepository.loadRepos(login);
            }
        });
    }


    public void setLogin(String login) {
        if (Objects.equals(this.login.getValue(), login)) {
            return;
        }
        this.login.setValue(login);
    }

    public LiveData<Resource<User>> getUser() {
        return user;
    }


    public LiveData<Resource<List<Repo>>> getRepositories() {
        return repositories;
    }


    public void retry() {
        if (this.login.getValue() != null) {
            this.login.setValue(this.login.getValue());
        }
    }
}
