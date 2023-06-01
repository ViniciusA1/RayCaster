package com.raycaster.interfaces;

import com.raycaster.engine.Estado;
import com.raycaster.itens.Item;
import com.raycaster.entidades.Player;
import com.raycaster.engine.Animacao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;



/**
 *
 * @author vinicius
 */
public class AnimacaoPlayer extends JPanel {
    private final Player jogador;
    private Animacao animacaoAtual;
    private long tempoAnterior;
    private long tempoAtual;
    private int frameAtual = 0;
    
    public AnimacaoPlayer(Player jogador) {
        this.jogador = jogador;
        this.setOpaque(false);
    }
    
    public void trocaItem(Item novoItem) {
        animacaoAtual = novoItem.getAnimacao(Estado.OCIOSO);
        frameAtual = 0;
    }
    
    public void setAnimacao(Animacao novaAnimacao) {
        animacaoAtual = novaAnimacao;
        frameAtual = 0;
    }
    
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
