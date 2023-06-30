package com.raycaster.itens;

import com.raycaster.engine.Estado;
import java.util.EnumSet;

/**
 * Classe que contém os atributos e métodos das armas de longo alcance do jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class ArmaLonga extends Arma {
    private final int municaoMaxima;
    private int municaoAtual;
    private int tamanhoPente;
    private int balasNoPente;

    /**
     * Construtor da classe que recebe todos os atributos necessários para a criação
     * da arma de longo alcance.
     * @param nome Nome da arma
     * @param municaoMaxima Quantidade máxima de munição
     * @param tamanhoPente Quantidade máxima de munição suportada pelo pente
     * @param possiveisEstados Possíveis estados da arma
     * @param cooldown "Cooldown" de uso da arma
     * @param dano Dano máximo da arma
     */
    public ArmaLonga(String nome, int municaoMaxima, int tamanhoPente, EnumSet<Estado> possiveisEstados, 
            long cooldown, double dano) {
        super(nome, cooldown, possiveisEstados, dano);
        this.municaoMaxima = municaoMaxima;
        this.municaoAtual = municaoMaxima / 2;
        this.tamanhoPente = tamanhoPente;
        this.balasNoPente = tamanhoPente;
    }
    
    /**
     * Devolve o atributo consumível da arma visível ao player
     * @return Retorna o atributo consumível
     */
    @Override
    public int getAtributoConsumivel() {
        return balasNoPente;
    }
    
    /**
     * Devolve o máximo de um atributo consumível da arma.
     * @return Retorna o máximo do atributo
     */
    @Override
    public int getConsumivelMax() {
        return (int) (((double) municaoAtual / municaoMaxima) * 100.0);
    }
    
    /**
     * Verifica se a arma está apta a ser utilizada.
     * @return Retorna true ou false para a verificação
     */
    @Override
    public boolean isUsavel() {
        return (balasNoPente > 0);
    }
    
    /**
     * Usa a arma com base na quantidade de balas restantes no pente.
     */
    @Override
    public void usar() {
        balasNoPente--;
    }
    
    /**
     * Verifica se a arma está apta para a recarga.
     * @return Retorna true ou false para a verificação
     */
    @Override
    public boolean isRecarregavel() {
        return (contemEstado(Estado.RECARREGANDO) && 
                municaoAtual > 0 && balasNoPente < tamanhoPente);
    }
    
    /**
     * Recarrega a arma com base na quantidade de munição restante.
     */
    @Override
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
