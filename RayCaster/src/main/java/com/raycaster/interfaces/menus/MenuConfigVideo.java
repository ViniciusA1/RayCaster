package com.raycaster.interfaces.menus;

import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.componentes.CheckBox;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.layouts.LayoutConfig;
import com.raycaster.interfaces.paineis.InterfaceManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuConfigVideo extends AbstractMenuConfig {
    
    private GraphicsDevice device;

    private LabelAnimado labelVideo;
    private BotaoCustom botaoResolucao;
    private LabelAnimado labelResolucao;
    
    private BotaoCustom botaoFullScreen;
    private CheckBox checkFullScreen;

    private final static String[] resolucao;

    private BotaoCustom botaoVoltar;

    static {
        resolucao = new String[]{"320x240",
            "480x320",
            "640x480",
            "800x600"};
    }

    public MenuConfigVideo(String resolucaoInicial, boolean isFullScreen, 
            Font configFonte, Font labelFonte, Image background) {
        super(background);
        
        setLayout(new LayoutConfig());
        
        carregaComponentes(resolucaoInicial, isFullScreen, configFonte, labelFonte);
    }
    
    public int getComprimento() {
        return Integer.parseInt(labelResolucao.
                getText().split("x")[0]);
    }
    
    public int getAltura() {
        return Integer.parseInt(labelResolucao.
                getText().split("x")[1]);
    }
    
    public boolean getFullScreen() {
        return checkFullScreen.isEnabled();
    }

    private void carregaComponentes(String resolucaoInicial, 
            boolean isFullScreen, Font configFonte, Font labelFonte) {
        
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();
        
        labelVideo = new LabelAnimado("Video",
                labelFonte, LabelAnimado.Animacao.FLOAT);

        botaoResolucao = new BotaoCustom("Resolution", 
                configFonte, () -> resolucao());
        
        int resolucaoId = buscaId(resolucaoInicial);

        labelResolucao = new LabelAnimado(resolucao, resolucaoId,
                configFonte,LabelAnimado.Animacao.FADE);
        
        botaoFullScreen = new BotaoCustom("FullScreen", 
                configFonte, () -> fullScreen());
        
        checkFullScreen = new CheckBox();
        checkFullScreen.setEnabled(!isFullScreen);
        checkFullScreen.setPreferredSize(new Dimension(50, 50));
        fullScreen();
        
        botaoVoltar = new BotaoCustom("Return", 
                configFonte, () -> voltar(), true);

        labelResolucao.stopAnimacao();
        
        add(labelVideo);
        add(botaoResolucao);
        add(labelResolucao);
        add(botaoFullScreen);
        add(checkFullScreen);
        add(botaoVoltar);
    }
    
    private int buscaId(String resolucaoInicial) {
        int id = -1;
        
        for(int i = 0; i < resolucao.length; i++) {
            if(resolucaoInicial.compareTo(resolucao[i]) == 0)
                id = i;
        }
        
        if(id < 0)
            id = 0;
        
        return id;
    }
    
    private void resolucao() {
        setFoco(getComponentZOrder(labelResolucao));
    }
    
    private void fullScreen() {
        JFrame janela = (JFrame) SwingUtilities.
                    getWindowAncestor(InterfaceManager.peek());

        if(checkFullScreen.isEnabled()) {
            device.setFullScreenWindow(null);
            checkFullScreen.setEnabled(false);
        }
        else {
            device.setFullScreenWindow(janela);
            checkFullScreen.setEnabled(true);
        }
    }
    
    @Override
    public void entrar() {
        super.entrar();
        
        SwingUtilities.invokeLater(() -> {
            botaoResolucao.requestFocusInWindow();
        });
    }
}
