package com.projeto.raycaster;

import javax.swing.JFrame;

/**
 *
 * @author vinic
 */
public class RayCaster {

    public static void main(String[] args) {
        JFrame janela = new JFrame();
        janela.setTitle("RayCaster");
        janela.setSize(800, 600);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);
        
        Engine game = new Engine(800, 600);
        
        janela.add(game);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }
}
