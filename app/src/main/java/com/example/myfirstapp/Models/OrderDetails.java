package com.example.myfirstapp.Models;

/**
 * Created by shubhajain on 5/25/2017.
 */

public class OrderDetails {
    private String tableId;
    private String orderTime;
    private String orderStatus;
    private String orderName;
    private String id;

    public OrderDetails(String id,String orderName, String tableId, String orderTime, String orderStatus){
        setId(id);
        setTableId(tableId);
        setOrderStatus(orderStatus);
        setOrderTime(orderTime);
        setOrderName(orderName);
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

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

    @Override
    public String toString() {
        return "OrderDetails{" +
                "tableId='" + tableId + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
