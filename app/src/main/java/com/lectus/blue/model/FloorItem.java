package com.lectus.blue.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FloorItem {
    private String company;
    private String name;
    private String floor_name;

    public FloorItem(JSONObject object)
    {
        try {
            this.company = object.getString("company");
            this.name = object.getString("name");
            this.floor_name = object.getString("floor_name");
        } catch (JSONException e) {
            this.company = "";
            this.name = "";
            this.floor_name = "";
        }
    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor_name() {
        return floor_name;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }
}
