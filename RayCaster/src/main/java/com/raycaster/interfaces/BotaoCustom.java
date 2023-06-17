package com.raycaster.interfaces;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Classe que contém os métodos e atributos do botão customizado feito para o
 * jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class BotaoCustom extends JButton {

    private static final Color COR_FOCO = Color.RED;
    private static final Color COR_ORIGINAL = Color.WHITE;

    /**
     * Construtor principal do botão, recebe o texto a ser exibido.
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

        focusKeybinding();
    }

    /**
     * Construtor auxiliar do botão, recebe também uma fonte customizada.
     * @param texto Texto exibido no botão
     * @param fonteCustomizada Fonte customizada do botão
     */
    public BotaoCustom(String texto, Font fonteCustomizada) {
        this(texto);

        this.setFont(fonteCustomizada);
    }
    
    /**
     * Construtor auxiliar do botão, recebe também uma "action".
     * @param texto Texto exibido no botão
     * @param fonteCustomizada Fonte customizada do botão
     * @param action Ação específica do botão
     */
    public BotaoCustom(String texto, Font fonteCustomizada, Runnable action) {
        this(texto, fonteCustomizada);
        
        actionKeybinding(action);
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
    
    /**
     * Seta o action listener e a tecla de ação.
     * @param action Ação vinculada ao botão
     */
    private void actionKeybinding(Runnable action) {
        ActionListener ouvinte = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        };
        
        addActionListener(ouvinte);
        
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

        getInputMap(JComponent.WHEN_FOCUSED).put(enterKeyStroke, "enter");
        getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            requestFocus();
            setFont(getFont().deriveFont(Font.BOLD));
            setForeground(COR_FOCO);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setFont(getFont().deriveFont(Font.PLAIN));
            setForeground(COR_ORIGINAL);
        }
    }
}
