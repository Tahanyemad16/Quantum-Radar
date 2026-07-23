package com.quantumradar.radar;

import com.quantumradar.model.Fine;
import com.quantumradar.model.Observation;
import com.quantumradar.model.Violation;
import com.quantumradar.rules.IViolationRule;

import java.util.*;

/**
 * QuRadar System
 * 
 * Description:
 * Core software system responsible for processing structured traffic observations 
 * against a collection of injected violation rules, aggregating fines, and tracking 
 * violation rule metrics.
 * 
 * AI Model Context:
 * In a real-world setup, the incoming Observation data (plate number, car type, seatbelt status) 
 * is assumed to be extracted upstream using a Computer Vision / Classification Model 
 * integrated with the physical camera hardware before reaching this rule evaluation system.
 */
public class QuRadar {
    private final List<IViolationRule> rules;
    private final List<Fine> fines;
    private final Map<String, Integer> ruleViolationCounts;

    public QuRadar(List<IViolationRule> rules) {
        this.rules = new ArrayList<>(rules);
        this.fines = new ArrayList<>();
        this.ruleViolationCounts = new HashMap<>();
    }

    public Optional<Fine> observe(Observation observation) {
        List<Violation> detectedViolations = new ArrayList<>();

        for (IViolationRule rule : rules) {
            Optional<Violation> violationOpt = rule.evaluate(observation);
            if (violationOpt.isPresent()) {
                detectedViolations.add(violationOpt.get());
                ruleViolationCounts.put(rule.getRuleName(), 
                    ruleViolationCounts.getOrDefault(rule.getRuleName(), 0) + 1);
            }
        }

        if (!detectedViolations.isEmpty()) {
            Fine fine = new Fine(observation.getPlateNumber());
            for (Violation v : detectedViolations) {
                fine.addViolation(v);
            }
            fines.add(fine);
            return Optional.of(fine);
        }

        return Optional.empty();
    }

    public Map<String, Integer> getAllPossibleFines() {
        Map<String, Integer> result = new HashMap<>();
        for (Fine fine : fines) {
            result.put(fine.getPlateNumber(), 
                result.getOrDefault(fine.getPlateNumber(), 0) + fine.getTotalAmount());
        }
        return result;
    }

    public Map<String, Integer> getViolatedRulesWithCount() {
        return new HashMap<>(ruleViolationCounts);
    }
}