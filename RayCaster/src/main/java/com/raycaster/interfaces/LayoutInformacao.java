package com.raycaster.interfaces;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Classe que contém atributos e métodos do layout de gerenciamento do
 * painel de informações do jogo.
 * 
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class LayoutInformacao implements LayoutManager {

    private final Font fontePersonalizada;


    /**
     * Construtor do layout, recebe a fonte personalizada do painel.
     * @param fontePersonalizada Fonte personalizada do painel
     */
    public LayoutInformacao(Font fontePersonalizada) {
        this.fontePersonalizada = fontePersonalizada;
    }

    /**
     * Adiciona um componente no layout.
     * @param string Nome do componente
     * @param cmpnt Referência ao componente
     */
    @Override
    public void addLayoutComponent(String string, Component cmpnt) {
    }

    /**
     * Remove um componente do layout.
     * @param cmpnt Referência ao componente
     */
    @Override
    public void removeLayoutComponent(Component cmpnt) {
    }

    /**
     * Seta o tamanho preferido do layout.
     * @param parent Container que usa o layout
     * @return Retorna a dimensão preferida
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    /**
     * Devolve o tamanho mínimo do container com esse layout.
     * @param parent Conteiner que usa o layout
     * @return Retorna a dimensão mínima
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * Define o padrao de distribuição dos componentes no layout.
     * @param parent Container que usa o layout
     */
    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int x = insets.left;
            int y = insets.top;

            int parentWidth = parent.getWidth();
            int parentHeight = parent.getHeight();

            Component[] components = parent.getComponents();

            Font fonte = fontePersonalizada.deriveFont(Font.BOLD,
                    (parentWidth + parentHeight) / 15);

            for (Component component : components) {
                int componentWidth = parentWidth - insets.left - insets.right;
                int componentHeight = parentHeight / components.length;

                component.setFont(fonte);
                component.setBounds(x, y, componentWidth, componentHeight);
                
                y += componentHeight;
            }
        }
    }
}
