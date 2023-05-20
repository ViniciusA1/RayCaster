package com.projeto.testeraycasting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author vinicius
 */
public class Mapa {
    private int[][] grid;
    private int limite;
    
    public Mapa() {
        grid = null;
        limite = 0;
    }

    public Mapa(int[][] grid, int limite) {
        this.grid = grid;
        this.limite = limite;
    }

    public int getLimite() {
        return limite;
    }
    
    public int getValor(int linha, int coluna) {
        return grid[linha][coluna];
    }
    
    public void setValor(int linha, int coluna, int valor) {
        grid[linha][coluna] = valor;
    }
    
    public void carregaMapa(String nomeMapa) {
        File arquivoMapa = new File(nomeMapa);
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
    
    public void liberaMapa() {
        grid = null;
        limite = 0;
    }
}
