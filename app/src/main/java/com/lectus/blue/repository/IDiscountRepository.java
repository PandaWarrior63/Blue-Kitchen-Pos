package com.lectus.blue.repository;

import com.lectus.blue.model.Discount;

public interface IDiscountRepository {
    void addNewDiscount(Discount discount) throws Exception;

    Discount getCurrentDiscount() throws Exception;

    void clearDiscount() throws Exception;

    double getDiscountAmount(double totalAmount, IApplyDiscountCallback callback) throws Exception;
}
