package com.lectus.blue.repository;

import com.lectus.blue.model.Product;
import java.util.ArrayList;


public interface IProductRepository {
    void createProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Product product);
    ArrayList<Product> getAllProducts();
    Product getProductById(int productId);
    ArrayList<Product> getFilteredProducts(String keyword);
}
