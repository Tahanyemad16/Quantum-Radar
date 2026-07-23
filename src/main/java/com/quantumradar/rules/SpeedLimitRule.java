package com.quantumradar.rules;

import com.quantumradar.model.CarType;
import com.quantumradar.model.Observation;
import com.quantumradar.model.Violation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class SpeedLimitRule implements IViolationRule {
    private final CarType targetCarType;
    private final double maxSpeed;
    private final int fee;

    public SpeedLimitRule(CarType targetCarType, double maxSpeed, int fee) {
        this.targetCarType = targetCarType;
        this.maxSpeed = maxSpeed;
        this.fee = fee;
    }

    @Override
    public String getRuleName() {
        return "Speed Limit Rule (" + targetCarType + ")";
    }

    @Override
    public Optional<Violation> evaluate(Observation observation) {
        if (observation.getCarType() == this.targetCarType && observation.getSpeed() > this.maxSpeed) {
            String desc = "speed of " + formatSpeed(observation.getSpeed())
                    + " exceeded max allowed " + formatSpeed(this.maxSpeed);
            return Optional.of(new Violation(desc, this.fee));
        }
        return Optional.empty();
    }

    /**
     * Renders a speed for the violation message without the misleading truncation a plain
     * {@code (int)} cast produces (e.g. 80.9 and 80.5 both becoming "80", making "80 exceeded
     * max allowed 80" look nonsensical). Whole numbers print without a decimal point; fractional
     * speeds are rounded to 2 decimal places with trailing zeros stripped (e.g. 80.5, not 80.50).
     */
    private static String formatSpeed(double speed) {
        return BigDecimal.valueOf(speed)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }
}