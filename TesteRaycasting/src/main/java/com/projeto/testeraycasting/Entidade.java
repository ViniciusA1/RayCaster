package com.projeto.testeraycasting;

/**
 *
 * @author vinicius
 */
public abstract class Entidade {
    private double x;
    private double y;
    private double velocidade;

    public Entidade(double x, double y, double velocidade) {
        this.x = x;
        this.y = y;
        this.velocidade = velocidade;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocidade() {
        return velocidade;
    }
}
