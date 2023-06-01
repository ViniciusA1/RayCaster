package com.raycaster.interfaces;

import com.raycaster.engine.Estado;
import com.raycaster.engine.Diretorio;
import com.raycaster.entidades.Player;
import com.raycaster.engine.Animacao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vinicius
 */
public class HUD extends JPanel {

    private final List<ImageIcon> sprite;
    private final Player jogador;
    private Animacao animacaoRetrato;
    private Font fontePersonalizada;
    private JLabel textoVida;
    private JLabel textoMunicao;

    public HUD(Player jogador) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        sprite = new ArrayList<>();
        this.jogador = jogador;

        fontePersonalizada = carregaFonte();

        carregaComponentes(fontePersonalizada);
        carregaSprites();
    }

    private Font carregaFonte() {
        Font fonteCustomizada = null;

        try {
            fonteCustomizada = Font.createFont(Font.TRUETYPE_FONT, new File(Diretorio.SPRITE_HUD + "font.ttf"));
        } catch (FontFormatException | IOException ex) {

        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(fonteCustomizada);

        return fonteCustomizada;
    }

    private void carregaComponentes(Font fonteCustom) {
        textoVida = new JLabel(Double.toString(jogador.getVidaAtual()));
        textoVida.setFont(fonteCustom.deriveFont(50f));
        textoVida.setForeground(Color.WHITE);

        textoMunicao = new JLabel(Integer.toString(jogador.getQtdConsumivel()));
        textoMunicao.setFont(fonteCustom.deriveFont(50f));
        textoMunicao.setForeground(Color.WHITE);

        add(textoMunicao);
        add(textoVida);
    }

    private void carregaSprites() {
        ImageIcon mainHUD = new ImageIcon(Diretorio.SPRITE_HUD + "main.png");
        animacaoRetrato = new Animacao(Diretorio.SPRITE_HUD + "player", Estado.OCIOSO);
        sprite.add(mainHUD);
    }

    public ImageIcon getSprite(int index) {
        return sprite.get(index);
    }

    public void atualizaItem() {
        textoMunicao.setText(Integer.toString(jogador.getQtdConsumivel()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        textoVida.setText(Double.toString(jogador.getVidaAtual()));
        textoMunicao.setText(Integer.toString(jogador.getQtdConsumivel()));
        g.drawImage(sprite.get(0).getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
