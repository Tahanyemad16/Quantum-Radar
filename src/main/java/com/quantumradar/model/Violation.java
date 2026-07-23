package com.quantumradar.model;

public class Violation {
    private final String description;
    private final int fee;

    public Violation(String description, int fee) {
        this.description = description;
        this.fee = fee;
    }

    public String getDescription() { return description; }
    public int getFee() { return fee; }
}