package com.lectus.blue.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TableOrderItem {
    private String item_code;
    private String name;
    private String parent;
    private String price;
    private String quantity;

    public TableOrderItem(JSONObject object) {

        try {
            this.name = object.getString("name");
            this.item_code = object.getString("item_code");
            this.parent = object.getString("parent");
            this.price = object.getString("price");
            this.quantity = object.getString("quantity");
        } catch (JSONException e) {
            //throw new RuntimeException(e);
            this.name = "";
            this.item_code = "";
            this.parent = "";
            this.price = "0";
            this.quantity = "0";
        }
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
