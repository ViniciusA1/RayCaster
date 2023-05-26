package com.projeto.raycaster;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author vinic
 */
public abstract class Item {
    private String nome;
    private long cooldown;
    private Map<Estado, Animacao> mapaAnimacao;
    private EfeitosSonoros sons;

    public Item(String nome, long cooldown) {
        mapaAnimacao = new HashMap<>();
        this.nome = nome;
        this.cooldown = cooldown;
        sons = new EfeitosSonoros(nome);
        carregaAnimacoes();
    }
    
    private void carregaAnimacoes() {
        mapaAnimacao.put(Estado.OCIOSO, new Animacao(Diretorio.SPRITE_ITENS + nome, Estado.OCIOSO));
        mapaAnimacao.put(Estado.USANDO, new Animacao(Diretorio.SPRITE_ITENS + nome, Estado.USANDO));
        mapaAnimacao.put(Estado.RECARREGANDO, new Animacao(Diretorio.SPRITE_ITENS + nome, Estado.RECARREGANDO));
    }
    
    public long getCooldown() {
        return cooldown;
    }
    
    public String getNome() {
        return nome;
    }
    
    public ImageIcon getSprite(Estado estadoAtual) {
        return mapaAnimacao.get(estadoAtual).getSprite();
    }
    
    public Animacao getAnimacao(Estado estadoAnimacao) {
        return mapaAnimacao.get(estadoAnimacao);
    }
    
    public void reproduzSom(Estado estadoDesejado) {
        sons.emiteSom(estadoDesejado);
    }
    
    public abstract void usar();
    
    public abstract int getAtributoConsumivel();
    
    public abstract boolean isUsavel();
    
    public abstract boolean isRecarregavel();
}
