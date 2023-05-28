package com.raycaster.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author vinicius
 */
public class Mapa {
    private String nomeMapa;
    private int[][] grid;
    private int limite;
    private final int tamanhoBloco = 32;
    
    @Deprecated
    public Mapa() {
        nomeMapa = "";
        grid = null;
        limite = 0;
    }

    @Deprecated
    public Mapa(int[][] grid, int limite) {
        nomeMapa = "";
        this.grid = grid;
        this.limite = limite;
    }
    
    @Deprecated
    public Mapa(String nomeMapa, int[][] grid, int limite) {
        this.nomeMapa = nomeMapa;
        this.grid = grid;
        this.limite = limite;
    }
    
    
    public Mapa(String nomeMapa, int limite){
        this.nomeMapa = nomeMapa;
        this.grid = new int[limite][limite];
        this.limite = limite;
        for(int i = 0; i< limite; i++){
            for(int j = 0; j<limite; j++){
                grid[i][j] = 0;
            }
        }
    }

    public int getLimite() {
        return limite;
    }
    
    public int getValor(int linha, int coluna) {
        return grid[linha / tamanhoBloco][coluna / tamanhoBloco];
    }

    public String getNomeMapa() {
        return nomeMapa;
    }
    
    public int getTamanhoBloco() {
        return tamanhoBloco;
    }
    
    public void setValor(int linha, int coluna, int valor) {
        grid[linha][coluna] = valor;
    }
    
    
    
    public static Mapa carregaMapa(String nomeMapa) {
        File arquivoMapa = new File("maps" + File.separator + nomeMapa);
        Scanner leitor;
        
        try {
            leitor = new Scanner(arquivoMapa);
        } catch(FileNotFoundException erro) {
            System.err.println("Erro! " + erro.getMessage());
            return null;
        }
        
        int limite = leitor.nextInt();
        Mapa aux = new Mapa(nomeMapa, limite);
        
        for(int i = 0, j; i < limite; i++) {
            for(j = 0; j < limite; j++)
                aux.setValor(i, j, leitor.nextInt());
        }
        
        leitor.close();
        return aux;
    }
    
    
    public void carregar() {
        File arquivoMapa = new File("maps" + File.separator + nomeMapa);
        Scanner leitor;
        
        try {
            leitor = new Scanner(arquivoMapa);
        } catch(FileNotFoundException erro) {
            System.err.println("Erro! " + erro.getMessage());
            return;
        }
        
        limite = leitor.nextInt();
        grid = new int[limite][limite];
        
        for(int i = 0, j; i < limite; i++) {
            for(j = 0; j < limite; j++)
                grid[i][j] = leitor.nextInt();
        }
        
        leitor.close();
    }
    
    /**
     * Metodo para salvar o mapa no arquivo
     * @author BrunoZara
     * @throws java.io.IOException
     */
    public void salvar() throws IOException{
        FileWriter out;
        String[] nomeMapaTXT = nomeMapa.split(".txt");
        File aux = new File("maps" + File.separator + nomeMapaTXT[0] +".txt");
        if (!aux.exists()){
            aux.createNewFile();
        }
        out = new FileWriter(aux);
        BufferedWriter buffout = new BufferedWriter(out);
        buffout.write(Integer.toString(limite));
        buffout.newLine();
        buffout.flush();
        for(int i = 0; i < this.limite; i++){
            for(int j = 0; j < this.limite; j++){
                buffout.write(Integer.toString(grid[i][j]));
                buffout.write(" ");
            }
            buffout.newLine();
            buffout.flush();
        }
        
        buffout.close();
        out.close();       
    }
    
    public  void excluir(){
        String[] aux = nomeMapa.split(".txt");
        File arquivoMapa = new File("maps" + File.separator + aux[0] + ".txt");
        
        arquivoMapa.delete();
         
    }
    
    public static ArrayList<Mapa> carregarMapList(){
        File arquivoMapa = new File("maps");
        Scanner leitor;
        ArrayList<Mapa> mapas = new ArrayList<>();
        
        
        File[] arquivoMapas = arquivoMapa.listFiles();
        for(File aux: arquivoMapas){
            if(!aux.isDirectory()){
                try {
                    leitor = new Scanner(aux);
                } catch(FileNotFoundException erro) {
                    System.err.println("Erro! " + erro.getMessage());
                    continue;
                }
                int tamanho = leitor.nextInt();
                Mapa mapaCarregado = new Mapa(aux.getName(), tamanho);

                for(int i = 0, j; i < tamanho; i++) {
                    for(j = 0; j < tamanho; j++)
                        mapaCarregado.setValor(i, j, leitor.nextInt());
                }
                mapas.add(mapaCarregado);
                leitor.close();
            }
        }
        
        
        return mapas;
    } 
    
    
    
    public void liberaMapa() {
        nomeMapa = "";
        grid = null;
        limite = 0;
    }
    
    public boolean checaColisao(double valorX, double valorY) {
        valorX /= tamanhoBloco;
        valorY /= tamanhoBloco;
        
        return (valorX < 0 || valorY < 0 || valorX >= limite || 
                valorY >= limite || grid[(int)valorX][(int)valorY] != 0);
    }
}