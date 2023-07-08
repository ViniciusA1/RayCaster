package com.raycaster.engine;

import com.raycaster.entidades.Player;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;

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
    private boolean isLeftClick;

    /**
     * Construtor do controlador, recebe o jogador associado ao evento e a 
     * sensibilidade desejada.
     * @param jogador Jogador associado aos eventos
     * @param sensibilidade Sensibilidade do mouse
     */
    public MouseInput(Player jogador, double sensibilidade) {
        this.jogador = jogador;
        this.sensibilidade = sensibilidade;
        this.isLeftClick = false;
    }
    
    /**
     * Trata os eventos de "click" do mouse.
     * @param e Evento gerado pelo mouse
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            isLeftClick = true;
            jogador.usaItem(e.getX(), e.getY());
        }
        
        mouseMoved(e);
    }
    
    /**
     * Trata os eventos de "arrastar" do mouse.
     * @param e Evento gerado pelo mouse
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if(isLeftClick)
            jogador.usaItem(e.getX(), e.getY());
        
        mouseMoved(e);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        isLeftClick = false;
    }
    
    /**
     * Trata os eventos de movimento (esquerda ou direita) do mouse.
     * @param e Evento gerado pelo mouse
     */
    @Override
    public void mouseMoved(MouseEvent e) {  
        int dx = e.getXOnScreen() - centroX;
        
        jogador.rotaciona(dx * sensibilidade);
        centralizaCursor(e.getComponent());
    }
    
    /**
     * Centraliza o cursor no meio da janela (baseado em sistema).
     * @param janela Janela a qual o mouse está associada
     */
    public void centralizaCursor(Component janela) {
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
