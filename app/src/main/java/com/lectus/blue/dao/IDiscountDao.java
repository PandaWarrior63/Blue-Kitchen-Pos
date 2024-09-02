package com.lectus.blue.dao;

import com.lectus.blue.model.Discount;

import java.util.ArrayList;
import java.util.Date;

public interface IDiscountDao {
    void createDiscount(String discountType, double discountValue, Date startDate) throws Exception;

    void inactivateDiscounts() throws Exception;

    Discount getDiscount(int discountId) throws Exception;

    ArrayList<Discount> getActiveDiscounts() throws Exception;
}
