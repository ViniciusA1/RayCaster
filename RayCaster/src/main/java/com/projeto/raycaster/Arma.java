package com.projeto.raycaster;

/**
 *
 * @author vinicius
 */
public abstract class Arma extends Item {
    private double dano;
    
    public Arma(String nome, long cooldown, double dano) {
        super(nome, cooldown);
        
        this.dano = dano;
    }
    
    public double getDano() {
        return dano;
    }
}
