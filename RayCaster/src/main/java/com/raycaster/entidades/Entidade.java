package com.raycaster.entidades;

import com.raycaster.engine.Estado;
import com.raycaster.mapa.Mapa;
import java.util.EnumSet;

/**
 * Classe que contém os atributos e métodos de cada entidade do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public abstract class Entidade {

    private final Hitbox hitbox;
    private double velocidade;
    private final double FOG;
    private double vidaMaxima;
    private double vidaAtual;
    private EnumSet<Estado> possiveisEstados;

    /**
     * Construtor da classe que recebe os atributos necessários para criação da
     * entidade.
     *
     * @param vidaMaxima Vida máxima da entidade
     * @param x Posição em x
     * @param y Posição em y
     * @param largura Largura da sua hitbox
     * @param velocidade Velocidade da entidade
     * @param FOG Campo de visão máximo da entidade
     * @param possiveisEstados Possiveis estados que a entidade pode assumir
     */
    public Entidade(double vidaMaxima, double x, double y, double largura,
            double velocidade, double FOG, EnumSet<Estado> possiveisEstados) {
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;

        this.FOG = FOG;

        this.possiveisEstados = possiveisEstados;

        hitbox = new Hitbox(x, y, largura);
    }

    /**
     * Busca e retorna a posição no eixo x da entidade.
     *
     * @return Retorna a posição em x
     */
    public double getX() {
        return hitbox.getX();
    }

    /**
     * Busca e retorna a posição no eixo y da entidade.
     *
     * @return Retorna a posição em y
     */
    public double getY() {
        return hitbox.getY();
    }

    /**
     * Busca e retorna a largura total da hitbox da entidade.
     *
     * @return Retorna a sua largura
     */
    public double getLargura() {
        return hitbox.getLargura();
    }

    /**
     * Busca e retorna a velocidade da entidade.
     *
     * @return Retorna a sua velocidade
     */
    public double getVelocidade() {
        return velocidade;
    }

    /**
     * Busca e retorna o campo de visão máximo permitido na entidade.
     *
     * @return Retorna o seu campo de visão
     */
    public double getFOG() {
        return FOG;
    }

    /**
     * Busca e retorna a vida máxima permitida na entidade.
     *
     * @return Retorna a sua vida máxima
     */
    public double getVidaMaxima() {
        return vidaMaxima;
    }

    /**
     * Busca e retorna a quantidade de vida atual da entidade.
     *
     * @return Retorna a sua vida atual
     */
    public double getVidaAtual() {
        return vidaAtual;
    }

    /**
     * Verifica se a entidade possui um determinado estado.
     *
     * @param estadoRecebido Estado que deve ser verificado
     * @return Retorna verdadeiro ou falso para a verificação
     */
    public boolean contemEstado(Estado estadoRecebido) {
        return possiveisEstados.contains(estadoRecebido);
    }

    /**
     * Verifica e trata a colisão da hitbox da entidade com os objetos do mapa
     *
     * @param mapaAtual Mapa em que a colisão ocorre
     * @param posX Posição em x da entidade
     * @param posY Posição em y da entidade
     */
    public void trataColisao(Mapa mapaAtual, double posX, double posY) {
        double tamanho = getLargura();
        double entidadeX = getX() + posX;
        double entidadeY = getY() + posY;

        int startX = (int) Math.floor(entidadeX - tamanho);
        int startY = (int) Math.floor(entidadeY - tamanho);
        int endX = (int) Math.ceil(entidadeX + tamanho);
        int endY = (int) Math.ceil(entidadeY + tamanho);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                if (mapaAtual.checaColisao(x, y)) {
                    return;
                }
            }
        }

        moveX(entidadeX);
        moveY(entidadeY);
    }

    /**
     * Move a entidade para uma determinada posição no eixo x.
     *
     * @param deltaX Nova posição em x
     */
    public void moveX(double deltaX) {
        hitbox.setX(deltaX);
    }

    /**
     * Move a entidade para uma determinada posição no eixo y.
     *
     * @param deltaY Nova posição em y
     */
    public void moveY(double deltaY) {
        hitbox.setY(deltaY);
    }
}
