package com.raycaster.interfaces.menus;

import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.paineis.InterfaceManager;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.componentes.LabelAnimado.Animacao;
import com.raycaster.interfaces.paineis.Painel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Classe que contém os métodos e atributos do menu de "pause" do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MenuPause extends Painel {

    private final JFrame janela;

    private LabelAnimado textoPause;
    private BotaoCustom botaoVoltar;
    private BotaoCustom botaoConfig;
    private BotaoCustom botaoSair;

    private MenuConfig menuConfiguracao;

    private Image imagemBackground;

    public MenuPause(JFrame janela, Font fontePersonalizada,
            MenuConfig menuConfiguracao) {

        this.janela = janela;
        this.menuConfiguracao = menuConfiguracao;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        carregaComponentes(fontePersonalizada);
    }

    /**
     * Construtor principal do menu, recebe os elementos necessários para sua
     * criação.
     *
     * @param janela
     * @param fontePersonalizada Fonte personalizada utilizada
     */
    public MenuPause(JFrame janela, Font fontePersonalizada) {
        this(janela, fontePersonalizada, null);

        menuConfiguracao = new MenuConfig(janela,
                imagemBackground, fontePersonalizada);
    }

    public void setImagem(Image imagemBackground) {
        this.imagemBackground = imagemBackground;
        menuConfiguracao.setImagem(imagemBackground);
    }

    /**
     * Carrega todos os componentes necessários para o funcionamento do menu.
     *
     * @param fonte Fonte personalizada dos componentes
     */
    private void carregaComponentes(Font fonte) {
        fonte = fonte.deriveFont(Font.PLAIN, 100f);

        textoPause = new LabelAnimado("Paused",
                fonte.deriveFont(Font.BOLD, 150f),
                Animacao.FLOAT);

        botaoVoltar = new BotaoCustom("Resume", fonte,
                () -> voltar(), true);
        botaoConfig = new BotaoCustom("Settings", fonte,
                () -> configurar());
        botaoSair = new BotaoCustom("Exit game", fonte,
                () -> sairJogo());

        textoPause.setAlignmentX(CENTER_ALIGNMENT);

        this.add(textoPause);
        this.add(botaoVoltar);
        this.add(botaoConfig);
        this.add(botaoSair);
    }

    /**
     * Renderiza os componentes do menu.
     *
     * @param g Componente gráfico
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.drawImage(imagemBackground, 0, 0,
                this.getWidth(), this.getHeight(), this);
    }

    @Override
    public void entrar() {
        super.entrar();

        SwingUtilities.invokeLater(() -> {
            botaoVoltar.requestFocusInWindow();
        });
    }

    public void configurar() {
        InterfaceManager.push(janela, menuConfiguracao);
    }

    /**
     * Sai do jogo completamente.
     */
    public void sairJogo() {
        super.sairPop();
        voltar();
        InterfaceManager.pop();
    }
}
