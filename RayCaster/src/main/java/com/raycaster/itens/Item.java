package com.raycaster.itens;

import com.raycaster.engine.Animacao;
import com.raycaster.engine.arquivos.Diretorio;
import com.raycaster.engine.sons.EfeitoSonoro;
import com.raycaster.engine.Estado;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Classe que contém os atributos e métodos de cada item do jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public abstract class Item {
    private final String nome;
    private long cooldown;
    private EnumSet<Estado> possiveisEstados;
    private Map<Estado, Animacao> mapaAnimacao;
    private EfeitoSonoro sons;

    /**
     * Construtor da classe que recebe os atributos necessários para criação do
     * item.
     * @param nome Nome do item
     * @param cooldown Tempo de "cooldown" do item
     * @param possiveisEstados Possíveis estados que pode assumir
     */
    public Item(String nome, long cooldown, EnumSet<Estado> possiveisEstados) {
        this.nome = nome;
        this.cooldown = cooldown;
        this.possiveisEstados = possiveisEstados;
        
        sons = new EfeitoSonoro(nome, possiveisEstados);
        mapaAnimacao = new HashMap<>();
        carregaAnimacoes();
    }
    
    /**
     * Carrega todas as animações que possivelmente estão associadas ao item.
     */
    private void carregaAnimacoes() {
        for (Estado estadoAux : possiveisEstados) {
            Animacao novaAnimacao = new Animacao(Diretorio.SPRITE_ITENS + nome, 
                      estadoAux);
            
            mapaAnimacao.put(estadoAux, novaAnimacao);
        }
    }
    
    /**
     * Devolve o cooldown vinculado ao item.
     * @return Retorna o cooldown do item
     */
    public long getCooldown() {
        return cooldown;
    }
    
    /**
     * Devolve o nome vinculado ao item.
     * @return Retorna o nome do item
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Retorna um sprite do item com base no estado enviado.
     * @param estadoAtual Estado em que se encontra o item
     * @return Retorna a referência para o sprite
     */
    public ImageIcon getSprite(Estado estadoAtual) {
        return mapaAnimacao.get(estadoAtual).getSprite();
    }
    
    /**
     * Retorna uma animação do item com base no estado enviado.
     * @param estadoAnimacao Estado de animação que será buscado
     * @return Retorna a referência para a animação
     */
    public Animacao getAnimacao(Estado estadoAnimacao) {
        return mapaAnimacao.get(estadoAnimacao);
    }
    
    /**
     * Reproduz um determinado som do item.
     * @param estadoDesejado Estado associado ao som que deve ser reproduzido
     */
    public void reproduzSom(Estado estadoDesejado) {
        sons.playSom(estadoDesejado);
    }
    
    /**
     * Verifica se o item possui um determinado estado.
     * @param estadoRecebido Estado que está sendo procurado
     * @return Retorna verdadeiro ou falso para a busca
     */
    public boolean contemEstado(Estado estadoRecebido) {
        return possiveisEstados.contains(estadoRecebido);
    }
    
    public void close() {
        mapaAnimacao.clear();
        sons.close();
    }
    
    /**
     * Verifica se o item está ou não apto ao uso.
     * @return Retorna verdadeiro ou falso para a verificação
     */
    public abstract boolean isUsavel();
    
    /**
     * Usa o item desejado.
     */
    public abstract void usar();
    
    /**
     * Verifica se o item está ou não apto à recarga.
     * @return Retorna verdadeiro ou falso para a verificação
     */
    public abstract boolean isRecarregavel();
    
    /**
     * Recarrega o item desejado.
     */
    public abstract void recarregar();
    
    /**
     * Busca e retorna um atributo consumível do item.
     * @return Retorna o atributo consumível
     */
    public abstract int getAtributoConsumivel();
    
    /**
     * Busca e retorna o valor máximo do atributo consumível do item.
     * @return Retorna o valor máximo do atributo consumível
     */
    public abstract int getConsumivelMax();
}
