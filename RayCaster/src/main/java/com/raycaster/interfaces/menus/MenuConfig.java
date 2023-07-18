package com.raycaster.interfaces.menus;

import com.raycaster.utils.ArquivoUtils;
import com.raycaster.utils.Diretorio;
import com.raycaster.engine.Engine;
import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.paineis.InterfaceManager;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.componentes.LabelAnimado.Animacao;
import java.awt.Font;
import java.awt.Image;
import java.util.Properties;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuConfig extends AbstractMenuConfig {

    private static final Properties dadosConfig;

    private final JFrame frame;

    private Engine engine;

    private MenuConfigVideo video;
    private MenuConfigAudio audio;
    private MenuConfigControle controle;

    private LabelAnimado labelConfig;

    private BotaoCustom botaoVideo;
    private BotaoCustom botaoAudio;
    private BotaoCustom botaoControle;

    private BotaoCustom botaoVoltar;

    static {
        dadosConfig = ArquivoUtils.
                lePropriedade(Diretorio.DADOS_ENGINE + "config");
    }

    public MenuConfig(JFrame frame, Image background, Font fonte) {
        super(background);

        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        carregaComponentes(fonte);
    }

    public int getComprimento() {
        return video.getComprimento();
    }

    public int getAltura() {
        return video.getAltura();
    }

    public void setEngine(Engine novoJogo) {
        this.engine = novoJogo;
    }

    @Override
    public void setImagem(Image background) {
        super.setImagem(background);

        video.setImagem(background);
        audio.setImagem(background);
        controle.setImagem(background);
    }

    private void carregaComponentes(Font fonte) {
        Font configFonte = fonte.deriveFont(Font.PLAIN, 60f);

        Font labelFonte = fonte.deriveFont(Font.BOLD, 150f);

        labelConfig = new LabelAnimado("Settings",
                labelFonte, Animacao.FLOAT);

        botaoVideo = new BotaoCustom("Video",
                configFonte, () -> video());
        botaoAudio = new BotaoCustom("Audio",
                configFonte, () -> audio());
        botaoControle = new BotaoCustom("Control",
                configFonte, () -> controle());
        botaoVoltar = new BotaoCustom("Return",
                configFonte, () -> voltar(), true);

        add(labelConfig);

        add(Box.createVerticalGlue());

        add(botaoVideo);
        add(botaoAudio);
        add(botaoControle);

        add(Box.createVerticalGlue());

        add(botaoVoltar);
        
        String resolucaoInicial = dadosConfig.getProperty("width") + 
                "x" + dadosConfig.getProperty("height");
        boolean isFullScreen = Boolean.parseBoolean(dadosConfig.
                getProperty("fullScreen"));

        video = new MenuConfigVideo(resolucaoInicial, isFullScreen, 
                configFonte, labelFonte, getImagem());
        audio = new MenuConfigAudio(configFonte, labelFonte,
                getImagem());
        controle = new MenuConfigControle(configFonte, labelFonte,
                getImagem());
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
    public void entrar() {
        super.entrar();

        SwingUtilities.invokeLater(() -> {
            botaoVideo.requestFocusInWindow();
        });
    }

    @Override
    public void sairPop() {
        super.sairPop();

        dadosConfig.setProperty("width", video.getComprimento() + "");
        dadosConfig.setProperty("height", video.getAltura() + "");
        dadosConfig.setProperty("fullScreen", video.getFullScreen() + "");
        dadosConfig.setProperty("musica", audio.getMusica() + "");
        dadosConfig.setProperty("sfx", audio.getSfx() + "");

        if (engine != null) {
            engine.setResolucao(video.getComprimento(),
                    video.getAltura());
        }

        ArquivoUtils.salvaPropriedade(dadosConfig);
    }
}
