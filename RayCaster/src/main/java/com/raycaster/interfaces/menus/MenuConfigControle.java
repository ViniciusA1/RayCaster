package com.raycaster.interfaces.menus;

import com.raycaster.interfaces.componentes.BarraProgresso;
import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.componentes.LabelAnimado.Animacao;
import com.raycaster.interfaces.layouts.LayoutConfig;
import java.awt.Font;
import java.awt.Image;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuConfigControle extends AbstractMenuConfig {
    
    private LabelAnimado labelControle;
    
    private BotaoCustom botaoSensibilidade;
    private BarraProgresso barraSensibilidade;
    
    private BotaoCustom botaoVoltar;
    
    public MenuConfigControle(Font configFonte, Font labelFonte, Image background) {
        super(background);
        
        setLayout(new LayoutConfig());
        
        carregaComponentes(configFonte, labelFonte);
    }
    
    private void carregaComponentes(Font configFonte, Font labelFonte) {
        
        labelControle = new LabelAnimado("Control", 
                labelFonte, Animacao.FLOAT);
        
        botaoSensibilidade = new BotaoCustom("Sensitivity", configFonte);
        
        barraSensibilidade = new BarraProgresso(0, 100, configFonte);
        
        botaoVoltar = new BotaoCustom("Return", 
                configFonte, () -> voltar(), true);
        
        add(labelControle);
        add(botaoSensibilidade);
        add(barraSensibilidade);
        add(botaoVoltar);
    }
    
    @Override
    public void entrar() {
        super.entrar();
        
        SwingUtilities.invokeLater(() -> {
            botaoSensibilidade.requestFocusInWindow();
        });
    }
}
