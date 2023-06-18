package com.raycaster.engine;

/**
 * Classe que contém uma enumeração dos possíveis estados permitidos no jogo.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public enum Estado {
    /**
     * Estado padrão do objeto.
     */
    OCIOSO,
    
    /**
     * Estado de utilização do objeto.
     */
    USANDO,

    /**
     * Estado de recarregamento do objeto.
     */
    RECARREGANDO,

    /**
     * Estado de mudança de objeto.
     */
    SACANDO
}
