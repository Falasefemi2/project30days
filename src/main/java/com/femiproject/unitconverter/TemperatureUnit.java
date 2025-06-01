package com.femiproject.unitconverter;

public enum TemperatureUnit {
    CELSIUS("C"),
    FAHRENHEIT("F"),
    KELVIN("K");

    private final String symbol;

    TemperatureUnit(String symbol) {
        this.symbol = symbol;
    }

    public static TemperatureUnit fromString(String input) {
        for (TemperatureUnit unit : values()) {
            if (unit.name().equalsIgnoreCase(input) || unit.symbol.equalsIgnoreCase(input)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid TemperatureUnit: " + input);
    }

    public String getSymbol() {
        return symbol;
    }

    public double toCelsius(double value) {
        switch (this) {
            case CELSIUS -> {
                return value;
            }
            case FAHRENHEIT -> {
                return (value - 32) * 5 / 9;
            }
            case KELVIN -> {
                return value - 273.15;
            }
            default ->
                throw new IllegalStateException("Unknown unit: " + this);
        }
    }

    public double fromCelsius(double value) {
        switch (this) {
            case CELSIUS -> {
                return value;
            }
            case FAHRENHEIT -> {
                return (value * 9 / 5) + 32;
            }
            case KELVIN -> {
                return value + 273.15;
            }
            default ->
                throw new IllegalStateException("Unknown unit: " + this);
        }
    }
}
