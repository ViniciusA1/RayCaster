package com.raycaster.entidades.inimigos;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 *
 * @author vinicius
 */
public class Atirador extends Inimigo {
    
    public Atirador(String nome, double vidaMaxima, double largura, 
            double velocidade, double FOG, 
            EnumSet<Estado> possiveisEstados) {
        
        super(nome, vidaMaxima, largura, velocidade, 
                FOG, possiveisEstados);
    }
    
}
