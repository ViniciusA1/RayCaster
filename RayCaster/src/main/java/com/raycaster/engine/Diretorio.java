package com.raycaster.engine;

import java.io.File;

/**
 *
 * @author vinicius
 */
public class Diretorio {
    public static final String MODELOS = "modelos" + File.separator;
    public static final String SONS = "sons" + File.separator;
    public static final String SPRITE_HUD = MODELOS + "hud" + File.separator;
    public static final String SPRITE_ITENS = MODELOS + "itens" + File.separator;
    public static final String TEXTURA_PAREDE = MODELOS + "paredes" + File.separator;
    
    private Diretorio() {}
    
    public static String convertePath(String caminho, Estado estado) {
        return caminho + File.separator + estado.name().toLowerCase();
    }
}
