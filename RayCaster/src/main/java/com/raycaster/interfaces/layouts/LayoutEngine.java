package com.raycaster.interfaces.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Classe que contém os atributos e métodos do layout manager personalizado da
 * engine.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class LayoutEngine implements LayoutManager {

    private Component hudPanel;
    private Component animacaoPanel;
    private Component miraPanel;
    private Component infoPanel;

    /**
     * Adiciona um componente ao layout manager.
     *
     * @param name Nome do componente
     * @param comp Referência ao componente
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        switch (name) {
            case "hud" ->
                hudPanel = comp;
            case "animacao" ->
                animacaoPanel = comp;
            case "mira" ->
                miraPanel = comp;
            case "info" ->
                infoPanel = comp;
        }
    }

    /**
     * Remove um componente do layout manager.
     *
     * @param comp Componente a ser removido
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        
    }

    /**
     * Devolve o tamanho de layout preferido.
     *
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
     *
     * @param parent "Parent" associado ao layout manager
     * @return Retorna a dimensão mínima de tamanho do layout
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * Estipula a regra geral de comportamento dos componentes nesse layout
     * manager.
     *
     * @param parent "Parent" associado ao layout manager
     */
    @Override
    public void layoutContainer(Container parent) {
        Dimension parentSize = parent.getSize();

        int hudWidth = parentSize.width;
        int hudHeight = parentSize.height / 8;

        int animacaoWidth = parentSize.width;
        int animacaoHeight = parentSize.height;

        int miraWidth = parentSize.width / 50;
        if(miraWidth % 2 == 0)
            miraWidth++;
        
        int infoWidth = parentSize.width / 5;
        int infoHeight = parentSize.height / 5;

        int hudX = 0;
        int hudY = parentSize.height - hudHeight;

        int animacaoX = 0;
        int animacaoY = 0;

        int miraX = (parentSize.width - miraWidth) / 2;
        int miraY = (parentSize.height - miraWidth) / 2;
        
        int infoX = 0;
        int infoY = 0;

        hudPanel.setBounds(hudX, hudY, hudWidth, hudHeight);
        
        animacaoPanel.setBounds(animacaoX, animacaoY, animacaoWidth,
                animacaoHeight);
        
        miraPanel.setBounds(miraX, miraY, miraWidth, 
                miraWidth);
        infoPanel.setBounds(infoX, infoY, infoWidth, 
                infoHeight);
    }
}
