package com.raycaster.interfaces.paineis;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public abstract class Painel extends JPanel {
    public void entrar() {
        setVisible(true);
        
        SwingUtilities.invokeLater(() -> {
            this.requestFocusInWindow();
        });
    }
    
    public void sairPop() {
        setVisible(false);
    }
    
    public void sairPush() {
        setVisible(false);
    }
    
    public void voltar() {
        InterfaceManager.pop();
    }
}
