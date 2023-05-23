package com.projeto.raycaster;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vinicius
 */
public class HUD extends JPanel {
    private final static String PATH = "modelos" + File.separator + "hud" + File.separator;
    private List<ImageIcon> sprite;
    private Player jogador;
    private JLabel textoVida;
    private JLabel textoMunicao;

    public HUD(Player jogador) {
        setLayout(null);
        sprite = new ArrayList<>();
        this.jogador = jogador;
        
        Font fontePersonalizada = carregaFonte();
        
        carregaComponentes(fontePersonalizada);
        carregaSprites();
    }
    
    private Font carregaFonte() {   
        Font fonteCustomizada = null;
        
        try {
            fonteCustomizada = Font.createFont(Font.TRUETYPE_FONT, new File(PATH + "font.ttf"));
        } catch (FontFormatException | IOException ex) {
            
        }
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(fonteCustomizada);
        
        return fonteCustomizada;
    }
    
    public void carregaComponentes(Font fonteCustom) {
        textoVida = new JLabel(Double.toString(jogador.getVidaAtual()));
        textoVida.setFont(fonteCustom.deriveFont(50f));
        textoVida.setForeground(Color.WHITE);
        textoVida.setLocation(245, 15);
        textoVida.setSize(150, 60);
        
        textoMunicao = new JLabel(Integer.toString(jogador.getQtdConsumivel()));
        textoMunicao.setFont(fonteCustom.deriveFont(50f));
        textoMunicao.setForeground(Color.WHITE);
        textoMunicao.setLocation(70, 15);
        textoMunicao.setSize(150, 60);
        
        add(textoVida);
        add(textoMunicao);
    }
    
    private void carregaSprites() {
        File arquivo = new File(PATH + "hud.txt");
        Scanner leitor;
        
        try {
            leitor = new Scanner(arquivo);
        } catch(FileNotFoundException erro) {
            return;
        }
        
        while(leitor.hasNext()) {
            String nomeImagem = leitor.nextLine();
            
            ImageIcon imagem = new ImageIcon(PATH + nomeImagem);
            sprite.add(imagem);
        }
        
        leitor.close();
    }
    
    public ImageIcon getSprite(int index) {
        return sprite.get(index);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(sprite.get(0).getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
