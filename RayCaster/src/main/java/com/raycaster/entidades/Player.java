package com.raycaster.entidades;

import com.raycaster.interfaces.AnimacaoPlayer;
import com.raycaster.itens.ArmaLonga;
import com.raycaster.engine.EfeitosSonoros;
import com.raycaster.engine.Estado;
import com.raycaster.interfaces.HUD;
import com.raycaster.itens.Item;
import com.raycaster.mapa.Mapa;
import com.raycaster.itens.Arma;
import java.util.EnumSet;
import java.util.List;

/**
 * Classe que guarda todos os atributos e métodos do jogador principal do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Player extends Entidade {

    private double angulo;
    private double pitch;
    private double FOV;
    private double pitchAcumulado;
    private long tempoAnterior;
    private Inventario<Arma> mochila;
    private Item itemAtual;
    private AnimacaoPlayer painelAnimacao;
    private HUD hudJogador;
    private Estado estadoAtual;
    private EfeitosSonoros som;

    /**
     * Construtor principal do jogador, recebe os atributos necessitados pela
     * classe.
     *
     * @param vidaMaxima Vida máxima do jogador
     * @param x Posição inicial em x do jogador
     * @param y Posição inicial em y do jogador
     * @param largura Largura da hitbox do jogador
     * @param velocidade Velocidade do jogador
     * @param fov Campo de visão do jogador
     * @param FOG Distância máxima de visão do jogador
     * @param possiveisEstados Possíveis estados assumidos pelo jogador
     */
    public Player(double vidaMaxima, double x, double y, double largura, double velocidade,
            int fov, double FOG, EnumSet<Estado> possiveisEstados) {

        super(vidaMaxima, x, y, largura, velocidade, FOG, possiveisEstados);

        angulo = 0;
        pitch = 0;
        pitchAcumulado = 0;
        FOV = Math.toRadians(fov);
        mochila = new Inventario<>();
        estadoAtual = Estado.OCIOSO;
        som = new EfeitosSonoros("player", possiveisEstados);
    }

    /**
     * "Seta" um novo painel de animação para os itens do jogador.
     *
     * @param novoPainel Novo painel de animação recebido
     */
    public void setPainelAnimacao(AnimacaoPlayer novoPainel) {
        painelAnimacao = novoPainel;
    }

    /**
     * "Seta" uma nova HUD para o jogador.
     *
     * @param hudJogador Nova HUD recebida
     */
    public void setHUD(HUD hudJogador) {
        this.hudJogador = hudJogador;
    }

    /**
     * "Seta" um novo estado para o jogador.
     *
     * @param novoEstado Novo estado recebido
     */
    public void setEstado(Estado novoEstado) {
        estadoAtual = novoEstado;
    }

    /**
     * Disponibiliza o angulo do jogador para código externo.
     *
     * @return Retorna o angulo de rotação do jogador.
     */
    public double getAngulo() {
        return angulo;
    }

    /**
     * Disponibiliza o FOV do jogador para código externo.
     *
     * @return Retorna o FOV do jogador
     */
    public double getFOV() {
        return FOV;
    }

    /**
     * Disponibiliza o "pitch" do jogador para código externo.
     *
     * @return Retorna o valor do "pitch" do jogador
     */
    public double getPitch() {
        return pitch;
    }
    
    public void setPitch(double fator, double tamanhoTela) {
        pitchAcumulado += fator;
        pitch = tamanhoTela * Math.sin(pitchAcumulado);
    }

    /**
     * Disponibiliza o item atual do jogador para código externo.
     *
     * @return Retorna o item atualmente selecionado pelo jogador.
     */
    public Item getItemAtual() {
        return itemAtual;
    }

    /**
     * Move o jogador no mapa atual de acordo com seu angulo e posição anterior.
     *
     * @param anguloRelativo Angulo relativo ao movimento desejado (W,A,S,D)
     * @param sinal Sinal relativo ao movimento desejado (frente ou trás)
     * @param mapaAtual Mapa atual em que o jogador será movido
     * @param deltaTime Fator de diferença entre o tempo de cada frame
     */
    public void move(double anguloRelativo, int sinal, Mapa mapaAtual, double deltaTime) {
        double dx = sinal * getVelocidade()
                * Math.cos(angulo + anguloRelativo) * deltaTime;

        double dy = sinal * getVelocidade()
                * Math.sin(angulo + anguloRelativo) * deltaTime;

        trataColisao(mapaAtual, dx, 0);
        trataColisao(mapaAtual, 0, dy);
    }

    /**
     * Rotaciona o player em torno do seu angulo.
     *
     * @param deltaX Variação do angulo que deve ser somada
     */
    public void rotaciona(double deltaX) {
        angulo += deltaX;
    }

    /**
     * Adiciona uma nova arma no inventário do jogador.
     *
     * @param novaArma Nova arma a ser adicionada
     */
    public void adicionaArma(Arma novaArma) {
        mochila.guardaObjeto(novaArma);
    }

    /**
     * Adiciona uma lista de novas armas ao inventário do jogador.
     *
     * @param novasArmas Novas armas para serem adicionadas
     */
    public void adicionaArma(List<Arma> novasArmas) {
        mochila.guardaObjeto(novasArmas);
    }

    /**
     * Saca um item do inventário do jogador.
     *
     * @param index Índice do novo item
     */
    public void sacaItem(int index) {
        if (estadoAtual != Estado.OCIOSO) {
            return;
        }

        Item novoItem = mochila.getObjeto(index);
        if (novoItem == itemAtual) {
            return;
        }

        itemAtual = novoItem;
        hudJogador.atualizaItem();
        painelAnimacao.setAnimacao(itemAtual.getAnimacao(estadoAtual));

        estadoAtual = Estado.SACANDO;
        som.emiteSom(estadoAtual);
    }

    /**
     * Executa a ação de usar o item atual do jogador.
     *
     * @param posX Posição x em que o item foi utilizado
     * @param posY Posição y em que o item foi utilizado
     */
    public void usaItem(int posX, int posY) {
        long tempoAtual = System.currentTimeMillis();

        if (estadoAtual != Estado.OCIOSO
                || tempoAtual - tempoAnterior <= itemAtual.getCooldown()
                || !itemAtual.isUsavel()) {
            return;
        }

        itemAtual.usar();
        estadoAtual = Estado.USANDO;

        painelAnimacao.setAnimacao(itemAtual.getAnimacao(estadoAtual));
        itemAtual.reproduzSom(estadoAtual);

        tempoAnterior = tempoAtual;
    }

    /**
     * Recarrega, se possível, o item atual do jogador.
     */
    public void recarregaItem() {
        if (estadoAtual != Estado.OCIOSO || !itemAtual.isRecarregavel()) {
            return;
        }

        ArmaLonga arma = (ArmaLonga) itemAtual;

        arma.recarregar();
        estadoAtual = Estado.RECARREGANDO;
        painelAnimacao.setAnimacao(arma.getAnimacao(estadoAtual));

        itemAtual.reproduzSom(estadoAtual);
    }

    /**
     * Disponibiliza o número do consumível do item para código externo.
     *
     * @return Retorna a quantidade do atributo consumível do item atual do
     * jogador
     */
    public int getQtdConsumivel() {
        if (itemAtual == null) {
            return 0;
        }

        return itemAtual.getAtributoConsumivel();
    }
    
    public int getConsumivelMax() {
        if(itemAtual == null) {
            return 0;
        }
        
        return itemAtual.getConsumivelMax();
    }
}
