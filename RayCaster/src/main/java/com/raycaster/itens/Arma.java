package com.raycaster.itens;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 * Classe que contém os atributos e métodos de cada arma do jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public abstract class Arma extends Item {
    private double dano;
    
    /**
     * Construtor da classe que recebe os atributos necessários para criação
     * da arma.
     * @param nome Nome da arma
     * @param cooldown "Cooldown" de uso da arma
     * @param possiveisEstados Possíveis estados da arma
     * @param dano Dano máximo da arma
     */
    public Arma(String nome, long cooldown, EnumSet<Estado> possiveisEstados, double dano) {
        super(nome, cooldown, possiveisEstados);
        
        this.dano = dano;
    }
    
    /**
     * Devolve o dano máximo vinculado à arma.
     * @return Retorna o dano da arma
     */
    public double getDano() {
        return dano;
    }
    
}
