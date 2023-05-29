package com.raycaster.entidades;

import com.raycaster.itens.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinic
 * @param <T>
 */
public class Inventario<T extends Item> {
    private List<T> objetos;
    private int tamanhoMaximo;

    public Inventario() {
        objetos = new ArrayList<>();
        this.tamanhoMaximo = 8;
    }
    
    public T getObjeto(int indice) {
        return objetos.get(indice);
    }
    
    public int getTamanhoMaximo() {
        return tamanhoMaximo;
    }
    
    public int getTamanhoOcupado() {
        return objetos.size();
    }
    
    public void guardaObjeto(T novoObjeto) {
        if(getTamanhoOcupado() < tamanhoMaximo)
            objetos.add(novoObjeto);
    }
    
    public void guardaObjeto(List<T> novosObjetos) {
        if(novosObjetos.size() <= tamanhoMaximo)
            objetos = novosObjetos;
    }
}