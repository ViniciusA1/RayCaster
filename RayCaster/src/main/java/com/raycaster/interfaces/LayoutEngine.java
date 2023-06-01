package com.raycaster.interfaces;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 *
 * @author vinicius
 */
public class LayoutEngine implements LayoutManager {
    private Component hudPanel;
    private Component animacaoPanel;

    @Override
    public void addLayoutComponent(String name, Component comp) {
        switch (name) {
            case "hud" -> hudPanel = comp;
            case "animacao" -> animacaoPanel = comp;
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        if (comp == hudPanel) {
            hudPanel = null;
        } else if (comp == animacaoPanel) {
            animacaoPanel = null;
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension hudSize = hudPanel.getPreferredSize();
        Dimension animacaoSize = animacaoPanel.getPreferredSize();

        int width = Math.max(animacaoSize.width, hudSize.width);
        int height = hudSize.height + animacaoSize.height;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

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