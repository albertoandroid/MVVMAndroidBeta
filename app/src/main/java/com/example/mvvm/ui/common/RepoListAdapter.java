package com.example.mvvm.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import com.example.mvvm.R;
import com.example.mvvm.databinding.RepoItemBinding;
import com.example.mvvm.model.Repo;

import java.util.Objects;

/*
RecyclerView adapter for Repo Class
 */
public class RepoListAdapter extends DataBoundListAdapter<Repo, RepoItemBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final RepoClickCallback repoClickCallback;
    private final boolean showFullName;

    public RepoListAdapter(DataBindingComponent dataBindingComponent, boolean showFullName,
                           RepoClickCallback repoClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.repoClickCallback = repoClickCallback;
        this.showFullName = showFullName;
    }

    @Override
    protected RepoItemBinding createBinding(ViewGroup parent) {
        RepoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.repo_item,
                        parent, false, dataBindingComponent);
        binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repo repo = binding.getRepo();
                if (repo != null && repoClickCallback != null) {
                    repoClickCallback.onClick(repo);
                }
            }
        });
        return binding;
    }

    @Override
    protected void bind(RepoItemBinding binding, Repo item) {
        binding.setRepo(item);
    }

    @Override
    protected boolean areItemsTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem.owner, newItem.owner) &&
                Objects.equals(oldItem.name, newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem.description, newItem.description) &&
                oldItem.stars == newItem.stars;
    }

    public interface RepoClickCallback {
        void onClick(Repo repo);
    }
}

