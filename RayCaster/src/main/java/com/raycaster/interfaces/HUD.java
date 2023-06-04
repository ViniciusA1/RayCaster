package com.raycaster.interfaces;

import com.raycaster.engine.Diretorio;
import com.raycaster.entidades.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe que contém os atributos e métodos da HUD principal do jogador.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class HUD extends JPanel {

    private final List<ImageIcon> sprite;
    private final Player jogador;
    private JLabel textoVida;
    private JLabel textoMunicao;

    /**
     * Construtor da classe que recebe a referência do jogador ao qual a HUD está
     * associada.
     * @param jogador Jogador ao qual a HUD está associada
     */
    public HUD(Player jogador, Font fontePersonalizada) {
        setLayout(new LayoutHUD(fontePersonalizada));
        sprite = new ArrayList<>();
        this.jogador = jogador;

        carregaComponentes();
        carregaSprites();
    }

    /**
     * Carrega todos os componentes relacionados a HUD.
     */
    private void carregaComponentes() {
        textoVida = new JLabel(Double.toString(jogador.getVidaAtual()));
        textoVida.setForeground(Color.WHITE);
        
        textoMunicao = new JLabel(Integer.toString(jogador.getQtdConsumivel()));
        textoMunicao.setForeground(Color.WHITE);
        
        add(textoMunicao);
        add(textoVida);
    }

    /**
     * Carrega todos os sprites relacionados a HUD.
     */
    private void carregaSprites() {
        ImageIcon mainHUD = new ImageIcon(Diretorio.SPRITE_HUD + "main.png");
        sprite.add(mainHUD);
    }

    /**
     * Retorna um dos sprite da HUD.
     * @param index Indice do sprite a ser encontrado
     * @return Retorna o sprite indicado pelo indice
     */
    public ImageIcon getSprite(int index) {
        return sprite.get(index);
    }

    /**
     * Atualiza o texto do item quando trocado.
     */
    public void atualizaItem() {
        textoMunicao.setText(Integer.toString(jogador.getQtdConsumivel()));
    }

    /**
     * Renderiza os componentes do painel de forma customizada.
     * @param g Atributo que renderiza gráficos no painel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        textoVida.setText(Double.toString(jogador.getVidaAtual()));
        textoMunicao.setText(Integer.toString(jogador.getQtdConsumivel()));
        g.drawImage(sprite.get(0).getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
