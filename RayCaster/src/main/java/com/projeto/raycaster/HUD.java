package com.projeto.raycaster;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author vinicius
 */
public class HUD extends JPanel {
    private final static String PATH = "modelos" + File.separator + "hud" + File.separator;
    private List<BufferedImage> sprite;
    private Player jogador;

    public HUD(Player jogador) {
        sprite = new ArrayList<>();
        this.jogador = jogador;
        carregaSprites();
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
            
            try {
                BufferedImage imagem = ImageIO.read(new File(PATH + nomeImagem));
                sprite.add(imagem);
            } catch (IOException ex) {
                return;
            }
        }
        
        leitor.close();
    }
    
    public BufferedImage getSprite(int index) {
        return sprite.get(index);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(sprite.get(0), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
