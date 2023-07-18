package com.raycaster.interfaces.componentes;

import com.raycaster.engine.Estado;
import com.raycaster.interfaces.paineis.InterfaceManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 *
 * @author vinicius
 */
public class BarraProgresso extends JProgressBar implements Interagivel {

    private final int taxa;

    private Runnable alteracao;

    public BarraProgresso(int min, int max, int value, int taxa,
            Runnable alteracao, Font configFonte) {
        super(min, max);

        this.taxa = taxa;
        this.alteracao = alteracao;

        setValue(value);
        setFont(configFonte.deriveFont(Font.PLAIN, 30f));

        carregaComponente();
    }

    public BarraProgresso(int min, int max, int value, Runnable alteracao,
            Font configFonte) {
        this(min, max, value, 5, alteracao, configFonte);
    }

    public BarraProgresso(int min, int max, int value, Font configFonte) {
        this(min, max, value, null, configFonte);
    }

    public BarraProgresso(int min, int max, Runnable alteracao,
            Font configFonte) {
        this(min, max, min, alteracao, configFonte);

    }

    public BarraProgresso(int min, int max, Font configFonte) {
        this(min, max, null, configFonte);
    }

    public BarraProgresso(Font configFonte) {
        this(0, 100, configFonte);
    }

    private void carregaComponente() {
        Border borda = BorderFactory.createLineBorder(Color.DARK_GRAY,
                10, true);

        setPreferredSize(new Dimension(250, 75));
        setOpaque(false);
        setForeground(Color.RED);
        setBorder(borda);
        setFocusable(false);
        setStringPainted(true);
        setUI(new BarraUI());
    }

    @Override
    public void interacaoTeclado(KeyEvent evento) {
        int keyCode = evento.getKeyCode();
        int valor = getValue();

        switch (keyCode) {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                if (valor + taxa > getMaximum()) {
                    return;
                }

                setValue(valor + taxa);

                if (alteracao != null) {
                    alteracao.run();
                }

                InterfaceManager.playSom(Estado.TROCANDO);
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                if (valor - taxa < getMinimum()) {
                    return;
                }

                setValue(valor - taxa);

                if (alteracao != null) {
                    alteracao.run();
                }

                InterfaceManager.playSom(Estado.TROCANDO);
            }
        }
    }

    private class BarraUI extends BasicProgressBarUI {

        @Override
        protected void paintString(Graphics g, int x, int y, int width,
                int height, int amountFull, Insets b) {
            int value = progressBar.getValue();

            Font font = progressBar.getFont();
            g.setFont(font);
            g.setColor(Color.WHITE);

            FontMetrics metrics = g.getFontMetrics(font);
            String text = Integer.toString(value);
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();

            int textX = x + (width - textWidth) / 2;
            int textY = y + (height - textHeight) / 2 + metrics.getAscent();

            g.drawString(text, textX, textY);
        }
    }

}
