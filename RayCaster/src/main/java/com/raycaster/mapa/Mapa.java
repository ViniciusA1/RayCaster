package com.raycaster.mapa;

import com.raycaster.engine.Estado;
import com.raycaster.entidades.Entidade;
import com.raycaster.entidades.inimigos.Inimigo;
import com.raycaster.utils.ArquivoUtils;
import com.raycaster.utils.Diretorio;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsavel por armazenar os mapas
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Mapa {

    private String nomeMapa;
    private int[][] grid;
    private List<Entidade> entidades;
    private int limite;
    private static final int TAMANHO_BLOCO = 32;
    private int playerSpawnX;
    private int playerSpawnY;

    /**
     * Construtor de instâncias do tipo mapa
     *
     * @param nomeMapa Nome associado ao mapa e também o nome do arquivo no qual
     * o mapa será salvo
     * @param limite Tamanho do mapa = LimitexLimite
     */
    public Mapa(String nomeMapa, int limite) {
        this.nomeMapa = nomeMapa;
        this.grid = new int[limite][limite];
        this.entidades = new ArrayList<>();
        this.limite = limite;
    }

    public void criaEntidade() {
        entidades.clear();
        
        try {
            List<Inimigo> inimigos = ArquivoUtils.leObjetos(
                    Diretorio.DADOS_ENTIDADES, Inimigo.class);
            
            entidades.add(inimigos.get(0));
            entidades.add((Entidade) inimigos.get(0).clone());
            entidades.add((Entidade) inimigos.get(0).clone());
            entidades.add((Entidade) inimigos.get(0).clone());
            entidades.add((Entidade) inimigos.get(0).clone());
            
            entidades.get(0).moveX(256);
            entidades.get(0).moveY(256);
            entidades.get(0).setID(-2);
            entidades.get(0).novaCelula(this);
            
            entidades.get(1).moveX(256);
            entidades.get(1).moveY(128);
            entidades.get(1).setID(-3);
            entidades.get(1).novaCelula(this);
            
            entidades.get(2).moveX(128);
            entidades.get(2).moveY(256);
            entidades.get(2).setID(-4);
            entidades.get(2).novaCelula(this);
            
            entidades.get(3).moveX(512);
            entidades.get(3).moveY(256);
            entidades.get(3).setID(-5);
            entidades.get(3).novaCelula(this);
            
            entidades.get(4).moveX(512);
            entidades.get(4).moveY(512);
            entidades.get(4).setID(-6);
            entidades.get(4).novaCelula(this);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que define o ponto de Spawn do player
     *
     * @param x coordenada X
     * @param y coordenada Y
     */
    public void setSpawn(int x, int y) {
        this.playerSpawnX = x;
        this.playerSpawnY = y;
    }

    /**
     * Metodo que define o ponto de Spawn do player
     *
     * @return int com a coordenada X
     */
    public int getSpawnX() {
        return playerSpawnX;
    }

    /**
     * Devolve a coordenada X do spawn proporcional à escala do mapa.
     *
     * @return Retorna a coordenada transformada
     */
    public double getBlockSpawnX() {
        return (playerSpawnX + 0.5) * TAMANHO_BLOCO;
    }

    /**
     * Metodo que define o ponto de Spawn do player
     *
     * @return int com a coordenada Y
     *
     *
     *
     */
    public int getSpawnY() {
        return playerSpawnY;
    }

    /**
     * Devolve a coordenada Y do spawn proporcional à escala do mapa.
     *
     * @return Retorna a coordenada transformada
     */
    public double getBlockSpawnY() {
        return (playerSpawnY + 0.5) * TAMANHO_BLOCO;
    }

    /**
     * Metodo de Get para pegar o limite
     *
     * @return o limite do mapa
     */
    public int getLimite() {
        return limite;
    }

    /**
     * Metodo que devolve o elemento da grid no determinado valor
     *
     * @param linha coordenada X
     * @param coluna coordenada Y
     * @return o elemento na posição informada dividio pelo tamanho do bloco
     */
    public int getValor(int linha, int coluna) {
        linha /= TAMANHO_BLOCO;
        coluna /= TAMANHO_BLOCO;

        if (linha < 0 || coluna < 0
                || linha >= limite || coluna >= limite) {
            return 0;
        }

        return grid[linha][coluna];
    }

    /**
     * Metodo que devolve o elemento da grid no determinado valor
     *
     * @param linha coordenada X
     * @param coluna coordenada Y
     * @return o elemento na posição informada
     */
    public int getID(int linha, int coluna) {
        return grid[linha][coluna];
    }

    public int getQtdEntidades() {
        return entidades.size();
    }
    
    public Entidade getEntidade(int id) {
        return entidades.get(id);
    }

    public double getEntidadeX(int id) {
        return entidades.get(id).getX();
    }

    public double getEntidadeY(int id) {
        return entidades.get(id).getY();
    }

    public BufferedImage getSpriteImagem(int id) {
        return entidades.get(id).getSpriteAtual();
    }

    public int getSpriteWidth(int id) {
        return entidades.get(id).getSpriteWidth();
    }

    public int getSpriteHeight(int id) {
        return entidades.get(id).getSpriteHeight();
    }

    /**
     * Metodo que devolve o nome do mapa
     *
     * @return o nome do mapa
     */
    public String getNomeMapa() {
        return nomeMapa;
    }

    /**
     * Metodo que devolve o tamanho do bloco
     *
     * @return o tamanho do bloco
     */
    public int getTamanhoBloco() {
        return TAMANHO_BLOCO;
    }

    /**
     * Metodo que atribui um valor na posição especificada
     *
     * @param linha coordenada X
     * @param coluna coordenada Y
     * @param valor Valor associado
     */
    public void setValor(int linha, int coluna, int valor) {
        grid[linha][coluna] = valor;
    }

    public void atualizaInimigos(double playerX, double playerY,
            double deltaTime) {

        entidades.forEach(entidadeAux -> {
            Inimigo inimigoAux = (Inimigo) entidadeAux;
            Estado estadoInimigo = inimigoAux.getEstado();

            if(estadoInimigo != Estado.MORTO && 
               estadoInimigo != Estado.FERIDO) {
                inimigoAux.move(this, playerX, 
                        playerY, deltaTime);
            }
                            
            inimigoAux.atualizaFrame();
        });
    }

    /**
     * Metodo responsavel por carregar um mapa na pasta de mapas a partir do
     * nome registrado
     *
     * @param nomeMapa nome do mapa a ser carregado
     * @return uma intancia do Mapa encontrado ou null caso não encrente mapa
     * com esse nome
     */
    public static Mapa carregaMapa(String nomeMapa) {
        File arquivoMapa = new File("maps" + File.separator + nomeMapa + ".txt");
        Scanner leitor;

        try {
            leitor = new Scanner(arquivoMapa);
        } catch (FileNotFoundException erro) {
            System.err.println("Erro! " + erro.getMessage());
            return null;
        }

        int limite = leitor.nextInt();
        Mapa aux = new Mapa(nomeMapa, limite);

        for (int i = 0, j; i < limite; i++) {
            for (j = 0; j < limite; j++) {
                aux.setValor(j, i, leitor.nextInt());
            }
        }

        aux.setSpawn(leitor.nextInt(), leitor.nextInt());
        //aux.criaEntidade();

        leitor.close();
        return aux;
    }

    /**
     * Metodo que re-carrega o mapa atual a partir da memoria
     */
    public void carregar() {
        File arquivoMapa = new File("maps" + File.separator + nomeMapa + ".txt");
        Scanner leitor;

        try {
            leitor = new Scanner(arquivoMapa);
        } catch (FileNotFoundException erro) {
            System.err.println("Erro! " + erro.getMessage());
            return;
        }

        limite = leitor.nextInt();
        grid = new int[limite][limite];

        for (int i = 0, j; i < limite; i++) {
            for (j = 0; j < limite; j++) {
                grid[j][i] = leitor.nextInt();
            }
        }
        
        this.setSpawn(leitor.nextInt(), leitor.nextInt());         
        this.criaEntidade();

        leitor.close();
    }

    /**
     * Método para salvar o mapa no arquivo
     *
     * @throws java.io.IOException Exception lançado por erro de entrada/saida
     */
    public void salvar() throws IOException {
        FileWriter out;
        String[] nomeMapaTXT = nomeMapa.split(".txt");
        File aux = new File("maps" + File.separator + nomeMapaTXT[0] + ".txt");
        if (!aux.exists()) {
            aux.createNewFile();
        }
        out = new FileWriter(aux);
        BufferedWriter buffout = new BufferedWriter(out);
        buffout.write(Integer.toString(limite));
        buffout.newLine();
        buffout.flush();
        for (int i = 0; i < this.limite; i++) {
            for (int j = 0; j < this.limite; j++) {
                buffout.write(Integer.toString(grid[j][i]));
                buffout.write(" ");
            }
            buffout.newLine();
            buffout.flush();
        }
        buffout.write(Integer.toString(playerSpawnX) + " " + Integer.toString(playerSpawnY));
        buffout.flush();
        buffout.close();
        out.close();
    }

    /**
     * Método que exclui o arquivo associado ao mapa atual
     */
    public void excluir() {
        String[] aux = nomeMapa.split(".txt");
        File arquivoMapa = new File("maps" + File.separator + aux[0] + ".txt");

        arquivoMapa.delete();

    }

    /**
     * Metodo que carrega todos os mapas salvos na memoria
     *
     * @return Um ArrayList com todos os mapas encontrados
     */
    public static ArrayList<Mapa> carregarMapList() {
        File arquivoMapa = new File("maps");
        Scanner leitor;
        ArrayList<Mapa> mapas = new ArrayList<>();

        File[] arquivoMapas = arquivoMapa.listFiles();
        for (File aux : arquivoMapas) {
            if (!aux.isDirectory()) {
                try {
                    leitor = new Scanner(aux);
                } catch (FileNotFoundException erro) {
                    System.err.println("Erro! " + erro.getMessage());
                    continue;
                }
                int tamanho = leitor.nextInt();
                Mapa mapaCarregado = new Mapa(aux.getName(), tamanho);

                mapaCarregado.carregar();
                mapas.add(mapaCarregado);
                leitor.close();
            }
        }

        return mapas;
    }

    /**
     * Método que reseta o mapa atual.
     */
    public void liberaMapa() {
        nomeMapa = "";
        grid = null;
        limite = 0;
        
        for(Entidade entidadeAux : entidades) {
            entidadeAux.close();
        }
        
        entidades.clear();
    }

    /**
     * Método que checa se o ponto atual esta na borda/parede do mapa ou não
     *
     * @param valorX Coordenada X
     * @param valorY Coordenada Y
     * @param id
     * @return true se o ponto esta na borda/parede do mapa ou false caso
     * contrario
     */
    public boolean checaColisao(double valorX, double valorY, int id) {
        valorX /= TAMANHO_BLOCO;
        valorY /= TAMANHO_BLOCO;

        return (valorX < 0 || valorY < 0 || valorX >= limite
                || valorY >= limite || (grid[(int) valorX][(int) valorY] != 0 && 
                grid[(int) valorX][(int) valorY] != id));
    }
    
    public boolean checaColisaoParede(double valorX, double valorY) {
        valorX /= TAMANHO_BLOCO;
        valorY /= TAMANHO_BLOCO;

        return (valorX < 0 || valorY < 0 || valorX >= limite
                || valorY >= limite || grid[(int) valorX][(int) valorY] > 0);
    }

    /**
     * Metodo que sobrscreve toString() para retornar uma string formatada
     * mostrado as informaçoes do mapa
     *
     * @return uma String Formatada mostando as informações do mapa
     */
    @Override
    public String toString() {
        return (getNomeMapa().replaceAll("\\.txt$", ""));
    }
}
