package com.raycaster.entidades;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 * Classe que contém os atributos e métodos das entidades inimigas do jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Inimigo extends Entidade {
    
    /**
     * Construtor da classe que recebe os atributos necessários para criação
     * da entidade.
     * @param vidaMaxima Vida máxima do inimigo
     * @param x Posição em x
     * @param y Posição em y
     * @param largura Largura da sua hitbox
     * @param velocidade Velocidade do inimigo
     * @param FOG Campo de visão máximo do inimigo
     * @param possiveisEstados Possíveis estados que o inimigo pode assumir
     */
    public Inimigo(double vidaMaxima, double x, double y, double largura, double velocidade, 
            double FOG, EnumSet<Estado> possiveisEstados) {
        
        super(vidaMaxima, x, y, largura, velocidade, FOG, possiveisEstados);
    }
    
    
}
