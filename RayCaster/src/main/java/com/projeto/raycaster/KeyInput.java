package com.projeto.raycaster;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vinic
 */
public class KeyInput extends KeyAdapter {
    private final HashMap<Integer, Runnable> keyState;
    private final Set<Integer> pressedKeys;

    public KeyInput() {
        keyState = new HashMap<>();
        pressedKeys = new HashSet<>();
    }
    
    public void adicionaKey(int codigoTecla, Runnable metodo) {
        keyState.put(codigoTecla, metodo);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        pressedKeys.add(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.remove(keyCode);
    }
    
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
