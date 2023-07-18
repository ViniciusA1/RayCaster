package com.raycaster.itens;

import com.raycaster.engine.Estado;
import com.raycaster.entidades.Entidade;
import com.raycaster.mapa.Mapa;
import java.util.EnumSet;

/**
 * Classe que contém os atributos e métodos de cada arma de curto alcance do 
 * jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class ArmaCurta extends Arma {

    private final int durabilidadeMaxima;
    private int durabilidadeAtual;
    private static final int TAXA_QUEBRA = 1;

    /**
     * Construtor da classe que recebe os atributos necessários para criação
     * da arma de curto alcance.
     * @param nome Nome da arma
     * @param durabilidadeMaxima Durabilidade máxima da arma
     * @param possiveisEstados Possíveis estados da arma
     * @param cooldown "Cooldown" de uso da arma
     * @param dano Dano máximo da arma
     * @param range
     */
    public ArmaCurta(String nome, int durabilidadeMaxima, EnumSet<Estado> possiveisEstados,
            long cooldown, double dano, double range) {

        super(nome, cooldown, possiveisEstados, dano, range);
        this.durabilidadeMaxima = durabilidadeMaxima;
        this.durabilidadeAtual = durabilidadeMaxima;
    }

    /**
     * Devolve a durabilidade máxima vinculada à arma.
     * @return Retorna a durabilidade máxima
     */
    public int getDurabilidadeMaxima() {
        return durabilidadeMaxima;
    }

    /**
     * Devolve o atributo consumível da arma visível ao jogador.
     * @return Retorna o atributo consumível
     */
    @Override
    public int getAtributoConsumivel() {
        return durabilidadeAtual;
    }
    
    /**
     * Devolve o máximo do atributo consumível da arma.
     * @return Retorna o máximo valor do atributo
     */
    @Override
    public int getConsumivelMax() {
        return (int) (((double) durabilidadeAtual / durabilidadeMaxima) * 100.0);
    }

    /**
     * Devolve a "taxa de quebra" vinculada à arma.
     * @return Retorna a "taxa de quebra"
     */
    public static int getTaxaQuebra() {
        return TAXA_QUEBRA;
    }

    /**
     * Verifica se a arma está apta a uso.
     * @return Retorna true ou false para a verificação
     */
    @Override
    public boolean isUsavel() {
        return (durabilidadeAtual > 0);
    }

    /**
     * Usa a arma de acordo com sua durabilidade atual.
     */
    @Override
    public void usar(Entidade usuario, Mapa mapaAtual) {
        int hit = verificaHit(usuario, mapaAtual);
        
        if(hit >= 0)
            return;
        
        durabilidadeAtual -= TAXA_QUEBRA;
        
        Entidade sofreDano = mapaAtual.getEntidade(-hit - 2);
        sofreDano.recebeDano(mapaAtual, getDano());
    }

    /**
     * Verifica se a arma está apta à recarga.
     * @return Retorna true ou false para a verificação
     */
    @Override
    public boolean isRecarregavel() {
        return (contemEstado(Estado.RECARREGANDO)
                && durabilidadeAtual < durabilidadeMaxima);
    }

    /**
     * 
     */
    @Override
    public void recarregar() {

    }
}
