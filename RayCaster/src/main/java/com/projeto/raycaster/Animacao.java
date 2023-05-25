package com.projeto.raycaster;

import java.io.File;
import java.nio.file.Path;
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
    private int frameWidth;
    private int frameHeight;
    private final static String PATH = "modelos" + File.separator + "itens" + File.separator;

    public Animacao(String nome, Estado estadoAnimacao) {
        String caminho = PATH + nome + File.separator + nome + "-" + estadoAnimacao.name();
        
        sprites = ArquivoUtils.leImagem(caminho + ".png");
        String[] dados = ArquivoUtils.leDados(caminho + ".txt");

        if (dados != null) {
            this.frameWidth = Integer.parseInt(dados[0]);
            this.frameHeight = Integer.parseInt(dados[1]);
            this.quantidadeFrames = Integer.parseInt(dados[2]);
            this.velocidadeFrame = Long.parseLong(dados[3]);
        }
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
    
    public int getFrameWidth() {
        return frameWidth;
    }
    
    public int getFrameHeight() {
        return frameHeight;
    }
}
