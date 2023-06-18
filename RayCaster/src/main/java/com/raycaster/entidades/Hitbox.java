package com.raycaster.entidades;

/**
 * Classe que contém os atributos e métodos da 
 * hitbox das entidades do jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Hitbox {
    private double x;
    private double y;
    private final double largura;

    /**
     * Construtor da hitbox, recebe posição inicial e tamanho.
     * @param x Posição inicial em x
     * @param y Posição inicial em y
     * @param largura Largura da hitbox
     */
    public Hitbox(double x, double y, double largura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
    }

    /**
     * Devolve a posição da entidade em x.
     * @return Retorna a coordenada x
     */
    public double getX() {
        return x;
    }

    /**
     * Devolve a posição da entidade em y.
     * @return Retorna a coordenada y
     */
    public double getY() {
        return y;
    }

    /**
     * Devolve o tamanho da hitbox da entidade.
     * @return Retorna a largura da hitbox
     */
    public double getLargura() {
        return largura;
    }

    /**
     * Seta uma nova posição em x da entidade.
     * @param x Nova posição em x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Seta uma nova posição em y da entidade.
     * @param y Nova posição em y
     */
    public void setY(double y) {
        this.y = y;
    }
}
