package com.raycaster.interfaces.menus;

import com.raycaster.engine.Estado;
import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.componentes.Interagivel;
import com.raycaster.interfaces.paineis.InterfaceManager;
import com.raycaster.interfaces.paineis.Painel;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public abstract class AbstractMenuConfig extends Painel {

    private Image background;
    
    private Component componenteFocado;
    
    private final KeyListener ouvinte;
    
    public AbstractMenuConfig(Image background) {
        this.background = background;
        
        ouvinte = new KeyListener();
    }

    public void setImagem(Image background) {
        this.background = background;
    }
    
    public Image getImagem() {
        return background;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(),
                getHeight(), this);
    }
    
    public void setFoco(int index) {
        Component[] componentes = getComponents();
        componenteFocado = componentes[index];
        
        for(Component compAux : componentes) {
            if(compAux instanceof BotaoCustom botao) {
                botao.setEnabled(false);
            }
        }
        
        Interagivel componente = (Interagivel) componenteFocado;
        componente.mudaFoco(true);
        
        setFocusable(true);
        addKeyListener(ouvinte);
        
        SwingUtilities.invokeLater(() -> {
            this.requestFocusInWindow();
        });
    }
    
    public void retornaFoco() {
        Component[] componentes = getComponents();
        
        for(Component compAux : componentes) {
            if(compAux instanceof BotaoCustom botao)
                botao.setEnabled(true);
        }
        
        Interagivel componente = (Interagivel) componenteFocado;
        componente.mudaFoco(false);
        
        setFocusable(false);
        
        SwingUtilities.invokeLater(() -> {
            componentes[1].requestFocusInWindow();
        });
    }
    
    private class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            if(event.getKeyCode() == KeyEvent.VK_ENTER) {
                removeKeyListener(this);
                retornaFoco();
                InterfaceManager.playSom(Estado.USANDO);
                return;
            }
            
            Interagivel componente = (Interagivel) componenteFocado;
            componente.interacaoTeclado(event);
        }
    }
}
