package com.quantumradar.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fine {
    private final String plateNumber;
    private final List<Violation> violations;

    public Fine(String plateNumber) {
        this.plateNumber = plateNumber;
        this.violations = new ArrayList<>();
    }

    public void addViolation(Violation violation) {
        this.violations.add(violation);
    }

    public int getTotalAmount() {
        return violations.stream()
                .mapToInt(Violation::getFee)
                .sum();
    }

    public String getPlateNumber() { return plateNumber; }

    // Returned as Unmodifiable List to protect encapsulation
    public List<Violation> getViolations() {
        return Collections.unmodifiableList(violations);
    }

    public void printFine() {
        System.out.println("Traffic for car " + plateNumber);
        System.out.println("Total amount: " + getTotalAmount() + " EGP");
        System.out.println("Violations:");
        for (Violation v : violations) {
            System.out.println("- " + v.getDescription() + " : " + v.getFee() + " EGP");
        }
    }
}