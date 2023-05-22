package com.projeto.raycaster;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author vinic
 */
public abstract class Item {
    private List<BufferedImage> sprite;
    private static final String PATH = "modelos/itens/";
    private double cooldown;

    public Item(String nome, double cooldown) {
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
            
            try {
                BufferedImage imagem = ImageIO.read(new File(PATH + nomeImagem));
                sprite.add(imagem);
            } catch (IOException ex) {
                return;
            }
        }
        
        leitor.close();
    }
    
    public BufferedImage getSprite() {
        return sprite.get(0);
    }
    
    public abstract void usar();
}
