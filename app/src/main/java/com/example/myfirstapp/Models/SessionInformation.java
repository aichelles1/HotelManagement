package com.example.myfirstapp.Models;

import android.app.Application;

/**
 * Created by shubhajain on 6/3/2017.
 */

public class SessionInformation extends Application {
    private static String hotelId;
    private static String hotelName;
    private static String orderId;
    private static String orderLineId;
    private static String hotelTableId;

    public static String getHotelId() {
        return hotelId;
    }

    public static void setHotelId(String hotelId) {
        SessionInformation.hotelId = hotelId;
    }

    public static String getOrderId() {
        return orderId;
    }

    public static void setOrderId(String orderId) {
        SessionInformation.orderId = orderId;
    }

    public static String getOrderLineId() {
        return orderLineId;
    }

    public static void setOrderLineId(String orderLineId) {
        SessionInformation.orderLineId = orderLineId;
    }

    public static String getHotelName() {
        return hotelName;
    }

    public static void setHotelName(String hotelName) {
        SessionInformation.hotelName = hotelName;
    }

    public static String getHotelTableId() {
        return hotelTableId;
    }

    public static void setHotelTableId(String hotelTableId) {
        SessionInformation.hotelTableId = hotelTableId;
    }
}
