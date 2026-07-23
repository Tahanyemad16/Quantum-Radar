package com.quantumradar;

import com.quantumradar.model.CarType;
import com.quantumradar.model.Fine;
import com.quantumradar.model.Observation;
import com.quantumradar.model.SeatbeltStatus;
import com.quantumradar.radar.QuRadar;
import com.quantumradar.rules.IViolationRule;
import com.quantumradar.rules.SeatbeltRule;
import com.quantumradar.rules.SpeedLimitRule;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        // Order matters: SeatbeltRule first so output matches example
        List<IViolationRule> rules = Arrays.asList(
            new SeatbeltRule(),
            new SpeedLimitRule(CarType.TRUCK, 60.0, 300),
            new SpeedLimitRule(CarType.PRIVATE, 80.0, 300)
        );

        QuRadar radar = new QuRadar(rules);

        // Observation 1: Private car exceeding speed limit & seatbelt unfastened
        Observation obs1 = new Observation("ABC1234", new Date(), CarType.PRIVATE, 94, SeatbeltStatus.NOT_FASTENED);
        Optional<Fine> fine1 = radar.observe(obs1);
        fine1.ifPresent(fine -> {
            fine.printFine();
            System.out.println();
        });

        // Observation 2: Truck exceeding speed limit
        Observation obs2 = new Observation("XYZ9876", new Date(), CarType.TRUCK, 75, SeatbeltStatus.FASTENED);
        Optional<Fine> fine2 = radar.observe(obs2);
        fine2.ifPresent(fine -> {
            fine.printFine();
            System.out.println();
        });

        // Observation 3: Bus within valid bounds (No speed rule configured for Bus)
        Observation obs3 = new Observation("BUS1010", new Date(), CarType.BUS, 75, SeatbeltStatus.FASTENED);
        Optional<Fine> fine3 = radar.observe(obs3);
        fine3.ifPresent(fine -> {
            fine.printFine();
            System.out.println();
        });

        System.out.println("--- All Fines (getAllPossibleFines) ---");
        Map<String, Integer> finesMap = radar.getAllPossibleFines();
        finesMap.forEach((plate, amount) -> System.out.println(plate + ": " + amount + " EGP"));

        System.out.println("\n--- Violated Rules Count ---");
        Map<String, Integer> ruleCounts = radar.getViolatedRulesWithCount();
        ruleCounts.forEach((ruleName, count) -> System.out.println(ruleName + ": " + count));
    }
}