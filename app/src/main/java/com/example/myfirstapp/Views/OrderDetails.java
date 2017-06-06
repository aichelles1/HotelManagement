package com.example.myfirstapp.Views;

import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by shubhajain on 5/25/2017.
 */

public class OrderDetails {
    private String tableId;
    private String orderTime;
    private String orderStatus;
    private String id;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
