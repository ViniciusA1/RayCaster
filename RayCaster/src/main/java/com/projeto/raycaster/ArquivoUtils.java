package com.projeto.raycaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author vinicius
 */
public class ArquivoUtils {
    public static String[] leDados(String nomeArquivo) {
        List<String> listaLinhas = new ArrayList<>();
        BufferedReader leitor;

        try {
            leitor = new BufferedReader(new FileReader(nomeArquivo));
            String linhaAux;
            
            while ((linhaAux = leitor.readLine()) != null) {
                System.out.println(linhaAux);
                listaLinhas.add(linhaAux.strip());
            }
            
        } catch (IOException e) {
            return null;
        }
       
        return listaLinhas.toArray(String[]::new);
    }
    
    public static ImageIcon leImagem(String nomeImagem) {
        File arquivoImagem = new File(nomeImagem);
        
        if(!arquivoImagem.exists())
            return null;
       
        return new ImageIcon(nomeImagem);
    }
}
