package com.raycaster.interfaces.componentes;

import com.raycaster.engine.Estado;
import com.raycaster.interfaces.paineis.InterfaceManager;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Classe que guarda os atributos e métodos de um 
 * label com animação customizável.
 * 
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class LabelAnimado extends JLabel implements Interagivel {

    private Timer timerAnimacao;

    private int direcao;
    private int posicaoY;

    private int alpha;
    private int delta;
    
    private Runnable ajuste;
    
    private String[] textos;
    private int idAtual;
    
    /**
     * Enum que representa os tipos de animação disponíveis.
     */
    public enum Animacao {

        /**
         * Animação do tipo "float"
         */
        FLOAT,

        /**
         * Animação do tipo "fade"
         */
        FADE
    }

    /**
     * Construtor principal do label, recebe o texto e tipo de animação
     * desejado.
     * 
     * @param texto Texto do label
     * @param tipo Tipo de animação para ser reproduzido
     */
    public LabelAnimado(String texto, Animacao tipo) {
        super(texto);
        this.setForeground(Color.RED);
        this.setAlignmentX(CENTER_ALIGNMENT);

        escolheAnimacao(tipo);
    }

    /**
     * Construtor auxiliar do label, recebe também uma fonte personalizada para
     * o texto.
     * 
     * @param texto Texto do label
     * @param fonteCustomizada Fonte customizada para o texto
     * @param tipo Tipo de animação para ser reproduzido
     */
    public LabelAnimado(String texto, Font fonteCustomizada, Animacao tipo) {
        this(texto, tipo);

        this.setFont(fonteCustomizada);
    }
    
    public LabelAnimado(String[] textos, int primeiroId, 
            Font fonteCustomizada, Animacao tipo) {
        
        this(textos[primeiroId], fonteCustomizada, tipo);
        
        this.textos = textos;
        this.idAtual = primeiroId;
    }

    /**
     * Construtor auxiliar do label, recebe também uma cor personalizada para o
     * texto.
     * 
     * @param texto Texto do label
     * @param fonteCustomizada Fonte customizada para o texto
     * @param tipo Tipo de animação a ser reproduzido
     * @param corTexto Cor customizada para o texto
     */
    public LabelAnimado(String texto, Font fonteCustomizada, Animacao tipo, Color corTexto) {
        this(texto, fonteCustomizada, tipo);

        this.setForeground(corTexto);
    }
    
    /**
     * Determina e carrega a animação de exibição do label com base na escolha
     * do usuário.
     * @param tipo Tipo da animação a ser reproduzida
     */
    private void escolheAnimacao(Animacao tipo) {
        switch(tipo) {
            case FLOAT -> {
                posicaoY = 0;
                direcao = 1;
                ajuste = this::ajusteFloat;
                timerAnimacao = new Timer(60, e -> animacaoFloat());
            }
            case FADE -> {
                alpha = 0;
                delta = 1;
                ajuste = this::ajusteFade;
                timerAnimacao = new Timer(10, e -> animacaoFade());
            }
        }
        
        setFocusable(false);
        startAnimacao();
    }
    
    @Override
    public void setFocusable(boolean isFocusable) {
        super.setFocusable(isFocusable);
        
        if(isFocusable) {
            startAnimacao();
        } else {
            stopAnimacao();
        }
    }
    
    public void setColor(Color cor) {
        this.setForeground(cor);
    }
    
    public int getIdAtual() {
        return idAtual;
    }

    /**
     * Atualiza a renderização do texto de forma personalizada.
     * @param g Componente gráfico do texto
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ajuste.run();
    }

    /**
     * Reproduz a animação de "float" do label.
     */
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
    
    /**
     * Determina o ajuste final para a animação do label.
     */
    private void ajusteFloat() {
        Container parent = getParent();

        if (parent != null) {
            setLocation((parent.getWidth() - getWidth()) / 2, posicaoY);
        }
    }

    /**
     * Reproduz a animação de "fade" do label.
     */
    private void animacaoFade() {
        alpha += delta;
        if (alpha >= 255) {
            alpha = 255;
            delta = -delta;
        } else if (alpha <= 0) {
            alpha = 0;
            delta = -delta;
        }

        ajusteFade();
    }
    
    /**
     * Determina o ajuste final da animação de "fade" do label.
     */
    private void ajusteFade() {
        Color corAtual = this.getForeground();
        this.setForeground(new Color(corAtual.getRed(), 
                corAtual.getBlue(), corAtual.getGreen(), alpha));
    }
    
    public void stopAnimacao() {
        timerAnimacao.stop();
        alpha = 255;
        this.setForeground(Color.WHITE);
    }
    
    public void startAnimacao() {
        timerAnimacao.restart();
        this.setForeground(Color.RED);
    }
    
        @Override
    public void interacaoTeclado(KeyEvent evento) {
        int keyCode = evento.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                idAtual = (idAtual + 1) % textos.length;
                setText(textos[idAtual]);
                InterfaceManager.playSom(Estado.SACANDO);
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                idAtual = (idAtual + textos.length - 1) % textos.length;
                setText(textos[idAtual]);
                InterfaceManager.playSom(Estado.SACANDO);
            }
        }
    }
}
