package com.raycaster.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 *
 * @author vinicius
 */
public class ArquivoUtils {

    public final static String FORMATO_IMAGEM = ".png";
    public final static String FORMATO_SOM = ".wav";
    public final static String FORMATO_DADOS = ".properties";

    public static Properties leDados(String nomeArquivo) {
        Properties propriedades = new Properties();
        FileInputStream streamDeInput;
        
        nomeArquivo += FORMATO_DADOS;

        try {
            streamDeInput = new FileInputStream(nomeArquivo);
            propriedades.load(streamDeInput);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de propriedades: " + e.getMessage());
            return null;
        }
        
        for (String key : propriedades.stringPropertyNames()) {
            String value = propriedades.getProperty(key);
            
            if (value != null)
                propriedades.setProperty(key, value.strip());
        }

        try {
            streamDeInput.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar o arquivo de propriedades: " + e.getMessage());
        }

        return propriedades;
    }

    public static ImageIcon leImagem(String nomeImagem) {
        nomeImagem += FORMATO_IMAGEM;

        File arquivoImagem = new File(nomeImagem);

        if (!arquivoImagem.exists()) {
            return null;
        }

        return new ImageIcon(nomeImagem);
    }
}
