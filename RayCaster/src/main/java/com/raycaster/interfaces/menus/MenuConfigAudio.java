package com.raycaster.interfaces.menus;

import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.componentes.LabelAnimado.Animacao;
import com.raycaster.interfaces.layouts.LayoutConfig;
import com.raycaster.interfaces.paineis.Painel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author vinicius
 */
public class MenuConfigAudio extends Painel {
    private LabelAnimado labelAudio;
    private BotaoCustom botaoMusica;
    private JProgressBar barraMusica;
    private BotaoCustom botaoSfx;
    private JProgressBar barraSfx;
    private BotaoCustom botaoVoltar;
    
    private Image background;
    
    public MenuConfigAudio(Font configFonte, Font labelFonte, Image background) {
        this.background = background;
        
        setLayout(new LayoutConfig());
        
        carregaComponentes(configFonte, labelFonte);
    }
    
    public void setImage(Image background) {
        this.background = background;
    }
    
    private void carregaComponentes(Font configFonte, Font labelFonte) {
        labelAudio = new LabelAnimado("Áudio", 
                labelFonte, Animacao.FLOAT);
        
        botaoVoltar = new BotaoCustom("Voltar", configFonte,
                () -> voltar(), true);
        
        Border borda = BorderFactory.createLineBorder(Color.DARK_GRAY,
                15, true);

        botaoMusica = new BotaoCustom("Música", configFonte);
        barraMusica = new JProgressBar(0, 100);
        barraMusica.setPreferredSize(new Dimension(200, 50));
        barraMusica.setOpaque(false);
        barraMusica.setForeground(Color.RED);
        barraMusica.setBorder(borda);

        botaoSfx = new BotaoCustom("SFX", configFonte);
        barraSfx = new JProgressBar(0, 100);
        barraSfx.setPreferredSize(new Dimension(200, 50));
        barraSfx.setOpaque(false);
        barraSfx.setForeground(Color.RED);
        barraSfx.setBorder(borda);
        
        add(labelAudio);
        add(botaoMusica);
        add(barraMusica);
        add(botaoSfx);
        add(barraSfx);
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
            botaoMusica.requestFocusInWindow();
        });
    }
}