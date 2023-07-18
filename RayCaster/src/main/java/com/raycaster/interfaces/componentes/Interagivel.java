package com.raycaster.interfaces.componentes;

import java.awt.event.KeyEvent;

/**
 *
 * @author vinicius
 */
public interface Interagivel {
    
    default public void interacaoTeclado(KeyEvent evento) {}
    
    default public void mudaFoco(boolean tipoFoco) {}
}
