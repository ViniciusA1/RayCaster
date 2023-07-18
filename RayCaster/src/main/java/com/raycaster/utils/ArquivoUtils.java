package com.raycaster.utils;

import com.raycaster.engine.Estado;
import com.raycaster.entidades.Entidade;
import com.raycaster.entidades.inimigos.Atirador;
import com.raycaster.entidades.jogadores.Player;
import com.raycaster.itens.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Classe que guarda os atributos e métodos estáticos para manipulação de
 * arquivos (carregamento e leitura).
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class ArquivoUtils {

    /**
     * Formato de imagem utilizado no programa.
     */
    public final static String FORMATO_IMAGEM = ".png";

    /**
     * Formato de sons utilizado no programa.
     */
    public final static String FORMATO_SOM = ".wav";

    /**
     * Formato de arquivos de dados utilizados no programa.
     */
    public final static String FORMATO_DADOS = ".properties";

    /**
     * Construtor privado para evitar instanciação.
     */
    private ArquivoUtils() {
    }

    /**
     * Lê e carrega um arquivo de propriedades.
     *
     * @param nomeArquivo Nome do arquivo que deve ser carregado
     * @return Retorna o arquivo carregado
     */
    public static Properties lePropriedade(String nomeArquivo) {
        nomeArquivo += FORMATO_DADOS;
        
        StripProperties propriedades = new StripProperties(nomeArquivo);

        FileInputStream streamTemporario;

        try {
            streamTemporario = new FileInputStream(nomeArquivo);
            propriedades.load(streamTemporario);
            streamTemporario.close();
        } catch (IOException e) {
            System.err.println("Erro! " + e.getMessage());
            return null;
        }

        return propriedades;
    }

    public static void salvaPropriedade(Properties propriedade) {
        OutputStream outputStream;
        
        try {
            StripProperties propertie = (StripProperties) propriedade;
            outputStream = new FileOutputStream(propertie.getPath());
            propertie.store(outputStream, null);
            outputStream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro! " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro!" + ex.getMessage());
        }
    }

    /**
     * Lê e carrega uma imagem.
     *
     * @param nomeImagem Nome da imagem a ser carregada
     * @return Retorna a imagem carregada
     */
    public static ImageIcon leImagem(String nomeImagem) {
        nomeImagem += FORMATO_IMAGEM;

        File arquivoImagem = new File(nomeImagem);

        if (!arquivoImagem.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo " + nomeImagem + " não existe!",
                    "Erro ao carregar imagem",
                    JOptionPane.ERROR_MESSAGE);

            return null;
        }

        return new ImageIcon(nomeImagem);
    }

    public static List<String> leMapas() {
        List<String> mapas = new ArrayList<>();
        File pastaMapas = new File("maps");

        File[] arquivoMapas = pastaMapas.listFiles();
        for (File aux : arquivoMapas) {
            if (!aux.isDirectory()) {
                mapas.add(aux.getName().replaceAll("\\.txt$", ""));
            }
        }

        return mapas;
    }

    /**
     * Lê e carrega uma lista de objetos genéricos.
     *
     * @param <T> Tipo do objeto a ser carregado
     * @param nomeArquivo Nome do arquivo contendo os dados do objeto
     * @param classeObjeto Classe a qual o objeto pertence
     * @return Retorna a lista de objetos criados
     */
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

            if (Item.class.isAssignableFrom(classeObjeto)) {
                objetoLido = (T) criaItem(dado, tipo, id);
            } else if (Entidade.class.isAssignableFrom(classeObjeto)) {
                objetoLido = (T) criaEntidade(dado, tipo, id);
            }

            if (objetoLido != null) {
                listaItens.add(objetoLido);
            }

            id++;
        }

        return listaItens;
    }

    /**
     * Lê dados de um item e cria-o.
     *
     * @param <T> Tipo do item a ser criado
     * @param dado Arquivo de propriedades que contém os dados
     * @param tipo Tipo do item
     * @param id ID do item na lista
     * @return Retorna o item criado
     */
    private static <T extends Item> T criaItem(Properties dado, String tipo, int id) {
        T itemCriado;

        String nome = dado.getProperty("nome" + id);
        long cooldown = Long.parseLong(dado.getProperty("cooldown" + id));

        String[] estados = dado.getProperty("estados" + id).split(",");
        EnumSet<Estado> possiveisEstados = EnumSet.noneOf(Estado.class);

        for (String estadoAux : estados) {
            Estado novoEstado = Estado.valueOf(estadoAux.strip());
            possiveisEstados.add(novoEstado);
        }

        switch (tipo) {
            case "ArmaLonga" -> {
                int municao = Integer.parseInt(dado.getProperty("municaoMaxima" + id));
                int pente = Integer.parseInt(dado.getProperty("tamanhoPente" + id));
                double dano = Double.parseDouble(dado.getProperty("dano" + id));
                double range = Double.parseDouble(dado.getProperty("range" + id));

                itemCriado = (T) new ArmaLonga(nome, municao,
                        pente, possiveisEstados, cooldown, dano, range);
            }
            case "ArmaCurta" -> {
                int durabilidade = Integer.parseInt(dado.getProperty("durabilidadeMaxima" + id));
                double dano = Double.parseDouble(dado.getProperty("dano" + id));
                double range = Double.parseDouble(dado.getProperty("range" + id));

                itemCriado = (T) new ArmaCurta(nome, durabilidade,
                        possiveisEstados, cooldown, dano, range);
            }
            default -> {
                return null;
            }
        }

        return itemCriado;
    }

    /**
     * Lê dados de uma entidade e cria-a.
     *
     * @param <T> Tipo da entidade a ser criada
     * @param dado Arquivo de propriedades contendo os dados da entidade
     * @param tipo Tipo da entidade
     * @param id ID da entidade na lista
     * @return Retorna a entidade criada
     */
    private static <T extends Entidade> T criaEntidade(Properties dado, String tipo, int id) {
        T entidadeCriada;

        String nome = dado.getProperty("nome" + id);
        double vidaMaxima = Double.parseDouble(dado.getProperty("vidaMaxima" + id));
        double largura = Double.parseDouble(dado.getProperty("largura" + id));
        double velocidade = Double.parseDouble(dado.getProperty("velocidade" + id));
        double FOG = Double.parseDouble(dado.getProperty("FOG" + id));

        String[] estados = dado.getProperty("estados" + id).split(",");
        EnumSet<Estado> possiveisEstados = EnumSet.noneOf(Estado.class);

        for (String estadoAux : estados) {
            Estado novoEstado = Estado.valueOf(estadoAux.strip());
            possiveisEstados.add(novoEstado);
        }

        switch (tipo) {
            case "Player" -> {
                int fov = Integer.parseInt(dado.getProperty("fov" + id));
                entidadeCriada = (T) new Player(nome, vidaMaxima, 0, 0, 
                        largura, velocidade, fov, FOG, possiveisEstados);
            }
            case "Atirador" -> {
                entidadeCriada = (T) new Atirador(nome, vidaMaxima, largura, velocidade, 
                        FOG, possiveisEstados);
            }
            default ->
                entidadeCriada = null;
        }

        return entidadeCriada;
    }
}
