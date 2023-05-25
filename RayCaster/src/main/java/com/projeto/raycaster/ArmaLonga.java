package com.projeto.raycaster;

/**
 *
 * @author vinic
 */
public class ArmaLonga extends Item {
    private final int municaoMaxima;
    private int municaoAtual;
    private int tamanhoPente;
    private int balasNoPente;

    public ArmaLonga(String nome, int municaoMaxima, int municaoAtual, int tamanhoPente, long cooldown) {
        super(nome, cooldown);
        this.municaoMaxima = municaoMaxima;
        this.municaoAtual = municaoMaxima / 2;
        this.tamanhoPente = tamanhoPente;
        this.balasNoPente = tamanhoPente;
    }
    
    @Override
    public int getAtributoConsumivel() {
        return balasNoPente;
    }
    
    @Override
    public boolean isUsavel() {
        return (balasNoPente > 0);
    }
    
    @Override
    public void usar() {
        balasNoPente--;
    }
    
    @Override
    public boolean isRecarregavel() {
        return (municaoAtual > 0 && balasNoPente < tamanhoPente);
    }
    
    public void recarregar() {
        int quantidadeDeRecarga = tamanhoPente - balasNoPente;
        
        if(municaoAtual >= quantidadeDeRecarga) {
            municaoAtual -= quantidadeDeRecarga;
            balasNoPente += quantidadeDeRecarga;
        }
        else {
            balasNoPente += municaoAtual;
            municaoAtual = 0;
        }
    }
}
