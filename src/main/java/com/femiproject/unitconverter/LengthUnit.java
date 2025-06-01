package com.femiproject.unitconverter;

public enum LengthUnit {
    MILLIMETER(0.001, "mm"),
    CENTIMETER(0.01, "cm"),
    METER(1.0, "m"),
    KILOMETER(1000.0, "km"),
    INCH(0.0254, "in"),
    FOOT(0.3048, "ft"),
    YARD(0.9144, "yd"),
    MILE(1609.34, "mi");

    private final double toMeter;
    private final String symbol;

    LengthUnit(double toMeter, String symbol) {
        this.toMeter = toMeter;
        this.symbol = symbol;
    }

    public double toBase(double value) {
        return value * toMeter;
    }

    public double fromBase(double value) {
        return value / toMeter;
    }

    public String getSymbol() {
        return symbol;
    }

    public static LengthUnit fromString(String input) {
        for (LengthUnit unit : values()) {
            if (unit.name().equalsIgnoreCase(input) || unit.symbol.equalsIgnoreCase(input)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid LengthUnit: " + input);
    }
}
