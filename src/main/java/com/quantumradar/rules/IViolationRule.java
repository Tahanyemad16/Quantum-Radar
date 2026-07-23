package com.quantumradar.rules;

import com.quantumradar.model.Observation;
import com.quantumradar.model.Violation;
import java.util.Optional;

public interface IViolationRule {
    String getRuleName();
    Optional<Violation> evaluate(Observation observation);
}