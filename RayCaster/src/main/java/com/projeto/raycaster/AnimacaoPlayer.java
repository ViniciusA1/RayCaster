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
    private Item itemAtual;
    private Player jogador;
    private int frameAtual = 0;
    
    public AnimacaoPlayer(Player jogador) {
        this.jogador = jogador;
        this.setOpaque(false);
    }
    
    public void trocaItem(Item novoItem) {
        itemAtual = novoItem;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(itemAtual.getSprite().getImage(), (int) jogador.getPitch(), (int) jogador.getPitch(), 
                this.getParent().getWidth() / 2, this.getParent().getHeight() / 2, 
                frameAtual * 107, 0, (frameAtual + 1) * 107, 107, this);
        
        //frameAtual = (frameAtual + 1) % 18;
    }
}
