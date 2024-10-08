package com.lectus.blue.repository;

import com.lectus.blue.model.Cart;

public interface ICartRepository {
    Cart getCart() throws Exception;
    void addProductToCart(int productId) throws Exception;
    void removeProductFromCart(int productId) throws Exception;
    void clearCart() throws Exception;
}
