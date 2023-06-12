package com.raycaster.interfaces;

import com.raycaster.engine.Engine;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuPause extends JPanel {

    private final Engine painelPrincipal;

    private BotaoCustom botaoVoltar;
    private BotaoCustom botaoSair;

    private final Image imagemBackground; 
   
    
    public MenuPause(Engine painelPrincipal, Font fontePersonalizada, Image imagemBackground) {
        this.painelPrincipal = painelPrincipal;
        this.imagemBackground = imagemBackground;
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        carregaComponentes(fontePersonalizada);
    }

    private void carregaComponentes(Font fonte) {
        fonte = fonte.deriveFont(Font.PLAIN, 100f);

        botaoVoltar = new BotaoCustom("Voltar", fonte, () -> voltar());
        botaoSair = new BotaoCustom("Sair", fonte, () -> sair());

        this.add(botaoVoltar);
        this.add(botaoSair);
                
        SwingUtilities.invokeLater(() -> {
            botaoVoltar.requestFocusInWindow();
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(imagemBackground, 0, 0, 
                this.getWidth(), this.getHeight(), this);
    }

    public void voltar() {
        painelPrincipal.voltaJogo(this);
    }

    public void sair() {
        painelPrincipal.fechaJogo();
    }
}