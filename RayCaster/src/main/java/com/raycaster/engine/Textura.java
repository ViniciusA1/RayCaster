package com.raycaster.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * Classe que armazena uma textura
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Textura {
    private int ID;
    private int[] textura;
    private int tamanho;
    private String nome;
    private int posX;
    private int posY;

    public Textura(int ID, int[] textura, int tamanho, String nome, int posX, int posY) {
        this.ID = ID;
        this.textura = textura;
        this.tamanho = tamanho;
        this.nome = nome;
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * metodo que devolve o id da textura carregada
     * @return ID da textura carregada
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getID() {
        return ID;
    }

    /**
     * metodo que devolve uma vetor contendo o RGB dos pixeis da textura carregada
     * @return um vetor de inteiros contando o RGB dos pixeis da textura carregada
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int[] getTextura() {
        return textura;
    }
    
    public int getPixel(int index) {
        return textura[index];
    }

    /**
     * metodo que devolve o tamanho da textura carregada
     * @return tamanho da textura carregada
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * metodo que devolve o nome da textura carregada
     * @return nome da textura carregada
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public String getNome() {
        return nome;
    }

    /**
     * Metodo que retorna a posição horizontal da textura atual no texture panel
     * @return a posição horizontal da textura atual no texture panel
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Metodo que retorna a posição Vertical da textura atual no texture panel
     * @return a posição vertical da textura atual no texture panel
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getPosY() {
        return posY;
    }
    
    /**
     * Metodo que convete o vetor de RGB para uma bufferedImage
     * @return Uma bufferedImage contendo a textura atual
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public BufferedImage getRGB(){
        BufferedImage imagem = new BufferedImage(this.getTamanho(), this.getTamanho(), BufferedImage.TYPE_INT_RGB);
        imagem.setRGB(0, 0, this.getTamanho(), this.getTamanho(), this.getTextura(), 0, this.getTamanho());
        return imagem;
    }
    
    
    
    /**
     * metodo que carrega todas as texturas na pasta de textura
     * @param diretorio diretorio que contem as texturas
     * @return um ArrayList contendo todas as testuras contidas em diretorio
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public static ArrayList<Textura> carregaTexturas(File diretorio) {
        File[] imagens = diretorio.listFiles();
        ArrayList<Textura> texturas = new ArrayList<>();
        
        Arrays.sort(imagens);
        
        try {
            for(File aux: imagens){
                if(aux.exists()  && !aux.isDirectory()){
                    String[] dados = aux.getName().split("-");
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    BufferedImage imagem = ImageIO.read(aux);
                    int size = imagem.getWidth();
                    if(imagem.getHeight() != size){
                        System.err.println("Só podem existir imagens quadradas NxN");
                        System.exit(1);
                    }
                    int[] cor = new int[size * size];
                    imagem.getRGB(0, 0, size, size, cor, 0, size);
                    texturas.add(new Textura(id, cor, size, nome, 0, 0));
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texturas;
    }
    
    /**
     * Metodo que busca uma textura em um arraylist com base no id informado
     * @param texturas Arraylist contendo todas as texturas carregadas
     * @param id O ID da textura que esta sendo buscada
     * @return A textura com o ID desejado ou null caso não encontrado
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public static Textura getTextura(ArrayList<Textura> texturas, int id){
        for(Textura aux: texturas){
            if(id == aux.getID()){
                return aux;
            }
        }
        return null;
    }
    
    @Override
    public String toString(){
        return (ID + " - " + nome);
    }
    
}
