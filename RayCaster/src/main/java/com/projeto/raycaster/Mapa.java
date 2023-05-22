package com.projeto.raycaster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author vinicius
 */
public class Mapa implements Guardar {
    private String nomeMapa;
    private int[][] grid;
    private int limite;
    
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
    }

    public int getLimite() {
        return limite;
    }
    
    public int getValor(int linha, int coluna) {
        return grid[linha][coluna];
    }

    public String getNomeMapa() {
        return nomeMapa;
    }
    
    public void setValor(int linha, int coluna, int valor) {
        grid[linha][coluna] = valor;
    }
    
    
    @Deprecated
    public void carregaMapa(String nomeMapa) {
        File arquivoMapa = new File("maps" + File.pathSeparator + nomeMapa);
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
    
    @Override
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
    @Override
    public void salvar() throws IOException{
        FileWriter out = new FileWriter("maps" + File.pathSeparator + nomeMapa);
        BufferedWriter buffout = new BufferedWriter(out);
        buffout.write(this.limite);
        buffout.newLine();
        buffout.flush();
        for(int i = 0; i < this.limite; i++){
            for(int j = 0; j < this.limite; j++){
                buffout.write(grid[i][j]);
                buffout.newLine();
                buffout.flush();
            }
        }
        buffout.close();
        out.close();
    }
    
    @Override
    public  void excluir(){
        File arquivoMapa = new File("maps" + File.pathSeparator + nomeMapa);
         if(arquivoMapa.exists()){
             arquivoMapa.delete();
         }
    }
    
    public void liberaMapa() {
        nomeMapa = "";
        grid = null;
        limite = 0;
    }
    
    public boolean checaColisao(int valorX, int valorY) {
        return (grid[valorX][valorY] != 0);
    }
}
