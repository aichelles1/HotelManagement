package com.example.myfirstapp.Models;

/**
 * Created by shubhajain on 5/30/2017.
 */

public class MenuItemDetails {
    private String id;
    private String hotelId;
    private String itemName;
    private double amount;
    private String itemType;
    private String itemDescription;
    private String menuItemImageURL;

    public MenuItemDetails(String id, String hotelId, String itemName, double amount, String itemDescription, String itemType, String menuItemImageURL) {
        this.id = id;
        this.hotelId = hotelId;
        this.itemName = itemName;
        this.amount = amount;
        this.itemDescription = itemDescription;
        this.itemType = itemType;
        this.menuItemImageURL = menuItemImageURL;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    @Override
    public String toString() {
        return "MenuItemDetails{" +
                "id='" + id + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", amount=" + amount +
                ", itemType='" + itemType + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", menuItemImageURL='" + menuItemImageURL + '\'' +
                '}';
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getMenuItemImageURL() {
        return menuItemImageURL;
    }

    public void setMenuItemImageURL(String menuItemImageURL) {
        this.menuItemImageURL = menuItemImageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
