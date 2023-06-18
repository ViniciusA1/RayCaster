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

    /**
     * Construtor da textura, recebe os dados necessários para inicialização.
     * @param ID ID da textura no array
     * @param textura Dados de cada pixel da textura
     * @param tamanho Tamanho inteiro da textura
     * @param nome Nome da textura
     * @param posX Posição em x da textura
     * @param posY Posição em y da textura
     */
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
     */
    public int getID() {
        return ID;
    }

    /**
     * Método que devolve uma vetor contendo o RGB dos pixeis da textura carregada
     * @return um vetor de inteiros contando o RGB dos pixeis da textura carregada
     */
    public int[] getTextura() {
        return textura;
    }
    
    /**
     * Devolve o pixel desejado pelo usuário.
     * @param index Índice do pixel no vetor
     * @return Retorna os dados do pixel
     */
    public int getPixel(int index) {
        return textura[index];
    }

    /**
     * Método que devolve o tamanho da textura carregada
     * @return tamanho da textura carregada
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Método que devolve o nome da textura carregada
     * @return nome da textura carregada
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método que retorna a posição horizontal da textura atual no texture panel
     * @return a posição horizontal da textura atual no texture panel
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Método que retorna a posição Vertical da textura atual no texture panel
     * @return a posição vertical da textura atual no texture panel
     */
    public int getPosY() {
        return posY;
    }
    
    /**
     * Método que convete o vetor de RGB para uma bufferedImage
     * @return Uma bufferedImage contendo a textura atual
     */
    public BufferedImage getRGB(){
        BufferedImage imagem = new BufferedImage(this.getTamanho(), this.getTamanho(), BufferedImage.TYPE_INT_RGB);
        imagem.setRGB(0, 0, this.getTamanho(), this.getTamanho(), this.getTextura(), 0, this.getTamanho());
        return imagem;
    }
    
    
    
    /**
     * Método que carrega todas as texturas na pasta de textura
     * @param diretorio diretorio que contem as texturas
     * @return um ArrayList contendo todas as testuras contidas em diretorio
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
     * Método que busca uma textura em um arraylist com base no id informado
     * @param texturas Arraylist contendo todas as texturas carregadas
     * @param id O ID da textura que esta sendo buscada
     * @return A textura com o ID desejado ou null caso não encontrado
     */
    public static Textura getTextura(ArrayList<Textura> texturas, int id){
        for(Textura aux: texturas){
            if(id == aux.getID()){
                return aux;
            }
        }
        return null;
    }
    
    /**
     * Converte o objeto textura pra string.
     * @return Retorna uma string com o objeto convertido
     */
    @Override
    public String toString(){
        return (ID + " - " + nome);
    }
    
}
