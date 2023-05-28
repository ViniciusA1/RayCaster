package com.raycaster.entidades;

import java.awt.Rectangle;



/**
 *
 * @author vinic
 */
public abstract class Entidade {
    private final Hitbox hitbox;
    private double velocidade;
    private double fov;
    private double vidaMaxima;
    private double vidaAtual;

    public Entidade(double vidaMaxima, double x, double y, double velocidade, double fov) {
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        
        this.fov = Math.toRadians(fov);
        
        hitbox = new Hitbox(x, y, 16, 16);
    }

    public double getX() {
        return hitbox.getX();
    }

    public double getY() {
        return hitbox.getY();
    }
    
    public double getWidth() {
        return hitbox.getWidth();
    }
    
    public double getHeight() {
        return hitbox.getHeight();
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
    
    public void moveX(double deltaX) {
        hitbox.setX(deltaX);
    }
    
    public void moveY(double deltaY) {
        hitbox.setY(deltaY);
    }
}
