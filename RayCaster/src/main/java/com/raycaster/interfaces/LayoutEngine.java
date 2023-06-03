package com.raycaster.interfaces;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Classe que contém os atributos e métodos do layout manager personalizado da
 * engine.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class LayoutEngine implements LayoutManager {
    private Component hudPanel;
    private Component animacaoPanel;

    /**
     * Adiciona um componente ao layout manager.
     * @param name Nome do componente
     * @param comp Referência ao componente
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        switch (name) {
            case "hud" -> hudPanel = comp;
            case "animacao" -> animacaoPanel = comp;
        }
    }

    /**
     * Remove um componente do layout manager.
     * @param comp Componente a ser removido
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        if (comp == hudPanel) {
            hudPanel = null;
        } else if (comp == animacaoPanel) {
            animacaoPanel = null;
        }
    }

    /**
     * Devolve o tamanho de layout preferido.
     * @param parent "Parent" associado ao layout manager
     * @return Retorna a dimensão de espaço preferida pelo layout
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension hudSize = hudPanel.getPreferredSize();
        Dimension animacaoSize = animacaoPanel.getPreferredSize();

        int width = Math.max(animacaoSize.width, hudSize.width);
        int height = hudSize.height + animacaoSize.height;

        return new Dimension(width, height);
    }

    /**
     * Devolve o tamanho mínimo que o layout deve conter.
     * @param parent "Parent" associado ao layout manager
     * @return Retorna a dimensão mínima de tamanho do layout
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * Estipula a regra geral de comportamento dos componentes nesse layout manager.
     * @param parent "Parent" associado ao layout manager
     */
    @Override
    public void layoutContainer(Container parent) {
        Dimension parentSize = parent.getSize();
        Insets insets = parent.getInsets();

        int hudWidth = parentSize.width - insets.left - insets.right;
        int hudHeight = parentSize.height / 8;

        int animacaoWidth = parentSize.width / 2;
        int animacaoHeight = parentSize.height / 2;

        int hudX = insets.left;
        int hudY = parentSize.height - insets.bottom - hudHeight;

        int animacaoX = parentSize.width - insets.right - animacaoWidth;
        int animacaoY = parentSize.height - insets.bottom - animacaoHeight;

        hudPanel.setBounds(hudX, hudY, hudWidth, hudHeight);
        animacaoPanel.setBounds(animacaoX, animacaoY, animacaoWidth, animacaoHeight);
    }
}