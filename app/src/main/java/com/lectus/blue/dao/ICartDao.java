package com.lectus.blue.dao;

import com.lectus.blue.model.CartItem;

import java.util.ArrayList;

public interface ICartDao {
    ArrayList<CartItem> getCartItems() throws Exception;
    void createCartItem(int productId) throws Exception;
    void deleteCartItem(int productId) throws Exception;
    void updateCartItem(int productId, int quantity) throws Exception;
    void clearCart() throws Exception;
}
