package com.femiproject.threeD;

public class Matrix3 {

    double[] values;

    Matrix3(double[] values) {
        this.values = values.clone();
    }

    public Matrix3 multiply(Matrix3 other) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                double sum = 0;
                for (int i = 0; i < 3; i++) {
                    sum += this.values[row * 3 + i] * other.values[i * 3 + col];
                }
                result[row * 3 + col] = sum;
            }
        }
        return new Matrix3(result);
    }

    public Vertex transform(Vertex v) {
        return new Vertex(
                v.x * values[0] + v.y * values[1] + v.z * values[2],
                v.x * values[3] + v.y * values[4] + v.z * values[5],
                v.x * values[6] + v.y * values[7] + v.z * values[8]);
    }
}
