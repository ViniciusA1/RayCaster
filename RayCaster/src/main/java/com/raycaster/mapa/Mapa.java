package com.raycaster.mapa;

import com.raycaster.mapa.Coords.Face;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe responsavel por armazenar os mapas
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Mapa{
    private String nomeMapa;
    private int[][] grid;
    private int limite;
    private final int tamanhoBloco = 32;
    private MapGroup grupo;
    private int playerSpawnX;
    private int playerSpawnY;
    
    
    /**
     * Construtor de instâncias do tipo mapa
     * @param nomeMapa Nome associado ao mapa e também o nome do arquivo no qual o mapa será salvo
     * @param limite Tamanho do mapa = LimitexLimete
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public Mapa(String nomeMapa, int limite){
        this.nomeMapa = nomeMapa;
        this.grid = new int[limite][limite];
        this.limite = limite;
        for(int i = 0; i< limite; i++){
            for(int j = 0; j<limite; j++){
                grid[i][j] = 0;
            }
        } 
        grupo = null;
    }

    /**
     * Metodo de Get para pegar o limite
     * @return o limite do mapa 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getLimite() {
        return limite;
    }
    
    /**
     * Metodo que devolve o elemento da grid no determinado valor
     * @param linha coordenada X
     * @param coluna coordenada Y
     * @return o elemento na posição informada dividio pelo tamanho do bloco
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getValor(int linha, int coluna) {
        return grid[linha / tamanhoBloco][coluna / tamanhoBloco];
    }
    
    /**
     * Metodo que devolve o elemento da grid no determinado valor
     * @param linha coordenada X
     * @param coluna coordenada Y
     * @return o elemento na posição informada
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getID(int linha, int coluna) {
        return grid[linha][coluna];
    }

    /**
     * Metodo que devolve o nome do mapa
     * @return o nome do mapa
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public String getNomeMapa() {
        return nomeMapa;
    }
    
    /**
     * Metodo que devolve o tamanho do bloco
     * @return o tamanho do bloco
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public int getTamanhoBloco() {
        return tamanhoBloco;
    }
    
    /**
     * (WIP)
     * Metodo que retorna o grupo de mapas que este mapa esta associado
     * @return MapGroup associado
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public MapGroup getGrupo() {
        return grupo;
    }
    
    /**
     * Metodo que atribui um valor na posição especificada
     * @param linha coordenada X
     * @param coluna coordenada Y
     * @param valor Valor associado
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public void setValor(int linha, int coluna, int valor) {
        grid[linha][coluna] = valor;
    }

    /**
     * (WIP)
     * Metodo que atribui o mapa atual a um grupo de mapas
     * @param grupo Grupo de mapas a ser associado
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public void setGrupo(MapGroup grupo) {
        this.grupo = grupo;
        grupo.addMapa(this);
    }
    
    

    
    
    
    /**
     * Metodo responsavel por carregar um mapa na pasta de mapas a partir do nome registrado
     * @param nomeMapa nome do mapa a ser carregado
     * @return uma intancia do Mapa encontrado ou null caso não encrente mapa com esse nome
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
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
    
    /**
     * Metodo que re-carrega o mapa atual a partir da memoria
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
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
     * @author Vinicius Augusto
     * @author Bruno Zara
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
    
    /**
     * Metodo que exclui o arquivo associado ao mapa atual
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public  void excluir(){
        String[] aux = nomeMapa.split(".txt");
        File arquivoMapa = new File("maps" + File.separator + aux[0] + ".txt");
        
        arquivoMapa.delete();
         
    }
    
    /**
     * Metodo que carrega todos os mapas salvos na memoria
     * @return Um ArrayList com todos os mapas encontrados
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
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
                        mapaCarregado.setValor(j, i, leitor.nextInt());
                }
                mapas.add(mapaCarregado);
                leitor.close();
            }
        }
        
        
        return mapas;
    } 
    
    
    /**
     * metodo que reseta o mapa atual
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public void liberaMapa() {
        nomeMapa = "";
        grid = null;
        limite = 0;
    }
    
    /**
     * Metodo que checa se o ponto atual esta na borda/parede do mapa ou não
     * @param valorX Coordenada X
     * @param valorY Coordenada Y
     * @return true se o ponto esta na borda/parede do mapa ou false caso contrario
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public boolean checaColisao(double valorX, double valorY) {
        valorX /= tamanhoBloco;
        valorY /= tamanhoBloco;
        
        return (valorX < 0 || valorY < 0 || valorX >= limite || 
                valorY >= limite || grid[(int)valorX][(int)valorY] != 0);
    }
    
    /**
     * (WIP)
     * função que varifica se existe um teletrasporte nesse bloco
     * @param valorX
     * @param valorY
     * @return true se existe um Gate entre mapas e false caso contrario
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public boolean hasGate(int valorX, int valorY) { 
        return grupo.checaGate(this, (int) valorX, (int) valorY);
    }
    
    /**
     * (WIP)
     * metodo que converte as coordenadas atual do mapa para novas coordenados em um novo mapa
     * @param valorX
     * @param valorY
     * @param face a face que esta sendo observada
     * @return as coordenadas no novo mapa
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public Coords converte(int valorX, int valorY, Face face){
        return grupo.converte(this, (int) valorX, (int) valorY, face);
    }
    
    /**
     * Metodo que sobrscreve toString() para retornar uma string formatada mostrado as informaçoes do mapa
     * @return uma String Formatada mostando as informações do mapa
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    @Override
    public String toString(){
        return (getNomeMapa()  + " - " + getLimite());
    }
}
