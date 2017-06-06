package com.example.myfirstapp.Models;

/**
 * Created by shubhajain on 5/30/2017.
 */

public class HotelTable {
    private String id;
    private String tableName;
    private String hotelId;
    private String status;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "HotelTable{" +
                "id='" + id + '\'' +
                ", tableName='" + tableName + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


    public HotelTable(String id, String tableName, String hotelId, String status){
        setHotelId(hotelId);
        setId(id);
        setStatus(status);
        setTableName(tableName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
