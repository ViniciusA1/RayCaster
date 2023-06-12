package com.raycaster.interfaces;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author vinicius
 */
public class LabelAnimado extends JLabel {

    private Timer timerAnimacao;

    private int direcao;
    private int posicaoY;

    private float alpha;
    private float delta;
    
    private Runnable ajuste;
    
    public enum Animacao {
        FLOAT,
        FADE
    }

    public LabelAnimado(String texto, Animacao tipo) {
        super(texto);
        this.setForeground(Color.RED);
        this.setAlignmentX(CENTER_ALIGNMENT);

        escolheAnimacao(tipo);
    }

    public LabelAnimado(String texto, Font fonteCustomizada, Animacao tipo) {
        this(texto, tipo);

        this.setFont(fonteCustomizada);
    }

    public LabelAnimado(String texto, Font fonteCustomizada, Animacao tipo, Color corTexto) {
        this(texto, fonteCustomizada, tipo);

        this.setForeground(corTexto);
    }
    
    private void escolheAnimacao(Animacao tipo) {
        switch(tipo) {
            case FLOAT -> {
                posicaoY = 0;
                direcao = 1;
                ajuste = this::ajusteFloat;
                timerAnimacao = new Timer(60, e -> animacaoFloat());
            }
            case FADE -> {
                alpha = 0f;
                delta = 0.01f;
                ajuste = this::ajusteFade;
                timerAnimacao = new Timer(10, e -> animacaoFade());
            }
        }

        timerAnimacao.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ajuste.run();
    }

    private void animacaoFloat() {
        posicaoY = getY() + direcao;

        if (posicaoY <= 0) {
            posicaoY = 0;
            direcao = 1;
        } else if (posicaoY + getHeight() >= 200) {
            posicaoY = 200 - getHeight();
            direcao = -1;
        }
        
        ajusteFloat();
    }
    
    private void ajusteFloat() {
        Container parent = getParent();

        if (parent != null) {
            setLocation((parent.getWidth() - getWidth()) / 2, posicaoY);
        }
    }

    private void animacaoFade() {
        alpha += delta;
        if (alpha >= 1f) {
            alpha = 1f;
            delta = -delta;
        } else if (alpha <= 0f) {
            alpha = 0f;
            delta = -delta;
        }

        ajusteFade();
    }
    
    private void ajusteFade() {
        setForeground(new Color(1f, 0f, 0f, alpha));
    }

    public void close() {
        timerAnimacao.stop();
    }
}
