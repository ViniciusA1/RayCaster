package com.raycaster.interfaces.menus;

import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.layouts.LayoutConfig;
import com.raycaster.interfaces.paineis.Painel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuConfigVideo extends Painel {

    private LabelAnimado labelVideo;
    private BotaoCustom botaoResolucao;
    private LabelAnimado labelResolucao;

    private final static String[] resolucao;

    private BotaoCustom botaoVoltar;
    
    private Image background;

    static {
        resolucao = new String[]{"320x200",
            "480x240",
            "640x480",
            "800x600"};
    }

    public MenuConfigVideo(Font configFonte, Font labelFonte, Image background) {
        this.background = background;
        
        setLayout(new LayoutConfig());
        
        carregaComponentes(configFonte, labelFonte);
    }
    
    public void setImage(Image background) {
        this.background = background;
    }

    private void carregaComponentes(Font configFonte, Font labelFonte) {
        labelVideo = new LabelAnimado("Vídeo",
                labelFonte, LabelAnimado.Animacao.FLOAT);

        botaoResolucao = new BotaoCustom("Resolução", configFonte);

        labelResolucao = new LabelAnimado(resolucao[0], configFonte,
                LabelAnimado.Animacao.FADE, Color.WHITE);
        
        botaoVoltar = new BotaoCustom("Voltar", 
                configFonte, () -> voltar(), true);

        labelResolucao.stopAnimacao();
        
        add(labelVideo);
        add(botaoResolucao);
        add(labelResolucao);
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
            botaoResolucao.requestFocusInWindow();
        });
    }
}
