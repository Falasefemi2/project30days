package com.femiproject.unitconverter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Unit Converter (type 'exit' to quit)");
            System.out.println("Format: <value> <from_unit> to <to_unit>");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    String[] parts = input.split("\\s+");
                    if (parts.length != 4 || !parts[2].equalsIgnoreCase("to")) {
                        System.out.println("Invalid format. Example: 10 cm to inch");
                        continue;
                    }

                    double value = Double.parseDouble(parts[0]);
                    String fromUnit = parts[1];
                    String toUnit = parts[3];

                    if (isLengthUnit(fromUnit) && isLengthUnit(toUnit)) {
                        LengthUnit from = LengthUnit.fromString(fromUnit);
                        LengthUnit to = LengthUnit.fromString(toUnit);
                        double result = UnitConverter.convertLength(value, from, to);
                        System.out.printf("%.4f %s = %.4f %s%n", value, from.getSymbol(), result, to.getSymbol());
                    } else if (isWeightUnit(fromUnit) && isWeightUnit(toUnit)) {
                        WeightUnit from = WeightUnit.fromString(fromUnit);
                        WeightUnit to = WeightUnit.fromString(toUnit);
                        double result = UnitConverter.convertWeight(value, from, to);
                        System.out.printf("%.4f %s = %.4f %s%n", value, from.getSymbol(), result, to.getSymbol());
                    } else if (isTemperatureUnit(fromUnit) && isTemperatureUnit(toUnit)) {
                        TemperatureUnit from = TemperatureUnit.fromString(fromUnit);
                        TemperatureUnit to = TemperatureUnit.fromString(toUnit);
                        double result = UnitConverter.convertTemperature(value, from, to);
                        System.out.printf("%.2f %s = %.2f %s%n", value, from.getSymbol(), result, to.getSymbol());
                    } else {
                        System.out.println("Incompatible units. Make sure both are of the same type.");
                    }

                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static boolean isLengthUnit(String unit) {
        try {
            LengthUnit.fromString(unit);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isWeightUnit(String unit) {
        try {
            WeightUnit.fromString(unit);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isTemperatureUnit(String unit) {
        try {
            TemperatureUnit.fromString(unit);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
