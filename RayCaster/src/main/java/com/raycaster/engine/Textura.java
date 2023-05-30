/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raycaster.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author bruno
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

    public int getID() {
        return ID;
    }

    public int[] getTextura() {
        return textura;
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getNome() {
        return nome;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
    
    
    
    
    public static ArrayList<Textura> carregaTexturas(File diretorio) {
        File[] imagens = diretorio.listFiles();
        ArrayList<Textura> texturas = new ArrayList<>();
        try {
            for(File aux: imagens){
                if(aux.exists()  && !aux.isDirectory()){
                    String[] dados = aux.getName().split(" - ");
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    BufferedImage imagem = ImageIO.read(aux);
                    int size = imagem.getWidth();
                    if(imagem.getHeight() != size){
                        System.err.println("só pondem existir imagens quadradas NxN");
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
}
