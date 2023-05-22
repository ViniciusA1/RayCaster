package com.projeto.raycaster;

/**
 *
 * @author vinic
 */
public class ArmaCurta extends Item {
    private final int durabilidadeMaxima;
    private int durabilidadeAtual;
    private static final int taxaQuebra = 1;

    public ArmaCurta(String nome, int durabilidadeMaxima, double cooldown) {
        super(nome, cooldown);
        this.durabilidadeMaxima = durabilidadeMaxima;
        this.durabilidadeAtual = durabilidadeMaxima;
    }

    public int getDurabilidadeMaxima() {
        return durabilidadeMaxima;
    }

    public int getDurabilidadeAtual() {
        return durabilidadeAtual;
    }

    public static int getTaxaQuebra() {
        return taxaQuebra;
    }
    
    @Override
    public void usar() {
        
    }
    
}
