package com.femiproject.unitconverter;

public class UnitConverter {

    public static double convertLength(double value, LengthUnit from, LengthUnit to) {
        double inMeters = from.toBase(value);
        return to.fromBase(inMeters);
    }

    public static double convertWeight(double value, WeightUnit from, WeightUnit to) {
        double inKg = from.toBase(value);
        return to.fromBase(inKg);
    }

    public static double convertTemperature(double value, TemperatureUnit from, TemperatureUnit to) {
        double inCelsius = from.toCelsius(value);
        return to.fromCelsius(inCelsius);
    }
}
