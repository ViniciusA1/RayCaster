package com.raycaster.entidades;

import com.raycaster.itens.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que guarda os atributos e métodos do inventário do jogador.
 * @author Vinicius Augusto
 * @author Bruno Zara
 * @param <T> Parâmetro T genérico que indica qual o tipo do item guardado
 */
public class Inventario<T extends Item> {
    private List<T> objetos;
    private int tamanhoMaximo;

    /**
     * Construtor do inventário.
     */
    public Inventario() {
        objetos = new ArrayList<>();
        this.tamanhoMaximo = 8;
    }
    
    /**
     * Devolve um item do inventário a mão do jogador.
     * @param indice Índice do objeto a ser devolvido
     * @return Retorna a referência para o objeto desejado
     */
    public T getObjeto(int indice) {
        return objetos.get(indice);
    }
    
    /**
     * Devolve o tamanho máximo do inventário.
     * @return Retorna o tamanho máximo do inventário
     */
    public int getTamanhoMaximo() {
        return tamanhoMaximo;
    }
    
    /**
     * Devolve o tamanho já ocupado pelos objetos.
     * @return Retorna o tamanho ocupado pelos objetos
     */
    public int getTamanhoOcupado() {
        return objetos.size();
    }
    
    /**
     * Guarda um objeto no inventário.
     * @param novoObjeto Novo objeto a ser inserido
     */
    public void guardaObjeto(T novoObjeto) {
        if(getTamanhoOcupado() < tamanhoMaximo)
            objetos.add(novoObjeto);
    }
    
    /**
     * Guarda uma lista inteira de objetos no inventário.
     * @param novosObjetos Novos objetos a serem inseridos
     */
    public void guardaObjeto(List<T> novosObjetos) {
        if(novosObjetos.size() <= tamanhoMaximo)
            objetos = novosObjetos;
    }
}