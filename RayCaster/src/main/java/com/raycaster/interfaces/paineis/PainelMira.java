package com.raycaster.interfaces.paineis;

import com.raycaster.engine.ArquivoUtils;
import com.raycaster.engine.Diretorio;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Classe que contém os atributos e métodos do 
 * painel de mira do jogador.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class PainelMira extends JPanel {
    private ImageIcon imagemMira;
    
    /**
     * Construtor do painel de mira.
     */
    public PainelMira() {
        this.setOpaque(false);

        imagemMira = ArquivoUtils.leImagem(Diretorio.SPRITE_HUD + "mira");
    }
    
    /**
     * Renderiza a imagem da mira no painel.
     * @param g Componente gráfico
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(imagemMira.getImage(), 0, 0, 
                this.getWidth(), this.getHeight(), this);
    }
}
