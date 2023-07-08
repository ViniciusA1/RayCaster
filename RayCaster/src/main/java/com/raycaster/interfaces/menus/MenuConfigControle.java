package com.raycaster.interfaces.menus;

import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.componentes.LabelAnimado.Animacao;
import com.raycaster.interfaces.layouts.LayoutConfig;
import com.raycaster.interfaces.paineis.Painel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuConfigControle extends Painel {
    
    private LabelAnimado labelControle;
    private BotaoCustom botaoVoltar;
    
    private Image background;
    
    public MenuConfigControle(Font configFonte, Font labelFonte, 
            Image background) {
        this.background = background;
        
        setLayout(new LayoutConfig());
        
        carregaComponentes(configFonte, labelFonte);
    }
    
    public void setImage(Image background) {
        this.background = background;
    }
    
    private void carregaComponentes(Font configFonte, Font labelFonte) {
        
        labelControle = new LabelAnimado("Controle", 
                labelFonte, Animacao.FLOAT);
        
        botaoVoltar = new BotaoCustom("Voltar", 
                configFonte, () -> voltar(), true);
        
        add(labelControle);
        add(botaoVoltar);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(),
                getHeight(), this);
    }
    
    @Override
    public void entrar() {
        super.entrar();
        
        SwingUtilities.invokeLater(() -> {
            labelControle.requestFocusInWindow();
        });
    }
}
