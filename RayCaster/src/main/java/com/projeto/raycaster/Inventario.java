package com.projeto.raycaster;

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
        objetos.add(novoObjeto);
    }
}