package com.example.myfirstapp.Models;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;

/**
 * Created by shubhajain on 5/25/2017.
 */

public class OrderLineDetails {
    private String orderId;
    private String id;
    private String menuItemId;
    private String quantity;
    private String menuItemName;




    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineDetails{" +
                "orderId='" + orderId + '\'' +
                ", id='" + id + '\'' +
                ", menuItemId='" + menuItemId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", menuItemName='" + menuItemName + '\'' +
                '}';
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public OrderLineDetails(String orderId, String menuItemId, String quantity){
        setOrderId(orderId);
        setMenuItemId(menuItemId);
        setQuantity(quantity);
    }

    public OrderLineDetails(String id, String menuItemId, String orderId, String quantity, String menuItemName){
        setOrderId(orderId);
        setId(id);
        setMenuItemName(menuItemName);
        setMenuItemId(menuItemId);
        setQuantity(quantity);
    }


}
