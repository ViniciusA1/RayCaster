package com.projeto.raycaster;

/**
 *
 * @author vinic
 */
public class ArmaCurta extends Item {
    private final int durabilidadeMaxima;
    private int durabilidadeAtual;
    private static final int taxaQuebra = 1;

    public ArmaCurta(String nome, int durabilidadeMaxima, long cooldown) {
        super(nome, cooldown);
        this.durabilidadeMaxima = durabilidadeMaxima;
        this.durabilidadeAtual = durabilidadeMaxima;
    }

    public int getDurabilidadeMaxima() {
        return durabilidadeMaxima;
    }
    
    @Override
    public int getAtributoConsumivel() {
        return durabilidadeAtual;
    }

    public static int getTaxaQuebra() {
        return taxaQuebra;
    }
    
    @Override
    public void usar(int x, int y) {
        
    }
    
}
