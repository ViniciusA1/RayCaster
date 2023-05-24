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
    private String nome;
    private long cooldown;
    private ImageIcon sprite;
    private final static String PATH = "modelos" + File.separator + "itens" + File.separator; 

    public Item(String nome, long cooldown) {
        sprite = new ImageIcon();
        this.nome = nome;
        this.cooldown = cooldown;
        carregaSprites(nome);
    }
    
    private void carregaSprites(String nomeArquivo) {
        sprite = new ImageIcon(PATH + nomeArquivo + ".png");
    }
    
    public long getCooldown() {
        return cooldown;
    }
    
    public String getNome() {
        return nome;
    }
    
    public ImageIcon getSprite() {
        return sprite;
    }
    
    public abstract void usar(int coordX, int coordY);
    
    public abstract int getAtributoConsumivel();
}
