package com.projeto.raycaster;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author vinicius
 */
public class Animacao {
    private ImageIcon sprites;
    private long velocidadeFrame;
    private int quantidadeFrames;
    private final static String PATH = "modelos" + File.separator + "itens" + File.separator;
    
    public Animacao(String nome, Estado estadoAnimacao) {
        sprites = new ImageIcon(PATH + nome + File.separator + nome + "-" + estadoAnimacao.name() + ".png");
        this.quantidadeFrames = 13;
        this.velocidadeFrame = 100l;
    }
    
    public ImageIcon getSprite() {
        return sprites;
    }
    
    public int getQuantidadeFrames() {
        return quantidadeFrames;
    }
    
    public long getVelocidadeFrame() {
        return velocidadeFrame;
    }
}
