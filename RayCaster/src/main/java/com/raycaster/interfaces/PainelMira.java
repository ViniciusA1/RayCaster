package com.raycaster.interfaces;

import com.raycaster.engine.ArquivoUtils;
import com.raycaster.engine.Diretorio;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class PainelMira extends JPanel {
    private ImageIcon imagemMira;
    
    public PainelMira() {
        this.setOpaque(false);

        imagemMira = ArquivoUtils.leImagem(Diretorio.SPRITE_HUD + "mira");
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(imagemMira.getImage(), 0, 0, 
                this.getWidth(), this.getHeight(), this);
    }
}
