package com.raycaster.engine;



import com.raycaster.interfaces.MenuInicial;

/**
 * Classe que guarda o método principal do programa.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class RayCaster {

    /**
     * Método principal do programa
     * @param args Argumentos de entrada
     */
    public static void main(String[] args) {
        System.out.println(RayCaster.class.getName());
        MenuInicial.inicia();
    }
}
