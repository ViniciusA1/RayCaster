package com.raycaster.interfaces;

import com.raycaster.engine.Engine;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author vinicius
 */
public class MenuPause extends JPanel {
    private final Engine painelPrincipal;
    
    private JButton botaoVoltar;
    private JButton botaoSair;
    
    private Font fontePersonalizada;
    
    public MenuPause(Engine painelPrincipal, Font fontePersonalizada) {
        this.painelPrincipal = painelPrincipal;
        this.fontePersonalizada = fontePersonalizada;
        
        carregaComponentes();
    }
    
    private void carregaComponentes() {
        botaoVoltar = new JButton("Voltar");
        
        botaoSair = new JButton("Sair");
        
        Font fonte = fontePersonalizada.deriveFont(Font.BOLD, 100f);
        
        botaoVoltar.setFont(fonte);
        botaoSair.setFont(fonte);        
        
        botaoVoltar.addActionListener(e -> voltar());
        botaoSair.addActionListener(e -> sair());
        
        this.add(botaoVoltar);
        this.add(botaoSair);
        
        botaoVoltar.setOpaque(false);
        botaoVoltar.setContentAreaFilled(false);
        botaoVoltar.setBorderPainted(false);
        botaoVoltar.setFocusPainted(false);
        
        botaoSair.setOpaque(false);
        botaoSair.setContentAreaFilled(false);
        botaoSair.setBorderPainted(false);
        botaoSair.setFocusPainted(false);
    }

    
    public void voltar() {
        painelPrincipal.voltaJogo(this);
    }
    
    public void sair() {
        painelPrincipal.fechaJogo();
    }
}
