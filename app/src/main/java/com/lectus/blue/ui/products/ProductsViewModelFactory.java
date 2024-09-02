package com.lectus.blue.ui.products;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lectus.blue.repository.IProductRepository;

public class ProductsViewModelFactory implements ViewModelProvider.Factory {
    private final IProductRepository repository;

    public ProductsViewModelFactory(IProductRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductsViewModel.class)) {
            return (T) new ProductsViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
