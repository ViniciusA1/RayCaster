package com.raycaster.interfaces.menus;

import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.paineis.InterfaceManager;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.componentes.LabelAnimado.Animacao;
import com.raycaster.interfaces.paineis.Painel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuConfig extends Painel {

    private final JFrame frame;
    
    private MenuConfigVideo video;
    private MenuConfigAudio audio;
    private MenuConfigControle controle;

    private LabelAnimado labelConfig;

    private BotaoCustom botaoVideo;
    private BotaoCustom botaoAudio;
    private BotaoCustom botaoControle;

    private BotaoCustom botaoVoltar;

    private Image background;

    public MenuConfig(JFrame frame, Image background, Font fonte) {
        this.frame = frame;
        this.background = background;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        carregaComponentes(fonte);
    }

    public void setImagem(Image background) {
        this.background = background;
        
        video.setImage(background);
        audio.setImage(background);
        //controle.setImage(background);
    }

    private void carregaComponentes(Font fonte) {
        Font configFonte = fonte.deriveFont(Font.PLAIN, 60f);
        
        Font labelFonte = fonte.deriveFont(Font.BOLD, 150f);

        labelConfig = new LabelAnimado("Configuração",
                labelFonte, Animacao.FLOAT);
        
        botaoVideo = new BotaoCustom("Vídeo", 
                configFonte, () -> video());
        botaoAudio = new BotaoCustom("Áudio", 
                configFonte, () -> audio());
        botaoControle = new BotaoCustom("Controle", 
                configFonte, () -> controle());
        botaoVoltar = new BotaoCustom("Voltar",
                configFonte, () -> voltar(), true);

        add(labelConfig);
        
        add(Box.createVerticalGlue());

        add(botaoVideo);
        add(botaoAudio);
        add(botaoControle);
        
        add(Box.createVerticalGlue());

        add(botaoVoltar);
        
        video = new MenuConfigVideo(configFonte, labelFonte, background);
        audio = new MenuConfigAudio(configFonte, labelFonte, background);
        controle = new MenuConfigControle(configFonte, labelFonte, background);
    }

    private void video() {
        InterfaceManager.push(frame, video);
    }

    private void audio() {
        InterfaceManager.push(frame, audio);
    }

    private void controle() {
        InterfaceManager.push(frame, controle);
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
            botaoVideo.requestFocusInWindow();
        });
    }
}
