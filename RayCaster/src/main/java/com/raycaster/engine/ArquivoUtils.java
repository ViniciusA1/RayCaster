package com.raycaster.engine;

import com.raycaster.itens.*;


import java.io.File;
import java.io.FileInputStream;
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
    
    public static Properties lePropriedade(String nomeArquivo) {
        Properties propriedades = new Properties();
        
        nomeArquivo += FORMATO_DADOS;

        try {
            FileInputStream streamTemporario = new FileInputStream(nomeArquivo);
            propriedades.load(streamTemporario);
            streamTemporario.close();
        } catch (IOException e) {
            System.err.println("Erro! " + e.getMessage());
            return null;
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
    
    public static <T extends Item> List<T> leItens(String nomeArquivo, Class<T> classeItem) {
        List<T> listaItens = new ArrayList<>();
        nomeArquivo += classeItem.getSimpleName().toLowerCase();
        
        Properties dado = lePropriedade(nomeArquivo);
        String tipo;
        
        if(dado == null)
            return null;
        
        int id = 1;
        
        while((tipo = dado.getProperty("type" + id)) != null) {
            T itemLido = criaItem(dado, tipo, id);
            
            if(itemLido != null)
                listaItens.add(itemLido);
            
            id++;
        }
        
        return listaItens;
    }
    
    private static <T extends Item> T criaItem(Properties dado, String tipo, int id) {
        T itemLido;
        
        String nome = dado.getProperty("nome" + id);
        long cooldown = Long.parseLong(dado.getProperty("cooldown" + id));
        
        switch(tipo) {
            case "ArmaLonga" -> {
                int municao = Integer.parseInt(dado.getProperty("municaoMaxima" + id));
                int pente = Integer.parseInt(dado.getProperty("tamanhoPente" + id));
                double dano = Double.parseDouble(dado.getProperty("dano" + id));
                
                itemLido = (T) new ArmaLonga(nome, municao, pente, cooldown, dano);
            }
            case "ArmaCurta" -> {
                int durabilidade = Integer.parseInt(dado.getProperty("durabilidadeMaxima" + id));
                double dano = Double.parseDouble(dado.getProperty("dano" + id));
                
                itemLido = (T) new ArmaCurta(nome, durabilidade, cooldown, dano);
            }
            default -> {
                return null;
            }
        }
        
        return itemLido;
    }
}
