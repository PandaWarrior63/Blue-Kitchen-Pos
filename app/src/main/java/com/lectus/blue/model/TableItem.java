package com.lectus.blue.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TableItem {
    private String name;
    private String description;
    private String background;
    private String floor;
    private String occupied_seats;
    private String seats;
    private String table_name;
    private String status;

    private List<TableOrderItem> orderList = new ArrayList<>();

    public TableItem(String name, String description) {
        this.name = name;
        this.description = description;
        this.background = "#DCDCDC";
    }
    public TableItem(JSONObject obj)
    {
        this.background = "#DCDCDC";
        this.description = "";

        try {
            this.name = obj.getString("name");
            this.floor = obj.getString("floor");
            this.occupied_seats = obj.getString("occupied_seats");
            this.seats = obj.getString("seats");
            this.table_name = obj.getString("table_name");
            this.status = obj.getString("status");
        } catch (JSONException e) {
            this.name = "";
            this.floor = "";
            this.occupied_seats = "";
            this.seats = "";
            this.table_name = "";
            this.status = "";
            throw new RuntimeException(e);

        }
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getOccupied_seats() {
        return occupied_seats;
    }

    public void setOccupied_seats(String occupied_seats) {
        this.occupied_seats = occupied_seats;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TableOrderItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<TableOrderItem> orderList) {
        this.orderList = orderList;
    }
    public void addTableOrderItem(TableOrderItem tableOrderItem){
        this.orderList.add(tableOrderItem);
    }

    public void setOrderList(JSONArray orderItems) {
        if (!orderList.isEmpty())
            orderList.clear();
        for(int i=0;i<orderItems.length();i++)
        {
            try {
                JSONObject object = orderItems.getJSONObject(i);
                TableOrderItem item = new TableOrderItem(object);
                orderList.add(item);

            } catch (JSONException e) {
                //throw new RuntimeException(e);
            }

        }
    }
}