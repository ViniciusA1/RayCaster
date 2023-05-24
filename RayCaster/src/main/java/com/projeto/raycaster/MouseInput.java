package com.projeto.raycaster;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author vinic
 */
public class MouseInput extends MouseAdapter {
    private double sensibilidade;
    private int centroX;
    private int centroY;
    private final Player jogador;

    public MouseInput(Player jogador, double sensibilidade) {
        this.jogador = jogador;
        this.sensibilidade = sensibilidade;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        jogador.usaItem(e.getX(), e.getY());
        mouseMoved(e);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        jogador.usaItem(e.getX(), e.getY());
        mouseMoved(e);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {  
        int dx = e.getXOnScreen() - centroX;
        
        jogador.rotaciona(dx * sensibilidade);
        centralizaCursor(e);
    }
    
    public void centralizaCursor(MouseEvent evento) {
        Component janela = evento.getComponent();
        
        centroX = janela.getLocationOnScreen().x + janela.getWidth() / 2;
        centroY = janela.getLocationOnScreen().y + janela.getHeight() / 2;
        
        
        try {
            Robot robot = new Robot();
            robot.mouseMove(centroX, centroY);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }
}
