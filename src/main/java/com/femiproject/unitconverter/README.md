# 📏 Unit Converter CLI in Java

This project is a **command-line unit conversion tool** built with Java. It supports **length**, **weight**, and **temperature** conversions between metric and imperial units. It is designed to be modular, extensible, and beginner-friendly — ideal for anyone looking to understand enums, object-oriented principles, and CLI design in Java.

---

## 🚀 Features

- ✅ Convert between multiple **Length units** (cm, mm, m, km, inch, foot, yard, mile)
- ✅ Convert between **Weight units** (mg, g, kg, oz, lb)
- ✅ Convert between **Temperature units** (Celsius, Fahrenheit, Kelvin)
- ✅ CLI-based interface with flexible input format
- ✅ Type-safe unit representation using Java `enum`
- ✅ Built-in unit tests using **JUnit 5**

---

## 📦 Technologies Used

- Java 17+
- JUnit 5 (for testing)
- Maven for dependency management and build
- Enum-based type modeling

---

## 🧠 Why `enum`?

This project makes extensive use of Java's `enum` type to represent all supported units.

### ✅ Benefits:

- **Type Safety:** Only valid units can be passed to conversion functions.
- **Centralized Logic:** Each enum constant carries its own logic (e.g., conversion factor or symbol), keeping the logic self-contained.
- **Ease of Extension:** Add a new unit by simply adding a new enum value (e.g., add `NANOMETER` to `LengthUnit`).
- **Readable Code:** Instead of parsing raw strings like `"cm"` everywhere, the code uses `LengthUnit.CENTIMETER`, which is more descriptive and less error-prone.

Example:

```java
LengthUnit from = LengthUnit.CENTIMETER;
LengthUnit to = LengthUnit.INCH;
double result = UnitConverter.convertLength(100, from, to);

```
