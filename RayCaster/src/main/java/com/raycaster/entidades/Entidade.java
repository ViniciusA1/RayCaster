package com.raycaster.entidades;

import com.raycaster.engine.Animacao;
import com.raycaster.engine.Estado;
import com.raycaster.engine.sons.EfeitoSonoro;
import com.raycaster.mapa.Mapa;
import com.raycaster.utils.Diretorio;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que contém os atributos e métodos de cada entidade do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public abstract class Entidade implements Cloneable {

    private String nome;
    private int id;
    private Hitbox hitbox;
    private double velocidade;
    private final double FOG;
    private double angulo;
    private double vidaMaxima;
    private double vidaAtual;
    private EnumSet<Estado> possiveisEstados;
    private Estado estadoAtual;
    private Map<Estado, Animacao> mapaAnimacao;
    private List<int[]> celulaOcupada;
    private EfeitoSonoro som;

    /**
     * Construtor da classe que recebe os atributos necessários para criação da
     * entidade.
     *
     * @param nome Nome da entidade
     * @param vidaMaxima Vida máxima da entidade
     * @param x Posição em x
     * @param y Posição em y
     * @param largura Largura da sua hitbox
     * @param velocidade Velocidade da entidade
     * @param FOG Campo de visão máximo da entidade
     * @param possiveisEstados Possiveis estados que a entidade pode assumir
     */
    public Entidade(String nome, double vidaMaxima, double x, double y, double largura,
            double velocidade, double FOG, EnumSet<Estado> possiveisEstados) {
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;

        this.FOG = FOG;
        this.angulo = 0;

        this.possiveisEstados = possiveisEstados;
        this.estadoAtual = Estado.OCIOSO;

        this.mapaAnimacao = new HashMap<>();
        this.hitbox = new Hitbox(x, y, largura);
        
        this.som = new EfeitoSonoro(nome, possiveisEstados);

        carregaAnimacoes(mapaAnimacao);
    }

    /**
     * Carrega todas as animações que possivelmente estão associadas a entidade.
     */
    private void carregaAnimacoes(Map<Estado, Animacao> mapa) {
        for (Estado estadoAux : possiveisEstados) {
            Animacao novaAnimacao = new Animacao(Diretorio.SPRITE_ENTIDADES + nome,
                    estadoAux);

            mapa.put(estadoAux, novaAnimacao);
        }
    }

    public String getNome() {
        return nome;
    }

    public int getID() {
        return id;
    }

    /**
     * Busca e retorna a posição no eixo x da entidade.
     *
     * @return Retorna a posição em x
     */
    public double getX() {
        return hitbox.getX();
    }

    /**
     * Busca e retorna a posição no eixo y da entidade.
     *
     * @return Retorna a posição em y
     */
    public double getY() {
        return hitbox.getY();
    }

    /**
     * Busca e retorna a largura total da hitbox da entidade.
     *
     * @return Retorna a sua largura
     */
    public double getLargura() {
        return hitbox.getLargura();
    }

    /**
     * Busca e retorna a velocidade da entidade.
     *
     * @return Retorna a sua velocidade
     */
    public double getVelocidade() {
        return velocidade;
    }

    /**
     * Busca e retorna o campo de visão máximo permitido na entidade.
     *
     * @return Retorna o seu campo de visão
     */
    public double getFOG() {
        return FOG;
    }

    public double getAngulo() {
        return angulo;
    }

    /**
     * Busca e retorna a vida máxima permitida na entidade.
     *
     * @return Retorna a sua vida máxima
     */
    public double getVidaMaxima() {
        return vidaMaxima;
    }

    /**
     * Busca e retorna a quantidade de vida atual da entidade.
     *
     * @return Retorna a sua vida atual
     */
    public double getVidaAtual() {
        return vidaAtual;
    }

    public BufferedImage getSpriteAtual() {
        return mapaAnimacao.get(estadoAtual).getSpriteImagem();
    }

    public int getSpriteWidth() {
        return mapaAnimacao.get(estadoAtual).getFrameWidth();
    }

    public int getSpriteHeight() {
        return mapaAnimacao.get(estadoAtual).getFrameHeight();
    }

    public Estado getEstado() {
        return estadoAtual;
    }

    public EnumSet<Estado> getEstados() {
        return possiveisEstados;
    }

    public void setEstado(Estado novoEstado) {

        if (novoEstado == estadoAtual) {
            return;
        }

        Animacao aux = mapaAnimacao.get(novoEstado);

        if (aux != null) {
            aux.setFrame(0);
        }

        aux = mapaAnimacao.get(estadoAtual);

        if (aux != null) {
            aux.setFrame(0);
        }

        this.estadoAtual = novoEstado;
    }

    public void setHitbox(Hitbox novaHitbox) {
        this.hitbox = novaHitbox;
    }

    public void setAnimacao(Map<Estado, Animacao> mapaAnimacao) {
        this.mapaAnimacao = mapaAnimacao;
    }

    public void setID(int novoID) {
        this.id = novoID;
    }

    public void setAngulo(double novoAngulo) {
        this.angulo = novoAngulo;
    }

    public final void novaCelula(Mapa mapaAtual) {
        this.celulaOcupada = new ArrayList<>();
        
        trataColisao(mapaAtual, 0, 0);
    }

    /**
     * Verifica se a entidade possui um determinado estado.
     *
     * @param estadoRecebido Estado que deve ser verificado
     * @return Retorna verdadeiro ou falso para a verificação
     */
    public boolean contemEstado(Estado estadoRecebido) {
        return possiveisEstados.contains(estadoRecebido);
    }

    /**
     * Verifica e trata a colisão da hitbox da entidade com os objetos do mapa
     *
     * @param mapaAtual Mapa em que a colisão ocorre
     * @param posX Posição em x da entidade
     * @param posY Posição em y da entidade
     */
    public void trataColisao(Mapa mapaAtual, double posX, double posY) {
        double tamanho = getLargura();
        double entidadeX = getX() + posX;
        double entidadeY = getY() + posY;

        int startX = (int) Math.floor(entidadeX - tamanho);
        int startY = (int) Math.floor(entidadeY - tamanho);
        int endX = (int) Math.ceil(entidadeX + tamanho);
        int endY = (int) Math.ceil(entidadeY + tamanho);

        int tamanhoBloco = mapaAtual.getTamanhoBloco();

        List<int[]> celulaAtual = new ArrayList<>();

        for (int x = startX, i = 0; x < endX; x += tamanho) {
            for (int y = startY; y < endY; y += tamanho, i++) {
                if (mapaAtual.checaColisao(x, y, id)) {
                    return;
                }
                
                int[] aux = {x / tamanhoBloco, y / tamanhoBloco};
                
                celulaAtual.add(aux);
            }
        }

        removeCelula(mapaAtual);

        for (int i = 0; i < celulaAtual.size(); i++) {
            int[] aux = celulaAtual.get(i);
            mapaAtual.setValor(aux[0], aux[1], id);
            celulaOcupada.add(aux);
        }

        setEstado(Estado.ANDANDO);

        moveX(entidadeX);
        moveY(entidadeY);
    }
    
    private void removeCelula(Mapa mapaAtual) {
        for (int i = 0; i < celulaOcupada.size(); i++) {
            int[] aux = celulaOcupada.get(i);
            
            mapaAtual.setValor(aux[0], aux[1], 0);
        }
        
        celulaOcupada.clear();
    }

    public void atualizaFrame() {
        Animacao animacao = mapaAnimacao.get(estadoAtual);

        if (estadoAtual == Estado.MORTO) {

            if (animacao.getFrameAtual()
                    != animacao.getQuantidadeFrames() - 1) {
                animacao.atualizaFrame();
            }
            
            return;
        }

        int frameAtual = animacao.atualizaFrame();

        if (frameAtual == 0 && estadoAtual != Estado.OCIOSO) {
            setEstado(Estado.OCIOSO);
        }
    }

    public void recebeDano(Mapa mapaAtual, double dano) {
        vidaAtual -= dano;
        
        som.playSom(Estado.FERIDO);

        if (vidaAtual > 0) {
            setEstado(Estado.FERIDO);
            return;
        }

        vidaAtual = 0;
        setEstado(Estado.MORTO);
        removeCelula(mapaAtual);
    }
    
    public void playSom(Estado estadoSom) {
        som.playSom(estadoSom);
    }
    
    public boolean isSomRunning(Estado estadoSom) {
        return som.isRunning(estadoSom);
    }
    
    public void close() {
        som.close();
    }

    /**
     * Move a entidade para uma determinada posição no eixo x.
     *
     * @param deltaX Nova posição em x
     */
    public void moveX(double deltaX) {
        hitbox.setX(deltaX);
    }

    /**
     * Move a entidade para uma determinada posição no eixo y.
     *
     * @param deltaY Nova posição em y
     */
    public void moveY(double deltaY) {
        hitbox.setY(deltaY);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Entidade clone = (Entidade) super.clone();

        clone.setHitbox(new Hitbox(0, 0, clone.getLargura()));
        Map<Estado, Animacao> mapa = new HashMap<>();
        carregaAnimacoes(mapa);
        clone.setAnimacao(mapa);

        return clone;
    }
}
