package com.raycaster.engine;

import com.raycaster.entidades.Entidade;
import com.raycaster.entidades.Player;
import com.raycaster.itens.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 * Classe que guarda os atributos e métodos estáticos para manipulação de arquivos
 * (carregamento e leitura).
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class ArquivoUtils {

    public final static String FORMATO_IMAGEM = ".png";
    public final static String FORMATO_SOM = ".wav";
    public final static String FORMATO_DADOS = ".properties";
    
    
    /**
     * 
     */
    private static class StripProperties extends Properties {
        @Override
        public String getProperty(String key) {
            String valor = super.getProperty(key);
            
            if (valor != null)
                valor = valor.strip();
            
            return valor;
        }
    }

    /**
     *
     * @param nomeArquivo
     * @return
     */
    public static Properties lePropriedade(String nomeArquivo) {
        StripProperties propriedades = new StripProperties();

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

    public static <T> List<T> leObjetos(String nomeArquivo, Class<T> classeObjeto) {
        List<T> listaItens = new ArrayList<>();
        nomeArquivo += classeObjeto.getSimpleName();

        Properties dado = lePropriedade(nomeArquivo);
        String tipo;

        if (dado == null) {
            return null;
        }

        int id = 1;

        while ((tipo = dado.getProperty("type" + id)) != null) {
            T objetoLido = null;
            
            if(Item.class.isAssignableFrom(classeObjeto))
                objetoLido = (T) criaItem(dado, tipo, id);
            else if(Entidade.class.isAssignableFrom(classeObjeto))
                objetoLido = (T) criaEntidade(dado, tipo, id);

            if (objetoLido != null)
                listaItens.add(objetoLido);

            id++;
        }

        return listaItens;
    }

    private static <T extends Item> T criaItem(Properties dado, String tipo, int id) {
        T itemCriado;

        String nome = dado.getProperty("nome" + id);
        long cooldown = Long.parseLong(dado.getProperty("cooldown" + id));
        
        String[] estados = dado.getProperty("estados" + id).split(",");
        EnumSet<Estado> possiveisEstados = EnumSet.noneOf(Estado.class);
        
        for(String estadoAux : estados) {
            Estado novoEstado = Estado.valueOf(estadoAux.strip());
            possiveisEstados.add(novoEstado);
        }

        switch (tipo) {
            case "ArmaLonga" -> {
                int municao = Integer.parseInt(dado.getProperty("municaoMaxima" + id));
                int pente = Integer.parseInt(dado.getProperty("tamanhoPente" + id));
                double dano = Double.parseDouble(dado.getProperty("dano" + id));

                itemCriado = (T) new ArmaLonga(nome, municao, 
                        pente, possiveisEstados, cooldown, dano);
            }
            case "ArmaCurta" -> {
                int durabilidade = Integer.parseInt(dado.getProperty("durabilidadeMaxima" + id));
                double dano = Double.parseDouble(dado.getProperty("dano" + id));

                itemCriado = (T) new ArmaCurta(nome, durabilidade,
                        possiveisEstados, cooldown, dano);
            }
            default -> {
                return null;
            }
        }

        return itemCriado;
    }
    
    private static <T extends Entidade> T criaEntidade(Properties dado, String tipo, int id) {
        T entidadeCriada;
        
        double vidaMaxima = Double.parseDouble(dado.getProperty("vidaMaxima" + id)); 
        double x = Double.parseDouble(dado.getProperty("x" + id)); 
        double y = Double.parseDouble(dado.getProperty("y" + id));
        double largura = Double.parseDouble(dado.getProperty("largura" + id));
        double velocidade = Double.parseDouble(dado.getProperty("velocidade" + id)); 
        int fov = Integer.parseInt(dado.getProperty("fov" + id));
        double FOG = Double.parseDouble(dado.getProperty("FOG" + id));
        
        String[] estados = dado.getProperty("estados" + id).split(",");
        EnumSet<Estado> possiveisEstados = EnumSet.noneOf(Estado.class);
        
        for(String estadoAux : estados) {
            Estado novoEstado = Estado.valueOf(estadoAux.strip());
            possiveisEstados.add(novoEstado);
        }
        
        switch(tipo) {
            case "Player" -> {
                entidadeCriada = (T) new Player(vidaMaxima, x, y, largura, velocidade, fov, FOG, possiveisEstados);
            }
            default -> entidadeCriada = null;
        }
        
        return entidadeCriada;
    }
}