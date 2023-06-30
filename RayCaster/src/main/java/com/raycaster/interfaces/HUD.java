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
import javax.swing.JProgressBar;

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
    private JProgressBar barraVida;
    private JProgressBar barraMunicao;

    /**
     * Construtor da classe que recebe a referência do jogador ao qual a HUD está
     * associada.
     * @param jogador Jogador ao qual a HUD está associada
     * @param fontePersonalizada Fonte personalizada da HUD
     */
    public HUD(Player jogador, Font fontePersonalizada) {
        setLayout(new LayoutHUD(fontePersonalizada));
        setOpaque(false);
        sprite = new ArrayList<>();
        this.jogador = jogador;

        carregaComponentes();
        carregaSprites();
    }

    /**
     * Carrega todos os componentes relacionados a HUD.
     */
    private void carregaComponentes() {
        Color cor = new Color(255, 0, 0);
        
        textoVida = new JLabel();
        textoVida.setForeground(cor);
        
        barraVida = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
        barraVida.setForeground(cor);
        
        textoMunicao = new JLabel();
        textoMunicao.setForeground(cor);
        
        barraMunicao = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
        barraMunicao.setForeground(cor);
        
        add(barraMunicao);
        add(textoMunicao);
        add(barraVida);
        add(textoVida);
        
        atualizaComponentes();
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
        barraMunicao.setValue(jogador.getConsumivelMax());
        
        System.out.println(jogador.getConsumivelMax());
    }
    
    public void atualizaComponentes() {
        atualizaItem();
        
        textoVida.setText(Double.toString(jogador.getVidaAtual()));
        barraVida.setValue(converteNumero(jogador.getVidaAtual(), 
                jogador.getVidaMaxima()));
    }
    
    private int converteNumero(double atual, double max) {
        return (int) (atual / max) * 100;
    }

    /**
     * Renderiza os componentes do painel de forma customizada.
     * @param g Atributo que renderiza gráficos no painel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        atualizaComponentes();
        
        g.drawImage(sprite.get(0).getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
