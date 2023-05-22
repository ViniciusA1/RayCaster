package com.projeto.raycaster;

/**
 *
 * @author vinic
 */
public class ArmaLonga extends Item {
    private int municaoMaxima;
    private int municaoAtual;

    public ArmaLonga(String nome, int municaoMaxima, int municaoAtual, double cooldown) {
        super(nome, cooldown);
        this.municaoMaxima = municaoMaxima;
        this.municaoAtual = municaoAtual;
    }
    
    
    
    public void usar() {
        
    }
}
