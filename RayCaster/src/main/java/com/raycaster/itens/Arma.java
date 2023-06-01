package com.raycaster.itens;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 *
 * @author vinicius
 */
public abstract class Arma extends Item {
    private double dano;
    
    public Arma(String nome, long cooldown, EnumSet<Estado> possiveisEstados, double dano) {
        super(nome, cooldown, possiveisEstados);
        
        this.dano = dano;
    }
    
    public double getDano() {
        return dano;
    }
    
}
