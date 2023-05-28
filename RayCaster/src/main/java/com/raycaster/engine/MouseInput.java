package com.raycaster.engine;

import com.raycaster.entidades.Player;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe que guarda os métodos e atributos do controlador geral dos eventos de
 * mouse do jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MouseInput extends MouseAdapter {
    private double sensibilidade;
    private int centroX;
    private int centroY;
    private final Player jogador;

    /**
     * Construtor do controlador, recebe o jogador associado ao evento e a 
     * sensibilidade desejada.
     * @param jogador Jogador associado aos eventos
     * @param sensibilidade Sensibilidade do mouse
     */
    public MouseInput(Player jogador, double sensibilidade) {
        this.jogador = jogador;
        this.sensibilidade = sensibilidade;
    }
    
    /**
     * Trata os eventos de "click" do mouse.
     * @param e Evento gerado pelo mouse
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
            jogador.usaItem(e.getX(), e.getY());
        
        mouseMoved(e);
    }
    
    /**
     * Trata os eventos de "arrastar" do mouse.
     * @param e Evento gerado pelo mouse
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
            jogador.usaItem(e.getX(), e.getY());
        
        mouseMoved(e);
    }
    
    /**
     * Trata os eventos de movimento (esquerda ou direita) do mouse.
     * @param e Evento gerado pelo mouse
     */
    @Override
    public void mouseMoved(MouseEvent e) {  
        int dx = e.getXOnScreen() - centroX;
        
        jogador.rotaciona(dx * sensibilidade);
        centralizaCursor(e);
    }
    
    /**
     * Centraliza o cursor no meio da janela (baseado em sistema).
     * @param evento Evento gerado pelo mouse
     */
    public void centralizaCursor(MouseEvent evento) {
        Component janela = evento.getComponent();
        
        centroX = janela.getLocationOnScreen().x + janela.getWidth() / 2;
        centroY = janela.getLocationOnScreen().y + janela.getHeight() / 2;
        
        
        try {
            Robot robot = new Robot();
            robot.mouseMove(centroX, centroY);
        } catch (AWTException ex) {
            System.err.println("Erro! Impossível usar a classe Robot");
        }
    }
}
