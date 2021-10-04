package com.gineuscrypticalsoft.busticketsystem.model;

public class CarListModel {

    String carName;
    String seatRent;
    String acType;
    String startTime;
    String endTime;
    String fromCity;
    String toCity;
    String image;

    public CarListModel(String carName, String seatRent, String acType, String startTime, String endTime, String fromCity, String toCity, String image) {
        this.carName = carName;
        this.seatRent = seatRent;
        this.acType = acType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.image = image;
    }

    public CarListModel(){}

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getSeatRent() {
        return seatRent;
    }

    public void setSeatRent(String seatRent) {
        this.seatRent = seatRent;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
