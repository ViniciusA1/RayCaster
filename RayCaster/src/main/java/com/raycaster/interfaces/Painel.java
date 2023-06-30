package com.raycaster.interfaces;

import javax.swing.JPanel;

/**
 *
 * @author vinicius
 */
public abstract class Painel extends JPanel {
    public void entrar() {
        setVisible(true);
    }
    
    public void sair() {
        setVisible(false);
    }
}
