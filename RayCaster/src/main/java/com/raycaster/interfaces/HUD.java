package com.raycaster.interfaces;

import com.raycaster.engine.Estado;
import com.raycaster.engine.Diretorio;
import com.raycaster.entidades.Player;
import com.raycaster.engine.Animacao;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
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
        setLayout(new LayoutHUD("font.ttf"));
        sprite = new ArrayList<>();
        this.jogador = jogador;

        carregaComponentes();
        carregaSprites();
    }

    private void carregaComponentes() {
        textoVida = new JLabel(Double.toString(jogador.getVidaAtual()));
        textoVida.setForeground(Color.WHITE);
        
        textoMunicao = new JLabel(Integer.toString(jogador.getQtdConsumivel()));
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
