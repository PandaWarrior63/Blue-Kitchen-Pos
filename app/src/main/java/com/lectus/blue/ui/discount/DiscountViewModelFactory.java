package com.lectus.blue.ui.discount;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lectus.blue.repository.IDiscountRepository;

public class DiscountViewModelFactory implements ViewModelProvider.Factory {
    private final IDiscountRepository repository;

    public DiscountViewModelFactory(IDiscountRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DiscountViewModel.class)) {
            return (T) new DiscountViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
