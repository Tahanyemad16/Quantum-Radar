package com.quantumradar.rules;

import com.quantumradar.model.Observation;
import com.quantumradar.model.SeatbeltStatus;
import com.quantumradar.model.Violation;

import java.util.Optional;

public class SeatbeltRule implements IViolationRule {

    @Override
    public String getRuleName() {
        return "Seatbelt Rule";
    }

    @Override
    public Optional<Violation> evaluate(Observation observation) {
        if (observation.getSeatbeltStatus() == SeatbeltStatus.NOT_FASTENED) {
            return Optional.of(new Violation("Seatbelt not fastned", 100));
        }
        return Optional.empty();
    }
}