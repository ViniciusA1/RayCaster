package com.raycaster.entidades;

/**
 *
 * @author vinicius
 */
public class Hitbox {
    private double x;
    private double y;
    private final double largura;

    public Hitbox(double x, double y, double largura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLargura() {
        return largura;
    }

    public void setX(double dx) {
        this.x += dx;
    }

    public void setY(double dy) {
        this.y += dy;
    }
}
