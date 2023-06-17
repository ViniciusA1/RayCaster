package com.raycaster.interfaces;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe que contém os atributos e métodos do 
 * painel de informações do jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class PainelInformacao extends JPanel {
    private JLabel fps;
    
    /**
     * Construtor do painel de informações.
     * @param fontePersonalizada Fonte personalizada do painel
     */
    public PainelInformacao(Font fontePersonalizada) {
        this.setOpaque(false);
        
        this.setLayout(new LayoutInformacao(fontePersonalizada));
        
        carregaComponentes();
    }
    
    /**
     * Carrega todos os componentes do painel.
     */
    private void carregaComponentes() {
        fps = new JLabel("FPS: 0");
        fps.setForeground(Color.WHITE);
        this.add(fps);
    }
    
    /**
     * Seta o fps para um novo valor.
     * @param fps Novo valor do fps
     */
    public void setFPS(int fps) {
        this.fps.setText("FPS: " + fps);
    }
}
