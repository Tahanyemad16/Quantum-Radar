package com.quantumradar.rules;

import com.quantumradar.model.CarType;
import com.quantumradar.model.Observation;
import com.quantumradar.model.SeatbeltStatus;
import com.quantumradar.model.Violation;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpeedLimitRuleTest {

    private static Observation createObservation(CarType carType, double speed) {
        return new Observation("ABC1234", new Date(), carType, speed, SeatbeltStatus.FASTENED);
    }

    @Test
    void testPrivateCarExceedingSpeedLimit() {
        SpeedLimitRule rule = new SpeedLimitRule(CarType.PRIVATE, 80.0, 300);
        Observation obs = createObservation(CarType.PRIVATE, 94.0);

        Optional<Violation> result = rule.evaluate(obs);

        assertTrue(result.isPresent());
        assertEquals(300, result.get().getFee());
        assertEquals("speed of 94 exceeded max allowed 80", result.get().getDescription());
    }

    @Test
    void testPrivateCarAtExactBoundaryHasNoViolation() {
        SpeedLimitRule rule = new SpeedLimitRule(CarType.PRIVATE, 80.0, 300);
        Observation obs = createObservation(CarType.PRIVATE, 80.0);

        assertFalse(rule.evaluate(obs).isPresent());
    }

    @Test
    void testPrivateCarOneAboveBoundaryTriggersViolation() {
        SpeedLimitRule rule = new SpeedLimitRule(CarType.PRIVATE, 80.0, 300);
        Observation obs = createObservation(CarType.PRIVATE, 81.0);

        assertTrue(rule.evaluate(obs).isPresent());
    }

    @Test
    void testTruckAtExactBoundaryHasNoViolation() {
        SpeedLimitRule rule = new SpeedLimitRule(CarType.TRUCK, 60.0, 300);
        Observation obs = createObservation(CarType.TRUCK, 60.0);

        assertFalse(rule.evaluate(obs).isPresent());
    }

    @Test
    void testTruckOneAboveBoundaryTriggersViolation() {
        SpeedLimitRule rule = new SpeedLimitRule(CarType.TRUCK, 60.0, 300);
        Observation obs = createObservation(CarType.TRUCK, 61.0);

        assertTrue(rule.evaluate(obs).isPresent());
    }

    @Test
    void testRuleDoesNotApplyToDifferentCarType() {
        SpeedLimitRule rule = new SpeedLimitRule(CarType.PRIVATE, 80.0, 300);
        Observation obs = createObservation(CarType.TRUCK, 100.0);

        assertFalse(rule.evaluate(obs).isPresent());
    }
}