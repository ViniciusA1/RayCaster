package com.raycaster.interfaces;

import com.raycaster.engine.Diretorio;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;

/**
 * Classe que guarda os métodos do layout personalizado para a HUD.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class LayoutHUD implements LayoutManager {
    private Font fontePersonalizada;
    
    /**
     * Construtor do layout da HUD.
     * @param nomeFonte Nome da fonte que será utilizada
     */
    public LayoutHUD(String nomeFonte) {
        carregaFonte(nomeFonte);
    }
    
    /**
     * Adiciona um novo componente nesse layout.
     * @param name Nome do componente
     * @param comp Referência do componente
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {}

    /**
     * Remove um componente do layout.
     * @param comp Componente que será removido
     */
    @Override
    public void removeLayoutComponent(Component comp) {}

    /**
     * Seta um tamanho preferido para o layout.
     * @param parent Parent ao qual o layout está associado
     * @return Retorna a nova dimensão preferida
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    /**
     * Seta o tamanho mínimo do layout.
     * @param parent Parent ao qual o layout está associado
     * @return Retorna a dimensão mínima do layout.
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * Define o padrão de comportamento dos componentes no layout
     * @param parent Parent ao qual o layout está associado
     */
    @Override
    public void layoutContainer(Container parent) {
        int width = parent.getWidth();
        int height = parent.getHeight();

        Font fonte = fontePersonalizada.deriveFont(Font.BOLD, (width / 2 + height) / 10);

        Component[] componentes = parent.getComponents();

        int spacing = width / 8;

        int startX = width / 10;
        int y = (height - fonte.getSize()) / 2;

        for (int i = 0; i < componentes.length; i++) {
            Component componenteAux = componentes[i];
            
            if(!componenteAux.isVisible())
                continue;

            componenteAux.setFont(fonte);

            int componentWidth = componenteAux.getPreferredSize().width;
            int componentX = startX + (componentWidth + spacing) * i;

            componenteAux.setBounds(componentX, y, componentWidth, fonte.getSize());
        }

    }

    /**
     * Carrega a fonte personalizada que será aplicada nos componentes.
     * @param nomeFonte Nome da fonte a ser carregada
     */
    private void carregaFonte(String nomeFonte) {
        try {
            File fontFile = new File(Diretorio.SPRITE_HUD + nomeFonte);
            fontePersonalizada = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (IOException | FontFormatException e) {
            System.err.println("Fonte não existe!");
        }
    }

}
