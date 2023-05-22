package com.projeto.raycaster;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



/**
 *
 * @author vinic
 */
public class Player extends Entidade {
    private double angulo;
    private double pitch;
    private double taxa;
    private Inventario<Item> mochila;
    private Item itemAtual;
    private Clip clip;
    
    public Player(double vidaMaxima, double x, double y, double velocidade, int fov) {
        super(vidaMaxima, x, y, velocidade, fov);
        angulo = 0;
        pitch = 0;
        taxa = 2;
        mochila = new Inventario<>();
    }
    
    public double getAngulo() {
        return angulo;
    }
    
    public double getPitch() {
        return pitch;
    }
    
    public void move(double anguloRelativo, int sinal, Mapa mapaAtual) {
        double dx = sinal * super.getVelocidade() * Math.cos(angulo + anguloRelativo);
        double dy = sinal * super.getVelocidade() * Math.sin(angulo + anguloRelativo);
        
        pitch += taxa;
        
        if(pitch >= 32 || pitch <= -32) {
            taxa *= -1;
        }
        
        if(!checaColisao(mapaAtual, dx + super.getX(), dy + super.getY()))
            setXY(dx, dy);
    }
    
    public void rotaciona(double deltaX) {
        angulo += deltaX;
    }
    
    public boolean checaColisao(Mapa mapaAtual, double posX, double posY) {
        return (mapaAtual.checaColisao((int) posX, (int) posY));
    }
    
    public void adicionaItem(Item novoItem) {
        mochila.guardaObjeto(novoItem);
    }

    public void sacaItem(int index) {
        if(clip == null) {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sons/holster.wav"));
                clip.open(audioInputStream);
            
                itemAtual = mochila.getObjeto(index);
                // Reproduz o som
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(!clip.isRunning()) {
            clip.close();
            clip = null;
        }
    }
    
    public BufferedImage getFrameAtual() {
        return itemAtual.getSprite();
    }
}
