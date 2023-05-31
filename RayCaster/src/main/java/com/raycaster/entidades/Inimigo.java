package com.raycaster.entidades;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 *
 * @author vinicius
 */
public class Inimigo extends Entidade {
    public Inimigo(double vidaMaxima, double x, double y, double largura, double velocidade, 
            double fov, double FOG, EnumSet<Estado> possiveisEstados) {
        
        super(vidaMaxima, x, y, largura, velocidade, fov, FOG, possiveisEstados);
    }
    
    
}
