package com.raycaster.utils;

import java.util.Properties;

/**
 * Classe que herda de Properties e sobrescreve seu método de busca em arquivo.
 */
public class StripProperties extends Properties {
    
    private final String PATH;
    
    public StripProperties(String caminho) {
        this.PATH = caminho;
    }
    
    public String getPath() {
        return PATH;
    }

    /**
     * Retorna uma string com o valor da propriedade já formatado com o "strip".
     *
     * @param key Chave para busca do valor
     * @return Retorna a string gerada
     */
    @Override
    public String getProperty(String key) {
        String valor = super.getProperty(key);

        if (valor != null) {
            valor = valor.strip();
        }

        return valor;
    }
}
