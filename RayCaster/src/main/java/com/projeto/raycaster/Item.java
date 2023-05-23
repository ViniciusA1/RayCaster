package com.projeto.raycaster;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author vinic
 */
public abstract class Item {
    private List<ImageIcon> sprite;
    private static final String PATH = "modelos/itens/";
    private long cooldown;

    public Item(String nome, long cooldown) {
        sprite = new ArrayList<>();
        this.cooldown = cooldown;
        carregaSprites(nome);
    }
    
    private void carregaSprites(String nomeArquivo) {
        File arquivo = new File(PATH + nomeArquivo);
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
    
    public ImageIcon getSprite() {
        return sprite.get(0);
    }
    
    public abstract void usar(int coordX, int coordY);
    
    public abstract int getAtributoConsumivel();
}
