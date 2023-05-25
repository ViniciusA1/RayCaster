package com.projeto.raycaster;

/**
 *
 * @author vinic
 */
public class ArmaLonga extends Item {
    private final int municaoMaxima;
    private int municaoAtual;
    private int tamanhoPente;

    public ArmaLonga(String nome, int municaoMaxima, int municaoAtual, long cooldown) {
        super(nome, cooldown);
        this.municaoMaxima = municaoMaxima;
        this.municaoAtual = municaoAtual;
    }
    
    @Override
    public int getAtributoConsumivel() {
        return municaoAtual;
    }
    
    @Override
    public void usar(int x, int y) {
        
    }
}
