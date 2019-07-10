package com.example.mvvm.ui.repo;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.example.mvvm.R;
import com.example.mvvm.databinding.ContributorItemBinding;
import com.example.mvvm.model.Contributor;
import com.example.mvvm.ui.common.DataBoundListAdapter;

import java.util.Objects;
/*
Utilizamos nuestro DataBoundListAdapter que nos hemos creado en el pasado
Un adapter para RecyclerView generico que usa Data binding y DiffUtil
 */


public class ContributorAdapter
        extends DataBoundListAdapter<Contributor, ContributorItemBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ContributorClickCallback callback;

    public ContributorAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                              ContributorClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    @Override
    protected ContributorItemBinding createBinding(ViewGroup parent) {
        ContributorItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.contributor_item, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Contributor contributor = binding.getContributor();
            if (contributor != null && callback != null) {
                callback.onClick(contributor);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ContributorItemBinding binding, Contributor item) {
        binding.setContributor(item);
    }

    @Override
    protected boolean areItemsTheSame(Contributor oldItem, Contributor newItem) {
        return Objects.equals(oldItem.getLogin(), newItem.getLogin());
    }

    @Override
    protected boolean areContentsTheSame(Contributor oldItem, Contributor newItem) {
        return Objects.equals(oldItem.getAvatarUrl(), newItem.getAvatarUrl())
                && oldItem.getContributions() == newItem.getContributions();
    }

    public interface ContributorClickCallback {
        void onClick(Contributor contributor);
    }
}
