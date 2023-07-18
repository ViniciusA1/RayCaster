package com.raycaster.entidades.inimigos;

import com.raycaster.engine.Estado;
import com.raycaster.entidades.Entidade;
import com.raycaster.mapa.Mapa;
import com.raycaster.utils.MathUtils;
import java.util.EnumSet;

/**
 *
 * @author vinicius
 */
public abstract class Inimigo extends Entidade {
    
    public Inimigo(String nome, double vidaMaxima, double largura, 
            double velocidade, double FOG, 
            EnumSet<Estado> possiveisEstados) {
        
        super(nome, vidaMaxima, 0, 0, largura, velocidade,
                FOG, possiveisEstados);
    }
    
    public void move(Mapa mapaAtual, double playerX, double playerY, 
            double deltaTime) {
        
        double dx = playerX - getX();
        double dy = playerY - getY();
        
        double norma = MathUtils.inverseSqrt(dx*dx + dy*dy);
        
        if(1 / norma > getFOG())
            return;
        
        dx *= getVelocidade() * norma * deltaTime;
        dy *= getVelocidade() * norma * deltaTime;
        
        trataColisao(mapaAtual, dx, dy);
    }
    
}
