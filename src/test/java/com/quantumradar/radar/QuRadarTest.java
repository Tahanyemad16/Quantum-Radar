package com.quantumradar.radar;

import com.quantumradar.model.CarType;
import com.quantumradar.model.Fine;
import com.quantumradar.model.Observation;
import com.quantumradar.model.SeatbeltStatus;
import com.quantumradar.rules.IViolationRule;
import com.quantumradar.rules.SeatbeltRule;
import com.quantumradar.rules.SpeedLimitRule;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QuRadarTest {

    private static List<IViolationRule> defaultRules() {
        return Arrays.asList(
                new SeatbeltRule(),
                new SpeedLimitRule(CarType.PRIVATE, 80.0, 300),
                new SpeedLimitRule(CarType.TRUCK, 60.0, 300)
        );
    }

    @Test
    void testObserveMultipleViolations() {
        QuRadar radar = new QuRadar(defaultRules());

        Observation obs = new Observation("ABC1234", new Date(), CarType.PRIVATE, 94.0, SeatbeltStatus.NOT_FASTENED);
        Optional<Fine> fineOpt = radar.observe(obs);

        assertTrue(fineOpt.isPresent());
        Fine fine = fineOpt.get();
        assertEquals(400, fine.getTotalAmount());
        assertEquals(2, fine.getViolations().size());
    }

    @Test
    void testObserveNoViolationsReturnsEmpty() {
        QuRadar radar = new QuRadar(defaultRules());

        Observation obs = new Observation("CLEAN01", new Date(), CarType.PRIVATE, 60.0, SeatbeltStatus.FASTENED);
        Optional<Fine> fineOpt = radar.observe(obs);

        assertFalse(fineOpt.isPresent());
    }

    @Test
    void testObserveSingleViolationOnly() {
        QuRadar radar = new QuRadar(defaultRules());

        Observation obs = new Observation("SPEEDER1", new Date(), CarType.PRIVATE, 100.0, SeatbeltStatus.FASTENED);
        Optional<Fine> fineOpt = radar.observe(obs);

        assertTrue(fineOpt.isPresent());
        Fine fine = fineOpt.get();
        assertEquals(300, fine.getTotalAmount());
        assertEquals(1, fine.getViolations().size());
    }

    @Test
    void testUnconfiguredCarTypeNeverTriggersSpeedRule() {
        QuRadar radar = new QuRadar(defaultRules());

        Observation obs = new Observation("BUS0001", new Date(), CarType.BUS, 200.0, SeatbeltStatus.FASTENED);
        Optional<Fine> fineOpt = radar.observe(obs);

        assertFalse(fineOpt.isPresent());
    }

    @Test
    void testGetAllPossibleFinesAggregatesByPlateAcrossObservations() {
        QuRadar radar = new QuRadar(defaultRules());

        radar.observe(new Observation("REPEAT01", new Date(), CarType.PRIVATE, 94.0, SeatbeltStatus.NOT_FASTENED));
        radar.observe(new Observation("REPEAT01", new Date(), CarType.PRIVATE, 100.0, SeatbeltStatus.FASTENED));

        Map<String, Integer> finesMap = radar.getAllPossibleFines();

        // First observation: seatbelt (100) + speed (300) = 400. Second observation: speed only (300).
        assertEquals(700, finesMap.get("REPEAT01"));
    }

    @Test
    void testGetAllPossibleFinesEmptyWhenNoViolationsRecorded() {
        QuRadar radar = new QuRadar(defaultRules());

        radar.observe(new Observation("CLEAN02", new Date(), CarType.PRIVATE, 60.0, SeatbeltStatus.FASTENED));

        assertTrue(radar.getAllPossibleFines().isEmpty());
    }

    @Test
    void testGetViolatedRulesWithCountTracksPerRuleCounts() {
        QuRadar radar = new QuRadar(defaultRules());

        radar.observe(new Observation("A1", new Date(), CarType.PRIVATE, 94.0, SeatbeltStatus.NOT_FASTENED));
        radar.observe(new Observation("A2", new Date(), CarType.TRUCK, 75.0, SeatbeltStatus.FASTENED));

        Map<String, Integer> counts = radar.getViolatedRulesWithCount();

        assertEquals(1, counts.get("Speed Limit Rule (PRIVATE)"));
        assertEquals(1, counts.get("Speed Limit Rule (TRUCK)"));
        assertEquals(1, counts.get("Seatbelt Rule"));
    }
}