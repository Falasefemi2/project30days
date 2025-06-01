package com.femiproject.unitconverter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UnitConverterTest {

    @Test
    public void testLength_cmToInch() {
        double result = UnitConverter.convertLength(100, LengthUnit.CENTIMETER, LengthUnit.INCH);
        assertEquals(39.3701, result, 0.0001);
    }

    @Test
    public void testLength_kmToMile() {
        double result = UnitConverter.convertLength(1, LengthUnit.KILOMETER, LengthUnit.MILE);
        assertEquals(0.621371, result, 0.0001);
    }

    @Test
    public void testLength_yardToMeter() {
        double result = UnitConverter.convertLength(1, LengthUnit.YARD, LengthUnit.METER);
        assertEquals(0.9144, result, 0.0001);
    }

    @Test
    public void testWeight_kgToLb() {
        double result = UnitConverter.convertWeight(2, WeightUnit.KILOGRAM, WeightUnit.POUND);
        assertEquals(4.40924, result, 0.0001);
    }

    @Test
    public void testWeight_ozToGram() {
        double result = UnitConverter.convertWeight(1, WeightUnit.OUNCE, WeightUnit.GRAM);
        assertEquals(28.3495, result, 0.0001);
    }

    @Test
    public void testTemperature_celsiusToFahrenheit() {
        double result = UnitConverter.convertTemperature(0, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT);
        assertEquals(32.0, result, 0.01);
    }

    @Test
    public void testTemperature_fahrenheitToKelvin() {
        double result = UnitConverter.convertTemperature(32, TemperatureUnit.FAHRENHEIT, TemperatureUnit.KELVIN);
        assertEquals(273.15, result, 0.01);
    }

    @Test
    public void testTemperature_kelvinToCelsius() {
        double result = UnitConverter.convertTemperature(300, TemperatureUnit.KELVIN, TemperatureUnit.CELSIUS);
        assertEquals(26.85, result, 0.01);
    }

    @Test
    public void testRoundTrip_length() {
        double original = 123.45;
        double meters = LengthUnit.FOOT.toBase(original);
        double roundTrip = LengthUnit.FOOT.fromBase(meters);
        assertEquals(original, roundTrip, 0.0001);
    }
}
