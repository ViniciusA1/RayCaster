package com.raycaster.engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe que contém os atributos e métodos do ouvinte de teclado principal
 * da engine.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class KeyInput extends KeyAdapter {
    private final HashMap<Integer, Runnable> keyState;
    private final Set<Integer> pressedKeys;

    /**
     * Construtor da classe que inicia o mapa de teclas e seu conjunto.
     */
    public KeyInput() {
        keyState = new HashMap<>();
        pressedKeys = new HashSet<>();
    }
    
    /**
     * Adiciona uma "keybinding" ao mapa de teclas.
     * @param codigoTecla Código inteiro da tecla
     * @param metodo Método ao qual a tecla estará associado
     */
    public void adicionaKey(int codigoTecla, Runnable metodo) {
        keyState.put(codigoTecla, metodo);
    }

    /**
     * Verifica e trata o evento de "pressionar" uma tecla.
     * @param e Evento ocorrido
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        pressedKeys.add(keyCode);
    }

    /**
     * Verifica e trata o evento de "soltar" uma tecla.
     * @param e Evento ocorrido
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.remove(keyCode);
    }
    
    /**
     * Executa uma série de métodos com base nos eventos de teclado coletados.
     */
    public void executaMetodo() {
        List<Runnable> actions = new ArrayList<>();

        for (int keyCode : pressedKeys) {
            Runnable action = keyState.get(keyCode);
            if (action != null) {
                actions.add(action);
            }
        }

        for (Runnable action : actions) {
            action.run();
        }
    }
}
