package com.projeto.raycaster;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

    public Animacao(String caminho, Estado estadoAnimacao) {
        String nomeDeBusca = Diretorio.convertePath(caminho, estadoAnimacao);
        
        sprites = ArquivoUtils.leImagem(nomeDeBusca);
        Properties dados = ArquivoUtils.leDados(nomeDeBusca);

        if (dados != null) {
            this.frameWidth = Integer.parseInt(dados.getProperty("frameWidth"));
            this.frameHeight = Integer.parseInt(dados.getProperty("frameHeight"));
            this.quantidadeFrames = Integer.parseInt(dados.getProperty("quantidadeFrames"));
            this.velocidadeFrame = Long.parseLong(dados.getProperty("velocidadeFrame"));
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
