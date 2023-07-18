package com.raycaster.interfaces.paineis;

import com.raycaster.engine.Estado;
import com.raycaster.itens.Item;
import com.raycaster.entidades.jogadores.Player;
import com.raycaster.engine.Animacao;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * Classe que contém os atributos e métodos do painel de animações do player.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class AnimacaoItem extends JPanel {

    private final Player jogador;
    private Item itemAtual;
    private Animacao animacaoAtual;

    /**
     * Construtor da classe que recebe a referência para o jogador ao qual o
     * painel está associado.
     *
     * @param jogador
     */
    public AnimacaoItem(Player jogador) {
        this.jogador = jogador;
        this.itemAtual = jogador.getItemAtual();
        this.setOpaque(false);
    }

    /**
     * Troca a animação do painel quando ocorre uma troca de item.
     *
     * @param novoItem Novo item que foi trocado
     */
    public void trocaItem(Item novoItem) {
        this.itemAtual = novoItem;
        animacaoAtual = novoItem.getAnimacao(Estado.OCIOSO);
        animacaoAtual.setFrame(0);
    }
    
    public void mudaAnimacao() {
        animacaoAtual = itemAtual.getAnimacaoAtual();
        animacaoAtual.setFrame(0);
    }

    /**
     * Renderiza o painel na tela de forma customizada.
     *
     * @param g Componente gráfico que renderiza imagens
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image imagemAtual = animacaoAtual.getSpriteImagem();

        int widthX = this.getWidth();
        int heightY = this.getHeight();

        g.drawImage(imagemAtual,
                (widthX / 2) + (int) jogador.getPitch(),
                (heightY / 2) + (int) -((Math.pow(jogador.getPitch(), 2)) / 50),
                widthX, heightY, 0, 0, 
                imagemAtual.getWidth(null), 
                imagemAtual.getHeight(null), null);
        
        int frameAtual = animacaoAtual.atualizaFrame();
        
        if(frameAtual == 0) {
            itemAtual.setEstado(Estado.OCIOSO);
            mudaAnimacao();
        }
    }
}
