package com.example.mvvm.ui.common;


import androidx.fragment.app.FragmentManager;

import com.example.mvvm.MainActivity;
import com.example.mvvm.R;
import com.example.mvvm.ui.repo.RepoFragment;
import com.example.mvvm.ui.search.SearchFragment;
import com.example.mvvm.ui.user.UserFragment;

import javax.inject.Inject;

/*
Una clase de utilidad que nos permite manejar la navegación desde el MainActivity
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;
    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        //Insertamos el fragtmen emplazando el fragment que ya existe.
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                //Hacemos el commit del fragment, en este caso
                //con esta función que es igual que el commit, con la unica
                //diferencia que genera una excepcion si se produce una
                //perdida de estado en la activity..
                .commitAllowingStateLoss();
    }


    public void navigateToRepo(String owner, String name) {

        RepoFragment fragment = RepoFragment.create(owner, name);
        String tag = "repo" + "/" + owner + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                //agregamos este fragment a la pila, asi que cuando
                //pulsemos al botón atrás en el siguente fragment,
                //volveremos a este
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    public void navigateToUser(String login) {
        String tag = "user" + "/" + login;
        UserFragment userFragment = UserFragment.create(login);
        fragmentManager.beginTransaction()
                .replace(containerId, userFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
