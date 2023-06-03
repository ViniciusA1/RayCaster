package com.raycaster.interfaces;

import com.raycaster.engine.Estado;
import com.raycaster.itens.Item;
import com.raycaster.entidades.Player;
import com.raycaster.engine.Animacao;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;



/**
 * Classe que contém os atributos e métodos do painel de animações do player.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class AnimacaoPlayer extends JPanel {
    private final Player jogador;
    private Animacao animacaoAtual;
    private long tempoAnterior;
    private long tempoAtual;
    private int frameAtual = 0;
    
    /**
     * Construtor da classe que recebe a referência para o jogador ao qual o painel
     * está associado.
     * @param jogador  Jogador ao qual o painel está associado
     */
    public AnimacaoPlayer(Player jogador) {
        this.jogador = jogador;
        this.setOpaque(false);
    }
    
    /**
     * Troca a animação do painel quando ocorre uma troca de item.
     * @param novoItem Novo item que foi trocado
     */
    public void trocaItem(Item novoItem) {
        animacaoAtual = novoItem.getAnimacao(Estado.OCIOSO);
        frameAtual = 0;
    }
    
    /**
     * "Seta" uma nova animação para ser desenhada no painel.
     * @param novaAnimacao Nova animação recebida
     */
    public void setAnimacao(Animacao novaAnimacao) {
        animacaoAtual = novaAnimacao;
        frameAtual = 0;
    }
    
    /**
     * Renderiza o painel na tela de forma customizada.
     * @param g Componente gráfico que renderiza imagens
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        tempoAtual = System.currentTimeMillis();
        
        ImageIcon imagemAtual = animacaoAtual.getSprite();
        int comprimento = animacaoAtual.getFrameWidth();
        int altura = animacaoAtual.getFrameHeight();
        
        g.drawImage(imagemAtual.getImage(), (int) jogador.getPitch(), (int) jogador.getPitch(), 
                    this.getWidth(), this.getHeight(), frameAtual * comprimento, 
                    0, (frameAtual + 1) * comprimento, altura, null);
     
        
        if(tempoAtual - tempoAnterior >= animacaoAtual.getVelocidadeFrame()) {
            frameAtual = (frameAtual + 1) % animacaoAtual.getQuantidadeFrames();
            tempoAnterior = tempoAtual;
            
            if(frameAtual == 0) {
                jogador.setEstado(Estado.OCIOSO);
                animacaoAtual = jogador.getItemAtual().getAnimacao(Estado.OCIOSO);
            }
        }
    }
}
