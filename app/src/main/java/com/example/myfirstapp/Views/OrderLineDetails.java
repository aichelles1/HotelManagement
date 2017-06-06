package com.example.myfirstapp.Views;

import com.google.gson.internal.LinkedTreeMap;

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

    public OrderLineDetails(LinkedTreeMap<String,Object> orderLines){
        if(orderLines.containsKey("Id")){
            setId(orderLines.get("Id").toString());
        }
        if(orderLines.containsKey("Menu_Item__c")){
            setMenuItemId(orderLines.get("Menu_Item__c").toString());
        }
        if(orderLines.containsKey("Order__c")){
            setOrderId(orderLines.get("Order__c").toString());
        }
        if(orderLines.containsKey("Quantity__c")){
            setQuantity(orderLines.get("Quantity__c").toString());
        }

        if(orderLines.containsKey("Menu_Item_Name__c")){
            setMenuItemName(orderLines.get("Menu_Item_Name__c").toString());
        }
    }


}
