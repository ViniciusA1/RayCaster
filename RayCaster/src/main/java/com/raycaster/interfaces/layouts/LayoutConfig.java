package com.raycaster.interfaces.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class LayoutConfig implements LayoutManager {
    private static final int COMPONENTS_PER_ROW = 2;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
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
            int x = 0;
            int y = 0;
            
            int parentWidth = parent.getWidth();
            int parentHeight = parent.getHeight();
            
            int gapHorizontal, alturaAnterior = 0, divisaoHorizontal;
            
            Component component = parent.getComponent(0);
            
            component.setSize(component.getPreferredSize().width, 
                    component.getPreferredSize().height);
            
            component.setLocation(x, y);
            
            divisaoHorizontal = (parentWidth / 8);
            
            gapHorizontal = (parentWidth / 2) -
                    parent.getComponent(1).getPreferredSize().width - divisaoHorizontal;
            
            x = gapHorizontal;
            y += component.getSize().height;

            for (int i = 1; i < parent.getComponentCount() - 1; i++) {
                component = parent.getComponent(i);
                
                component.setSize(component.getPreferredSize().width, 
                        component.getPreferredSize().height);
                
                component.setLocation(x, y);
                 
                if(i % COMPONENTS_PER_ROW == 0) {
                    x = gapHorizontal;
                    y += Math.max(component.getSize().height, alturaAnterior);
                } else {
                    x = (parentWidth / 2) + divisaoHorizontal;
                    alturaAnterior = component.getSize().height;
                }
            }
            
            component = parent.getComponent(parent.getComponentCount() - 1);
            
            component.setSize(component.getPreferredSize().width, 
                    component.getPreferredSize().height);
            
            component.setLocation((parentWidth / 2) - (component.getSize().width / 2), 
                    parentHeight - component.getSize().height);
        }
    }
}