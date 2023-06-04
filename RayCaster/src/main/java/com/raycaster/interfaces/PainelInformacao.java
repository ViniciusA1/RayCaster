package com.raycaster.interfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class PainelInformacao extends JPanel {
    private JLabel fps;
    
    public PainelInformacao(Font fontePersonalizada) {
        this.setOpaque(false);
        
        this.setLayout(new LayoutInformacao(fontePersonalizada));
        
        carregaComponentes();
    }
    
    private void carregaComponentes() {
        fps = new JLabel("FPS: 0");
        fps.setForeground(Color.WHITE);
        this.add(fps);
    }
    
    public void setFPS(int fps) {
        this.fps.setText("FPS: " + fps);
    }
}
