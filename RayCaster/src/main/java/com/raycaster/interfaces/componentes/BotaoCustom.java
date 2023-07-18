package com.raycaster.interfaces.componentes;

import com.raycaster.engine.Estado;
import com.raycaster.interfaces.paineis.InterfaceManager;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Classe que contém os métodos e atributos do botão customizado feito para o
 * jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class BotaoCustom extends JButton implements Interagivel {

    private static final Color COR_FOCO = Color.RED;
    private static final Color COR_ORIGINAL = Color.WHITE;

    /**
     * Construtor principal do botão, recebe o texto a ser exibido.
     *
     * @param texto Texto exibido no botão
     */
    public BotaoCustom(String texto) {
        super(texto);

        this.setAlignmentX(CENTER_ALIGNMENT);
        this.setForeground(Color.WHITE);

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);

        this.addFocusListener(new FocusListener());
        this.addMouseListener(new MouseListener());
        
        this.setUI(new BotaoUI());

        focusKeybinding();
    }

    /**
     * Construtor auxiliar do botão, recebe também uma fonte customizada.
     *
     * @param texto Texto exibido no botão
     * @param fonteCustomizada Fonte customizada do botão
     */
    public BotaoCustom(String texto, Font fonteCustomizada) {
        this(texto);

        this.setFont(fonteCustomizada);
    }

    /**
     * Construtor auxiliar do botão, recebe também uma "action".
     *
     * @param texto Texto exibido no botão
     * @param fonteCustomizada Fonte customizada do botão
     * @param action Ação específica do botão
     */
    public BotaoCustom(String texto, Font fonteCustomizada, Runnable action) {
        this(texto, fonteCustomizada);

        actionKeybinding(action);
    }

    public BotaoCustom(String texto, Font fonteCustomizada, Runnable action,
            boolean isVoltar) {
        this(texto, fonteCustomizada, action);

        if (isVoltar) {
            atalhoVoltar(action);
        }
    }

    /**
     * Seta as keybindings padrão do botão.
     */
    private void focusKeybinding() {
        HashSet<AWTKeyStroke> backwardKeys = new HashSet<>();
        backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, 0));
        backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_W, 0));

        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

        HashSet<AWTKeyStroke> fowardKeys = new HashSet<>();
        fowardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, 0));
        fowardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_S, 0));

        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, fowardKeys);
    }

    private void atalhoVoltar(Runnable acao) {
        Action voltarAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acao.run();
            }
        };

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
                        0), "voltarAction");

        getActionMap().put("voltarAction", voltarAction);
    }

    /**
     * Seta o action listener e a tecla de ação.
     *
     * @param action Ação vinculada ao botão
     */
    private void actionKeybinding(Runnable action) {
        ActionListener ouvinte = (ActionEvent e) -> {
            InterfaceManager.playSom(Estado.USANDO);
            action.run();
        };

        addActionListener(ouvinte);

        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

        getInputMap(JComponent.WHEN_FOCUSED).put(enterKeyStroke, "enter");
        getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceManager.playSom(Estado.USANDO);
                action.run();
            }
        });
    }

    /**
     * Classe que guarda o ouvinte de foco do botão.
     */
    private class FocusListener extends FocusAdapter {

        @Override
        public void focusGained(FocusEvent e) {
            setFont(getFont().deriveFont(Font.BOLD));
            setForeground(COR_FOCO);

            InterfaceManager.playSom(Estado.TROCANDO);
        }

        @Override
        public void focusLost(FocusEvent e) {
            setFont(getFont().deriveFont(Font.PLAIN));
            setForeground(COR_ORIGINAL);
        }
    }

    /**
     * Classe que guarda o ouvinte de mouse do botão.
     */
    private class MouseListener extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isEnabled()) {
                return;
            }

            requestFocus();
            
            setFont(getFont().deriveFont(Font.BOLD));
            setForeground(COR_FOCO);
            InterfaceManager.playSom(Estado.TROCANDO);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!isEnabled()) {
                return;
            }

            setFont(getFont().deriveFont(Font.PLAIN));
            setForeground(COR_ORIGINAL);
        }
    }
    
    private static class BotaoUI extends BasicButtonUI {
        @Override
        protected void paintText(Graphics g, AbstractButton button, Rectangle textRect, String text) {
            Graphics2D g2d = (Graphics2D) g;

            Color originalColor = g2d.getColor();
            Font originalFont = g2d.getFont();

            Font font = originalFont.deriveFont(originalFont.getSize2D() - 1);
            g2d.setFont(font);

            FontMetrics fontMetrics = g2d.getFontMetrics(font);

            int textX = textRect.x + (textRect.width - 
                    fontMetrics.stringWidth(text)) / 2;
            int textY = textRect.y + ((textRect.height - 
                    fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();

            g2d.setColor(Color.GRAY);
            g2d.drawString(text, textX, textY - 1);

            g2d.setColor(originalColor);
            g2d.drawString(text, textX, textY + 1);

            g2d.setColor(originalColor);
            g2d.setFont(originalFont);
        }
    }
}
