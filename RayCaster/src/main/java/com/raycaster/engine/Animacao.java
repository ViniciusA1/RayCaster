package com.raycaster.engine;

import com.raycaster.utils.Diretorio;
import com.raycaster.utils.ArquivoUtils;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 * Classe que guarda os atributos e métodos que representam o conjunto de
 * animações de um objeto do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Animacao {

    private final ImageIcon sprites;
    private long velocidadeFrame;
    private int quantidadeFrames;
    private int frameWidth;
    private int frameHeight;
    
    private long tempoAnterior;
    private long tempoAtual;
    private int frameAtual;
    

    /**
     * Construtor da animação, recebe o caminho do arquivo e estado associado
     *
     * @param caminho Caminho do arquivo para ser carregado na animação
     * @param estadoAnimacao Estado do player/item associado a animação
     */
    public Animacao(String caminho, Estado estadoAnimacao) {
        String nomeDeBusca = Diretorio.convertePath(caminho,
                estadoAnimacao);
        sprites = ArquivoUtils.leImagem(nomeDeBusca);

        carregaDados(nomeDeBusca);
    }

    private void carregaDados(String nomeDeBusca) {
        Properties dados = ArquivoUtils.lePropriedade(nomeDeBusca);

        if (dados == null) {
            return;
        }

        this.frameWidth = Integer.parseInt(dados.
                getProperty("frameWidth"));
        this.frameHeight = Integer.parseInt(dados.
                getProperty("frameHeight"));
        this.quantidadeFrames = Integer.parseInt(dados.
                getProperty("quantidadeFrames"));
        this.velocidadeFrame = Long.parseLong(dados.
                getProperty("velocidadeFrame"));
    }

    /**
     * Disponibiliza a referência do sprite para código externo
     *
     * @return Retorna o sprite sheet associado a animação
     */
    public ImageIcon getSprite() {
        return sprites;
    }
    
    public int getFrameAtual() {
        return frameAtual;
    }
    
    public void setFrame(int frameID) {
        if(frameID >= quantidadeFrames)
            return;
        
        this.frameAtual = frameID;
    }
    
    public int atualizaFrame() {
        tempoAtual = System.currentTimeMillis();
        
        if (tempoAtual - tempoAnterior >= velocidadeFrame) {
            frameAtual = (frameAtual + 1) % quantidadeFrames;
            tempoAnterior = tempoAtual;
        }
        
        return frameAtual;
    }
    
    public BufferedImage getSpriteImagem() {
        Image imagemSprite = sprites.getImage();
        
        BufferedImage bufferedImage = new BufferedImage(
                frameWidth, frameHeight, 
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(imagemSprite, 0, 0, frameWidth,
                frameHeight, frameAtual * frameWidth,
                0, (frameAtual + 1) * frameWidth, frameHeight, null);
        g2d.dispose();
        
        return bufferedImage;
    }

    /**
     * Disponibiliza a quantidade máxima de frames para código externo
     *
     * @return Retorna o número total de frames na animação
     */
    public int getQuantidadeFrames() {
        return quantidadeFrames;
    }

    /**
     * Disponibiliza o valor da velocidade de frames para código externo
     *
     * @return Retorna a velocidade entre os frames
     */
    public long getVelocidadeFrame() {
        return velocidadeFrame;
    }

    /**
     * Disponibiliza o valor de largura de frames para código externo
     *
     * @return Retorna o tamanho em largura dos frames
     */
    public int getFrameWidth() {
        return frameWidth;
    }

    /**
     * Disponibiliza o valor de altura de frames para código externo
     *
     * @return Retorna o tamanho em altura dos frames
     */
    public int getFrameHeight() {
        return frameHeight;
    }
}
