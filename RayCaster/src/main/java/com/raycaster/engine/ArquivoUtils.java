package com.raycaster.engine;

import com.raycaster.entidades.Hitbox;
import com.raycaster.entidades.Inventario;
import com.raycaster.itens.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static <T> T criaObjeto(String nomeArquivo, Class<T> classeObjeto) {
        Properties propriedades = lePropriedade(nomeArquivo);

        T novoObjeto;

        try {
            novoObjeto = classeObjeto.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.println("Erro! " + ex.getMessage());
            return null;
        }

        Field[] campos = getCampos(classeObjeto);

        for (Field campoAux : campos) {
            campoAux.setAccessible(true);
            String nomeCampo = campoAux.getName().strip();
            String propriedadeCampo = propriedades.getProperty(nomeCampo);

            if (propriedadeCampo == null) {
                continue;
            }

            propriedadeCampo = propriedadeCampo.strip();

            Object atributo = converteDado(propriedadeCampo, campoAux.getType(), classeObjeto);

            try {
                campoAux.set(novoObjeto, atributo);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                System.err.println("Erro! " + ex.getMessage());
                return null;
            }
        }

        return novoObjeto;
    }

    private static Object converteDado(String dado, Class<?> classeDado, Class<?> classeObjeto) {
     
        if (classeDado == String.class) {
            return dado;
        } else if (classeDado == int.class) {
            return Integer.valueOf(dado);
        } else if (classeDado == double.class) {
            return Double.valueOf(dado);
        } else if (classeDado == boolean.class) {
            return Boolean.valueOf(dado);
        } else if (classeDado == long.class) {
            return Long.valueOf(dado);
        } else if (classeDado == Hitbox.class) {
            String[] valor = dado.split(",");

            return new Hitbox(Double.parseDouble(valor[0]), Double.parseDouble(valor[1]),
                    Double.parseDouble(valor[2]));

        } else if (classeDado == Inventario.class) {
            return new Inventario<>();
        } else if(classeDado == Estado.class) {
            return Estado.OCIOSO;
        } else if(classeDado == EfeitosSonoros.class) {
            return new EfeitosSonoros(classeObjeto.getSimpleName().toLowerCase());
        }

        return null;
    }
    
    private static Field[] getCampos(Class<?> classe) {
        List<Field> campos = new ArrayList<>();

        Field[] camposDaClasse = classe.getDeclaredFields();
        campos.addAll(Arrays.asList(camposDaClasse));
        
        Class<?> superClasse = classe.getSuperclass();

        while(superClasse != null) {
            Field[] camposDaSuperClasse = superClasse.getDeclaredFields();
            campos.addAll(Arrays.asList(camposDaSuperClasse));
            superClasse = superClasse.getSuperclass();
        }

        return campos.toArray(Field[]::new);
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

        if (dado == null) {
            return null;
        }

        int id = 1;

        while ((tipo = dado.getProperty("type" + id)) != null) {
            T itemLido = criaItem(dado, tipo, id);

            if (itemLido != null) {
                listaItens.add(itemLido);
            }

            id++;
        }

        return listaItens;
    }

    private static <T extends Item> T criaItem(Properties dado, String tipo, int id) {
        T itemLido;

        String nome = dado.getProperty("nome" + id);
        long cooldown = Long.parseLong(dado.getProperty("cooldown" + id));

        switch (tipo) {
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
