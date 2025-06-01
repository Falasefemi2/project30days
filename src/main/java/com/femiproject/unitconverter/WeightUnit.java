package com.femiproject.unitconverter;

public enum WeightUnit {
    MILLIGRAM(0.000001, "mg"),
    GRAM(0.001, "g"),
    KILOGRAM(1.0, "kg"),
    OUNCE(0.0283495, "oz"),
    POUND(0.453592, "lb");

    private final double toKg;
    private final String symbol;

    WeightUnit(double toKg, String symbol) {
        this.toKg = toKg;
        this.symbol = symbol;
    }

    public double toBase(double value) {
        return value * toKg;
    }

    public double fromBase(double value) {
        return value / toKg;
    }

    public String getSymbol() {
        return symbol;
    }

    public static WeightUnit fromString(String input) {
        for (WeightUnit unit : values()) {
            if (unit.name().equalsIgnoreCase(input) || unit.symbol.equalsIgnoreCase(input)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid WeightUnit: " + input);
    }
}
