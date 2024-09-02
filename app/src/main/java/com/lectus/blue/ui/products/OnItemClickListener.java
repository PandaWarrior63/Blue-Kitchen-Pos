package com.lectus.blue.ui.products;

import com.lectus.blue.model.Product;

public interface OnItemClickListener {
    void onItemClick(int productId);
    void onItemLongClick(int productId);
    void onProductInfoClick(Product product);
}
