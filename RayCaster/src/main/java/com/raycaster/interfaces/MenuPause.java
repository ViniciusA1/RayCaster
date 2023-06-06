package com.raycaster.interfaces;

import com.raycaster.engine.EfeitosSonoros;
import com.raycaster.engine.Engine;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuPause extends JPanel {

    private final Engine painelPrincipal;

    private JButton botaoVoltar;
    private JButton botaoSair;

    private Font fontePersonalizada;
    private EfeitosSonoros somBotao;
    private Image imagemBackground; 
    
    private final static Color corOriginal = Color.WHITE;
    private final static Color corFoco = Color.RED;

    public MenuPause(Engine painelPrincipal, Font fontePersonalizada, Image imagemBackground) {
        this.painelPrincipal = painelPrincipal;
        this.fontePersonalizada = fontePersonalizada;
        this.imagemBackground = imagemBackground;
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        carregaComponentes();
    }

    private void carregaComponentes() {
        botaoVoltar = new JButton("Voltar");
        botaoSair = new JButton("Sair");
        
        botaoVoltar.setAlignmentX(CENTER_ALIGNMENT);
        botaoSair.setAlignmentX(CENTER_ALIGNMENT);

        fontePersonalizada = fontePersonalizada.deriveFont(Font.PLAIN, 100f);

        botaoVoltar.setFont(fontePersonalizada);
        botaoSair.setFont(fontePersonalizada);
        botaoVoltar.setForeground(Color.WHITE);
        botaoSair.setForeground(Color.WHITE);

        botaoVoltar.addActionListener((ActionEvent e) -> {
            voltar();
        });
        
        botaoSair.addActionListener((ActionEvent e) -> {
            sair();
        });

        FocusListener focusListener = new FocusListener();
        MouseListener mouseListener = new MouseListener();

        botaoVoltar.addFocusListener(focusListener);
        botaoSair.addFocusListener(focusListener);
        botaoVoltar.addMouseListener(mouseListener);
        botaoSair.addMouseListener(mouseListener);

        this.add(botaoVoltar);
        this.add(botaoSair);

        botaoVoltar.setContentAreaFilled(false);
        botaoVoltar.setBorderPainted(false);
        botaoVoltar.setFocusPainted(false);

        botaoSair.setContentAreaFilled(false);
        botaoSair.setBorderPainted(false);
        botaoSair.setFocusPainted(false);
        
        configurarTeclasNavegacao();
    }
    
    private void configurarTeclasNavegacao() {
        HashSet<AWTKeyStroke> teclas = new HashSet<>();
        teclas.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, 0));
        teclas.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, 0));
        teclas.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_W, 0));
        teclas.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_S, 0));
        
        botaoVoltar.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, teclas);
        botaoSair.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, teclas);
        
        SwingUtilities.invokeLater(() -> {
            botaoVoltar.requestFocusInWindow();
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(imagemBackground, 0, 0, 
                this.getWidth(), this.getHeight(), this);
    }

    private void initSom() {}

    public void voltar() {
        painelPrincipal.voltaJogo(this);
    }

    public void sair() {
        painelPrincipal.fechaJogo();
    }

    private class FocusListener extends FocusAdapter {

        @Override
        public void focusGained(FocusEvent e) {
            JButton botao = (JButton) e.getSource();
            botao.setFont(botao.getFont().deriveFont(Font.BOLD));
            botao.setForeground(corFoco);
        }

        @Override
        public void focusLost(FocusEvent e) {
            JButton botao = (JButton) e.getSource();
            botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
            botao.setForeground(corOriginal);
        }
    }

    private class MouseListener extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            JButton botao = (JButton) e.getSource();
            botao.requestFocus();
            botao.setFont(botao.getFont().deriveFont(Font.BOLD));
            botao.setForeground(corFoco);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton botao = (JButton) e.getSource();
            botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
            botao.setForeground(corOriginal);
        }
    }

}