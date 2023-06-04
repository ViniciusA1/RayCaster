package com.raycaster.interfaces;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 *
 * @author vinicius
 */
public class LayoutInformacao implements LayoutManager {

    private final Font fontePersonalizada;

    private static final int gap = 10;

    public LayoutInformacao(Font fontePersonalizada) {
        this.fontePersonalizada = fontePersonalizada;
    }

    @Override
    public void addLayoutComponent(String string, Component cmpnt) {
    }

    @Override
    public void removeLayoutComponent(Component cmpnt) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }


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
