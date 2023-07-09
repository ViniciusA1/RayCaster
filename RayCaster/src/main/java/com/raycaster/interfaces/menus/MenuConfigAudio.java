package com.raycaster.interfaces.menus;

import com.raycaster.engine.sons.SomManager;
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
public class MenuConfigAudio extends AbstractMenuConfig {
    
    private LabelAnimado labelAudio;
    
    private BotaoCustom botaoMusica;
    private BarraProgresso barraMusica;
    
    private BotaoCustom botaoSfx;
    private BarraProgresso barraSfx;
    
    private BotaoCustom botaoVoltar;
    
    public MenuConfigAudio(Font configFonte, Font labelFonte, Image background) {
        super(background);
        
        setLayout(new LayoutConfig());
        
        carregaComponentes(configFonte, labelFonte);
    }
    
    public int getMusica() {
        return barraMusica.getValue();
    }
    
    public int getSfx() {
        return barraSfx.getValue();
    }
    
    private void carregaComponentes(Font configFonte, Font labelFonte) {
        labelAudio = new LabelAnimado("Áudio", 
                labelFonte, Animacao.FLOAT);
        
        botaoVoltar = new BotaoCustom("Voltar", configFonte,
                () -> voltar(), true);

        botaoMusica = new BotaoCustom("Música", 
                configFonte, () -> acaoMusica());
        
        barraMusica = new BarraProgresso(0, 100, () -> alteraMusica(), 
                configFonte);
        barraMusica.setValue(SomManager.getVolumeMusica());

        botaoSfx = new BotaoCustom("SFX", 
                configFonte, () -> acaoSfx());
        
        barraSfx = new BarraProgresso(0, 100, () -> alteraSfx(),
                configFonte);
        barraSfx.setValue(SomManager.getVolumeSfx());
        
        add(labelAudio);
        add(botaoMusica);
        add(barraMusica);
        add(botaoSfx);
        add(barraSfx);
        add(botaoVoltar);
    }
    
    private void acaoMusica() {
        setFoco(getComponentZOrder(barraMusica));
    }
    
    private void alteraMusica() {
        int novoVolume = barraMusica.getValue();
        
        SomManager.ajustaMusica(novoVolume);
    }
    
    private void acaoSfx() {
        setFoco(getComponentZOrder(barraSfx));
    }
    
    private void alteraSfx() {
        int novoVolume = barraSfx.getValue();
        
        SomManager.ajustaSfx(novoVolume);
    }
    
    @Override
    public void entrar() {
        super.entrar();
        
        SwingUtilities.invokeLater(() -> {
            botaoMusica.requestFocusInWindow();
        });
    }
    
    @Override
    public void voltar() {
        super.voltar();
        
        retornaFoco();
    }
}