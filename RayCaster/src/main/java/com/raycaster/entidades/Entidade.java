package com.raycaster.entidades;

import com.raycaster.engine.Estado;
import java.awt.Rectangle;
import java.util.EnumSet;



/**
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public abstract class Entidade {
    private final Hitbox hitbox;
    private double velocidade;
    private double fov;
    private final double FOG;
    private double vidaMaxima;
    private double vidaAtual;
    private EnumSet<Estado> possiveisEstados;

    public Entidade(double vidaMaxima, double x, double y, double largura, 
            double velocidade, double fov, double FOG, EnumSet<Estado> possiveisEstados) {
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        
        this.fov = Math.toRadians(fov);
        this.FOG = FOG;
        
        this.possiveisEstados = possiveisEstados;
        
        hitbox = new Hitbox(x, y, largura);
    }
    
    public Entidade() {
        hitbox = null;
        FOG = 0;
    }

    public double getX() {
        return hitbox.getX();
    }

    public double getY() {
        return hitbox.getY();
    }
    
    public double getLargura() {
        return hitbox.getLargura();
    }

    public double getVelocidade() {
        return velocidade;
    }
    
    public double getFov() {
        return fov;
    }
    
    public double getFOG() {
        return FOG;
    }
    
    public double getVidaMaxima() {
        return vidaMaxima;
    }
    
    public double getVidaAtual() {
        return vidaAtual;
    }
    
    public boolean contemEstado(Estado estadoRecebido) {
        return possiveisEstados.contains(estadoRecebido);
    }
    
    public void moveX(double deltaX) {
        hitbox.setX(deltaX);
    }
    
    public void moveY(double deltaY) {
        hitbox.setY(deltaY);
    }
}
