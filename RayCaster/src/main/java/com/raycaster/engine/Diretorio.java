package com.raycaster.engine;

import java.io.File;

/**
 * Classe que guarda atributos e métodos estáticos relacionados a manipulação
 * de caminhos e diretórios.
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Diretorio {
    /**
     * Caminho para os modelos do jogo.
     */
    public static final String MODELOS = "modelos" + File.separator;
    
    /**
     * Caminho para os sons do jogo.
     */
    public static final String SONS = "sons" + File.separator;
    
    /**
     * Caminho para os dados do jogo.
     */
    public static final String DADOS = "dados" + File.separator;
    
    /**
     * Caminho para os modelos de hud do jogo.
     */
    public static final String SPRITE_HUD = MODELOS + "hud" + File.separator;
    
    /**
     * Caminho para os modelos de itens do jogo.
     */
    public static final String SPRITE_ITENS = MODELOS + "itens" + File.separator;
    
    /**
     * Caminho para os modelos de parede do jogo.
     */
    public static final String TEXTURA_PAREDE = MODELOS + "paredes" + File.separator;
    
    /**
     * Caminho para os dados de itens do jogo.
     */
    public static final String DADOS_ITENS = DADOS + "itens" + File.separator;
    
    /**
     * Caminho para os dados de entidades do jogo.
     */
    public static final String DADOS_ENTIDADES = DADOS + "entidades" + File.separator;
    
    /**
     * Construtor privado para evitar instâncias da classe.
     */
    private Diretorio() {}
    
    
    /**
     * Converte o caminho no nome de arquivo padrão definido pelo programa.
     * @param caminho Caminho do arquivo a ser convertido
     * @param estado Estado relacionado ao arquivo
     * @return Retorna uma string contendo o nome de arquivo convertido
     */
    public static String convertePath(String caminho, Estado estado) {
        return caminho + File.separator + estado.name().toLowerCase();
    }
}
