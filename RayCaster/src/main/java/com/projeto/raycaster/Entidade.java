package com.projeto.raycaster;



/**
 *
 * @author vinic
 */
public abstract class Entidade {
    private double x;
    private double y;
    private double velocidade;
    private double fov;
    private double vidaMaxima;
    private double vidaAtual;

    public Entidade(double vidaMaxima, double x, double y, double velocidade, double fov) {
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        this.x = x;
        this.y = y;
        this.fov = Math.toRadians(fov);
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
    
    public double getFov() {
        return fov;
    }
    
    public double getVidaMaxima() {
        return vidaMaxima;
    }
    
    public double getVidaAtual() {
        return vidaAtual;
    }
    
    public void setXY(double deltaX, double deltaY) {
        x += deltaX;
        y += deltaY;
    }
}
