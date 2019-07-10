package com.example.mvvm.ui.search;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.mvvm.R;
import com.example.mvvm.binding.FragmentDataBindingComponent;
import com.example.mvvm.databinding.SearchFragmentBinding;
import com.example.mvvm.di.Injectable;
import com.example.mvvm.model.Repo;
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
public class SearchFragment extends Fragment implements Injectable {

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

    /*
AutoClearedValue
A value holder que automaticamente nos limplia las referencias si una vista de fragment
    es destruida
 */
    AutoClearedValue<SearchFragmentBinding> binding;

    AutoClearedValue<RepoListAdapter> adapter;

    private SearchViewModel searchViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /*
        UserFragmentBinding es una clase que se crea automaticamente cuando utilizamos databinding-.
         */
        SearchFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.search_fragment, container, false,
                        dataBindingComponent);
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
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
        initRecyclerView();

        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent, true,
                new RepoListAdapter.RepoClickCallback() {
                    @Override
                    public void onClick(Repo repo) {
                        navigationController.navigateToRepo(repo.owner.login, repo.name);
                    }
                });
        binding.get().repoList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        initSearchInputListener();

        binding.get().setCallback(new RetryCallback() {
            @Override
            public void retry() {
                searchViewModel.refresh();
            }
        });
    }

    private void initSearchInputListener() {
        binding.get().input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /*
            El IME_ACTION_SEARCH es la palabra clave que reservan en Android cuando se
            ha de realizar una función de busqueda.
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchFragment.this.doSearch(v);
                    return true;
                }
                return false;
            }
        });
        binding.get().input.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doSearch(v);
                return true;
            }
            return false;
        });
    }

    private void doSearch(View v) {
        String query = binding.get().input.getText().toString();
        // Dismiss keyboard
        dismissKeyboard(v.getWindowToken());
        binding.get().setQuery(query);
        searchViewModel.setQuery(query);
    }

    private void initRecyclerView() {
        //añadimos un scrollListener a nuestro recyclerview
        //para que cuando se hayan visto todos, lanzar una nueva petición
        //para los nuevos datos.
        binding.get().repoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {
                    searchViewModel.loadNextPage();
                }
            }
        });
        searchViewModel.getResults().observe(this, new Observer<Resource<List<Repo>>>() {
            @Override
            public void onChanged(Resource<List<Repo>> result) {
                binding.get().setSearchResource(result);
                binding.get().setResultCount((result == null || result.data == null)
                        ? 0 : result.data.size());
                adapter.get().replace(result == null ? null : result.data);
                binding.get().executePendingBindings();
            }
        });

        searchViewModel.getLoadMoreStatus().observe(this, new Observer<SearchViewModel.LoadMoreState>() {
            @Override
            public void onChanged(SearchViewModel.LoadMoreState loadingMore) {
                if (loadingMore == null) {
                    binding.get().setLoadingMore(false);
                } else {
                    binding.get().setLoadingMore(loadingMore.isRunning());
                    String error = loadingMore.getErrorMessageIfNotHandled();
                    if (error != null) {
                        //  Snackbar.make(binding.get().loadMoreBar, error, Snackbar.LENGTH_LONG).show();
                    }
                }
                binding.get().executePendingBindings();
            }
        });
    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }
}
