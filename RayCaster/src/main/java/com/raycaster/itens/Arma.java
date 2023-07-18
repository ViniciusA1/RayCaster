package com.raycaster.itens;

import com.raycaster.engine.Estado;
import com.raycaster.entidades.Entidade;
import com.raycaster.mapa.Mapa;
import java.util.EnumSet;

/**
 * Classe que contém os atributos e métodos de cada arma do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public abstract class Arma extends Item {

    private double dano;

    private double range;

    /**
     * Construtor da classe que recebe os atributos necessários para criação da
     * arma.
     *
     * @param nome Nome da arma
     * @param cooldown "Cooldown" de uso da arma
     * @param possiveisEstados Possíveis estados da arma
     * @param dano Dano máximo da arma
     * @param range
     */
    public Arma(String nome, long cooldown, EnumSet<Estado> possiveisEstados, 
            double dano, double range) {
        super(nome, cooldown, possiveisEstados);

        this.dano = dano;
        this.range = range;
    }

    /**
     * Devolve o dano máximo vinculado à arma.
     *
     * @return Retorna o dano da arma
     */
    public double getDano() {
        return dano;
    }

    public int verificaHit(Entidade usuario, Mapa mapaAtual) {
        double anguloRaio = usuario.getAngulo();

        int entidadeID = usuario.getID();
        double entidadeX = usuario.getX();
        double entidadeY = usuario.getY();

        int eixo;
        int posX, posY;
        int direcaoX, direcaoY;
        double deltaX, deltaY;
        double distanciaX, distanciaY;
        double cosRaio = Math.cos(anguloRaio);
        double sinRaio = Math.sin(anguloRaio);

        // Posição inicial do ponto extremo do raio
        posX = (int) entidadeX;
        posY = (int) entidadeY;

        // Determina o quanto é possivel andar em cada unidade do mapa
        deltaX = (cosRaio == 0) ? Double.MAX_VALUE : Math.abs(1 / cosRaio);
        deltaY = (sinRaio == 0) ? Double.MAX_VALUE : Math.abs(1 / sinRaio);

        // Verifica a orientação em X do raio
        if (cosRaio < 0) {
            direcaoX = -1;
            distanciaX = (entidadeX - posX) * deltaX;
        } else {
            direcaoX = 1;
            distanciaX = (posX + 1.0 - entidadeX) * deltaX;
        }

        // Verifica a orientação em Y do raio
        if (sinRaio < 0) {
            direcaoY = -1;
            distanciaY = (entidadeY - posY) * deltaY;
        } else {
            direcaoY = 1;
            distanciaY = (posY + 1.0 - entidadeY) * deltaY;
        }

        // Percorre o mapa até encontrar o primeiro obstáculo
        while (true) {

            // Verifica qual componente do raio é menor e aumenta-a
            if (distanciaX < distanciaY) {
                distanciaX += deltaX;
                posX += direcaoX;
                eixo = 0;
            } else {
                distanciaY += deltaY;
                posY += direcaoY;
                eixo = 1;
            }

            // Checa a colisão do raio com a parede
            if (mapaAtual.checaColisao(posX, posY, 
                    entidadeID)) {
                break;
            }
        }
        
        double distancia;

        if (eixo == 0) {
            distancia = distanciaX - deltaX;
        } else {
            distancia = distanciaY - deltaY;
        }
        
        return (distancia > range) ? 0 : 
                mapaAtual.getValor(posX, posY);
    }

}
