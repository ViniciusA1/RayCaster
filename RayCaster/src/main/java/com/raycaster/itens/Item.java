package com.raycaster.itens;

import com.raycaster.engine.Animacao;
import com.raycaster.engine.Diretorio;
import com.raycaster.engine.EfeitosSonoros;
import com.raycaster.engine.Estado;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author vinic
 */
public abstract class Item {
    private String nome;
    private long cooldown;
    private EnumSet<Estado> possiveisEstados;
    private Map<Estado, Animacao> mapaAnimacao;
    private EfeitosSonoros sons;

    public Item(String nome, long cooldown, EnumSet<Estado> possiveisEstados) {
        this.nome = nome;
        this.cooldown = cooldown;
        this.possiveisEstados = possiveisEstados;
        
        sons = new EfeitosSonoros(nome, possiveisEstados);
        mapaAnimacao = new HashMap<>();
        carregaAnimacoes();
    }
    
    private void carregaAnimacoes() {
        for (Estado estadoAux : possiveisEstados) {
            Animacao novaAnimacao = new Animacao(Diretorio.SPRITE_ITENS + nome, 
                      estadoAux);
            
            mapaAnimacao.put(estadoAux, novaAnimacao);
        }
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
    
    public boolean contemEstado(Estado estadoRecebido) {
        return possiveisEstados.contains(estadoRecebido);
    }
    
    public abstract void usar();
    
    public abstract int getAtributoConsumivel();
    
    public abstract boolean isUsavel();
    
    public abstract boolean isRecarregavel();
}
