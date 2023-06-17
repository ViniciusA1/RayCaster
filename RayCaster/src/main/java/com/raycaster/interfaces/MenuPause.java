package com.raycaster.interfaces;

import com.raycaster.engine.Engine;
import com.raycaster.interfaces.LabelAnimado.Animacao;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Classe que contém os métodos e atributos do menu de "pause" do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MenuPause extends JPanel {

    private final Engine painelPrincipal;

    private LabelAnimado textoPause;
    private BotaoCustom botaoVoltar;
    private BotaoCustom botaoSair;

    private final Image imagemBackground;

    /**
     * Construtor principal do menu, recebe os elementos necessários para sua
     * criação.
     *
     * @param painelPrincipal Painel principal que será sobrescrito
     * @param fontePersonalizada Fonte personalizada utilizada
     * @param imagemBackground Imagem que servirá de background
     */
    public MenuPause(Engine painelPrincipal, Font fontePersonalizada, Image imagemBackground) {
        this.painelPrincipal = painelPrincipal;
        this.imagemBackground = imagemBackground;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        carregaComponentes(fontePersonalizada);
    }

    /**
     * Carrega todos os componentes necessários para o funcionamento do menu.
     *
     * @param fonte Fonte personalizada dos componentes
     */
    private void carregaComponentes(Font fonte) {
        fonte = fonte.deriveFont(Font.PLAIN, 100f);

        textoPause = new LabelAnimado("Pausado",
                fonte.deriveFont(Font.PLAIN, 150f), Animacao.FLOAT);

        botaoVoltar = new BotaoCustom("Voltar", fonte, () -> voltar());
        botaoSair = new BotaoCustom("Sair", fonte, () -> sair());

        textoPause.setAlignmentX(CENTER_ALIGNMENT);

        this.add(textoPause);
        this.add(botaoVoltar);
        this.add(botaoSair);
        
        adicionarAtalho();

        SwingUtilities.invokeLater(() -> {
            botaoVoltar.requestFocusInWindow();
        });
    }

    /**
     * Adiciona atalhos para acesso dos botões.
     */
    private void adicionarAtalho() {
        Action voltarAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltar();
            }
        };

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 
                        0), "voltarAction");
        
        getActionMap().put("voltarAction", voltarAction);
    }

    /**
     * Renderiza os componentes do menu.
     * @param g Componente gráfico
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.drawImage(imagemBackground, 0, 0,
                this.getWidth(), this.getHeight(), this);
    }

    /**
     * Volta para o painel anterior.
     */
    public void voltar() {
        painelPrincipal.voltaJogo(this);
    }

    /**
     * Sai do jogo completamente.
     */
    public void sair() {
        painelPrincipal.fechaJogo();
    }
}
