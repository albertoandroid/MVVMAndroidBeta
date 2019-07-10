package com.example.mvvm.ui.user;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvm.R;
import com.example.mvvm.binding.FragmentDataBindingComponent;
import com.example.mvvm.databinding.UserFragmentBinding;
import com.example.mvvm.di.Injectable;
import com.example.mvvm.model.Repo;
import com.example.mvvm.model.User;
import com.example.mvvm.repository.Resource;
import com.example.mvvm.ui.common.NavigationController;
import com.example.mvvm.ui.common.RepoListAdapter;
import com.example.mvvm.ui.common.RetryCallback;
import com.example.mvvm.util.AutoClearedValue;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements Injectable {

    private static final String LOGIN_KEY = "login";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    /*
    DataBindingComponent es una interfaz que se genera en tiempo de compilación y contine
    todos los getter para todos los bindingadapter de instancias utilizadas.
    FragmentDataBindingComponent -> Implementación de un componente databingin para fragment
     */
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserViewModel userViewModel;
/*
AutoClearedValue
A value holder que automaticamente nos limplia las referencias si una vista de fragment
    es destruida

   UserFragmentBinding //Clase que genera automaticamente AndroidStudio cuando utilizamos DataBinding
        //en una activity en concreto
 */

    AutoClearedValue<UserFragmentBinding> binding;
    private AutoClearedValue<RepoListAdapter> adapter;


    public static UserFragment create(String login) {
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_KEY, login);
        userFragment.setArguments(bundle);
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /*
        UserFragmentBinding es una clase que se crea automaticamente cuando utilizamos databinding-.
         */
        UserFragmentBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.user_fragment,
                container, false, dataBindingComponent);
        dataBinding.setRetryCallback(new RetryCallback() {
            @Override
            public void retry() {
                userViewModel.retry();
            }
        });
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        Para instanciar lo hacemos a través del ViewModelProvider. Por lo tanto ya quedan asociados
        nuestro fragment con la calse viewModel.
         */
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.setLogin(getArguments().getString(LOGIN_KEY));
        //Analizamos los cambios como vimos en su momento.
        userViewModel.getUser().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                binding.get().setUser(userResource == null ? null : userResource.data);
                binding.get().setUserResource(userResource);
                // this is only necessary because espresso cannot read data binding callbacks.
                binding.get().executePendingBindings();
            }
        });
        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent, false,
                new RepoListAdapter.RepoClickCallback() {
                    @Override
                    public void onClick(Repo repo) {
                        navigationController.navigateToRepo(repo.owner.login, repo.name);
                    }
                });
        binding.get().repoList.setAdapter(rvAdapter);
        this.adapter = new AutoClearedValue<>(this, rvAdapter);
        initRepoList();
    }

    private void initRepoList() {
        userViewModel.getRepositories().observe(this, new Observer<Resource<List<Repo>>>() {
            @Override
            public void onChanged(Resource<List<Repo>> repos) {
                // no null checks for adapter.get() since LiveData guarantees that we'll not receive
                // the event if fragment is now show.
                if (repos == null) {
                    adapter.get().replace(null);
                } else {
                    adapter.get().replace(repos.data);
                }
            }
        });
    }

}
