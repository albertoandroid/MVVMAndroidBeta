package com.example.mvvm.ui.common;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/*
ViewHolde generico que trabaja con ViewDataBinding
 */

public class DataBoundViewHolder<T extends ViewDataBinding>
        extends RecyclerView.ViewHolder {

    public final T binding;
    DataBoundViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
