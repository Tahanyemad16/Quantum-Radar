package com.quantumradar.model;

import java.util.Date;

public class Observation {
    private final String plateNumber;
    private final Date date;
    private final CarType carType;
    private final double speed;
    private final SeatbeltStatus seatbeltStatus;

    public Observation(String plateNumber, Date date, CarType carType, double speed, SeatbeltStatus seatbeltStatus) {
        this.plateNumber = plateNumber;
        this.date = date;
        this.carType = carType;
        this.speed = speed;
        this.seatbeltStatus = seatbeltStatus; // Bug Fixed here!
    }

    public String getPlateNumber() { return plateNumber; }
    public Date getDate() { return date; }
    public CarType getCarType() { return carType; }
    public double getSpeed() { return speed; }
    public SeatbeltStatus getSeatbeltStatus() { return seatbeltStatus; }
}