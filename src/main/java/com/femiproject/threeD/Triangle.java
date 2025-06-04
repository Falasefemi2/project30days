package com.femiproject.threeD;

import java.awt.Color;

public class Triangle {

    Vertex v1;
    Vertex v2;
    Vertex v3;
    Color color;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }
}
