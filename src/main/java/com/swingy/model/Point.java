package com.swingy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Point {
    @NotNull(message = "Point.X should not be null")
    @Size(min = 0, message = "Point.x should be greater than 0")
    private int x;

    @NotNull(message = "Point.y should not be null")
    @Size(min = 0, message = "Point.y should be greater than 0")
    private int y;

    // Constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter for x
    public int getX() {
        return x;
    }

    // Setter for x
    public void setX(int x) {
        this.x = x;
    }

    // Getter for y
    public int getY() {
        return y;
    }

    // Setter for y
    public void setY(int y) {
        this.y = y;
    }

    // Method to represent the point as a string
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }
}
