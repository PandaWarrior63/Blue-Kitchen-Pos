package com.lectus.blue.ui.cart;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lectus.blue.repository.ICartRepository;
import com.lectus.blue.repository.IDiscountRepository;

public class CartViewModelFactory implements ViewModelProvider.Factory {
    private final ICartRepository repository;
    private final IDiscountRepository discountRepository;

    public CartViewModelFactory(ICartRepository repository, IDiscountRepository discountRepository) {
        this.repository = repository;
        this.discountRepository = discountRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(repository, discountRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
