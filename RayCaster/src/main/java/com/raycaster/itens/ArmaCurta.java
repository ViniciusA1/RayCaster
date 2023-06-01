package com.raycaster.itens;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 *
 * @author vinic
 */
public class ArmaCurta extends Arma {

    private final int durabilidadeMaxima;
    private int durabilidadeAtual;
    private static final int TAXA_QUEBRA = 1;

    public ArmaCurta(String nome, int durabilidadeMaxima, EnumSet<Estado> possiveisEstados,
            long cooldown, double dano) {

        super(nome, cooldown, possiveisEstados, dano);
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
        return TAXA_QUEBRA;
    }

    @Override
    public boolean isUsavel() {
        return (durabilidadeAtual > 0);
    }

    @Override
    public void usar() {
        durabilidadeAtual -= TAXA_QUEBRA;
    }

    @Override
    public boolean isRecarregavel() {
        return (contemEstado(Estado.RECARREGANDO)
                && durabilidadeAtual < durabilidadeMaxima);
    }

    @Override
    public void recarregar() {

    }
}
