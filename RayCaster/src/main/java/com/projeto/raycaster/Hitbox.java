package com.projeto.raycaster;

/**
 *
 * @author vinicius
 */
public class Hitbox {
    private double x;
    private double y;
    private final double width;
    private final double height;

    public Hitbox(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double dx) {
        this.x += dx;
    }

    public void setY(double dy) {
        this.y += dy;
    }
}
