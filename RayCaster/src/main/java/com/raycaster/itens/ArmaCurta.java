package com.raycaster.itens;

/**
 *
 * @author vinic
 */
public class ArmaCurta extends Arma {
    private final int durabilidadeMaxima;
    private int durabilidadeAtual;
    private static final int taxaQuebra = 1;

    public ArmaCurta(String nome, int durabilidadeMaxima, long cooldown, double dano) {
        super(nome, cooldown, dano);
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
    public boolean isUsavel() {
        return (durabilidadeAtual > 0);
    }
    
    @Override
    public void usar() {
        durabilidadeAtual--;
    }
    
    @Override
    public boolean isRecarregavel() {
        return false;
    }
}
