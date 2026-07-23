# Quantum Radar (QuRadar)

A Java simulation of a traffic radar system that evaluates vehicle observations against configurable traffic rules and issues fines for detected violations.

Each observation contains a plate number, date, car type, speed, and seatbelt status.

## Requirements

- JDK 17+
- Maven 3.6+

## Setup & Execution

Clone the repository:

```bash
git clone <repo-url>
cd Quantum-Radar
```

Run the tests:

```bash
mvn clean test
```

Compile and run the demo:

```bash
mvn clean compile
java -cp target/classes com.quantumradar.Main
```

## Design

Traffic rules are implemented using a Strategy-style design through `IViolationRule`.

`QuRadar` receives a list of rules through constructor injection and evaluates each observation against them. This allows new rules to be added without modifying the core processing logic in `QuRadar`.

### Current Rules

- Private vehicle maximum speed: 80 km/h (300 EGP)
- Truck maximum speed: 60 km/h (300 EGP)
- Seatbelt must be fastened (100 EGP)

## Project Structure

```text
src/
├── main/java/com/quantumradar/
│   ├── Main.java
│   ├── model/
│   ├── radar/
│   └── rules/
└── test/java/com/quantumradar/
    ├── radar/
    └── rules/
```

## Example Output

```text
Traffic for car ABC1234
Total amount: 400 EGP
Violations:
- Seatbelt not fastned : 100 EGP
- speed of 94 exceeded max allowed 80 : 300 EGP

Traffic for car XYZ9876
Total amount: 300 EGP
Violations:
- speed of 75 exceeded max allowed 60 : 300 EGP

--- All Fines (getAllPossibleFines) ---
XYZ9876: 300 EGP
ABC1234: 400 EGP

--- Violated Rules Count ---
Speed Limit Rule (PRIVATE): 1
Seatbelt Rule: 1
Speed Limit Rule (TRUCK): 1
```

## Assumptions

- The physical radar hardware, computer vision processing, and vehicle classification models are outside the scope of this Java core application. Related input assumptions are documented in `QuRadar.java`.
- `QuRadar` receives already-structured `Observation` objects.
- No speed limit is assumed for a vehicle type unless a corresponding rule is explicitly configured for it.
- Fine amounts follow the values provided in the assessment specification.