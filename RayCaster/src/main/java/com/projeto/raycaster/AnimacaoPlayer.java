package com.projeto.raycaster;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author vinicius
 */
public class AnimacaoPlayer extends JPanel {
    private Player jogador;
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
    }
    
    public void setAnimacao(Animacao novaAnimacao) {
        animacaoAtual = novaAnimacao;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        tempoAtual = System.nanoTime();
        
        if(tempoAtual - tempoAnterior >= animacaoAtual.getVelocidadeFrame()) {
            frameAtual = (frameAtual + 1) % animacaoAtual.getQuantidadeFrames();
            tempoAnterior = tempoAtual;
            
            if(frameAtual == 0) {
                jogador.setEstado(Estado.OCIOSO);
                animacaoAtual = jogador.getItemAtual().getAnimacao(Estado.OCIOSO);
            }
        }
        
        ImageIcon imagemAtual = animacaoAtual.getSprite();
        int frameWidth = imagemAtual.getIconWidth();
        int frameHeight = imagemAtual.getIconHeight();
        
        g.drawImage(imagemAtual.getImage(), (int) jogador.getPitch(), (int) jogador.getPitch(), 
                this.getParent().getWidth() / 2, this.getParent().getHeight() / 2, 
                frameAtual * frameWidth, 0, (frameAtual + 1) * frameWidth, frameHeight, null);
    }
}
