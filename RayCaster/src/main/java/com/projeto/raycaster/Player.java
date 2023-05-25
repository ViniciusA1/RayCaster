package com.projeto.raycaster;



/**
 *
 * @author vinic
 */
public class Player extends Entidade {
    private double angulo;
    private double pitch;
    private double taxaPitch;
    private long tempoAnterior;
    private Inventario<Item> mochila;
    private Item itemAtual;
    private AnimacaoPlayer painelAnimacao;
    private HUD hudJogador;
    private Estado estadoAtual;
    private EfeitosSonoros som;
    
    public Player(double vidaMaxima, double x, double y, double velocidade, int fov) {
        super(vidaMaxima, x, y, velocidade, fov);
        angulo = 0;
        pitch = 0;
        taxaPitch = 0.5;
        mochila = new Inventario<>();
        estadoAtual = Estado.OCIOSO;
        som = new EfeitosSonoros("player");
    }
    
    public void setPainelAnimacao(AnimacaoPlayer novoPainel) {
        painelAnimacao = novoPainel;
    }
    
    public void setHUD(HUD hudJogador) {
        this.hudJogador = hudJogador;
    }
    
    public void setEstado(Estado novoEstado) {
        estadoAtual = novoEstado;
    }
    
    public double getAngulo() {
        return angulo;
    }
    
    public double getPitch() {
        return pitch;
    }
    
    public Item getItemAtual() {
        return itemAtual;
    }
    
    public void move(double anguloRelativo, int sinal, Mapa mapaAtual) {
        double dx = sinal * super.getVelocidade() * Math.cos(angulo + anguloRelativo);
        double dy = sinal * super.getVelocidade() * Math.sin(angulo + anguloRelativo);
        
        pitch += taxaPitch;
        
        if(pitch >= 16 || pitch <= -16) {
            taxaPitch *= -1;
        }
        
        if(!checaColisao(mapaAtual, dx + super.getX(), dy + super.getY()))
            setXY(dx, dy);
    }
    
    public void rotaciona(double deltaX) {
        angulo = angulo + deltaX;
    }
    
    public boolean checaColisao(Mapa mapaAtual, double posX, double posY) {
        return (mapaAtual.checaColisao((int) posX, (int) posY));
    }
    
    public void adicionaItem(Item novoItem) {
        mochila.guardaObjeto(novoItem);
    }

    public void sacaItem(int index) {
        if(estadoAtual != Estado.OCIOSO)
            return;
        
        itemAtual = mochila.getObjeto(index);
        hudJogador.atualizaItem();
        painelAnimacao.setAnimacao(itemAtual.getAnimacao(estadoAtual));
        
        /*if(!clip.isRunning()) {
            clip.close();
            clip = null;
        }*/
    }
    
    public void usaItem(int posX, int posY) {
        long tempoAtual = System.currentTimeMillis();
        
        if(estadoAtual != Estado.OCIOSO || 
           tempoAtual - tempoAnterior <= itemAtual.getCooldown() || 
           !itemAtual.isUsavel())
            return;
        
        itemAtual.usar();
        estadoAtual = Estado.ATIRANDO;
        
        painelAnimacao.setAnimacao(itemAtual.getAnimacao(estadoAtual));
        itemAtual.reproduzSom(Estado.ATIRANDO);
        
        tempoAnterior = tempoAtual;
    }
    
    public void recarregaItem() {
        if(estadoAtual != Estado.OCIOSO || !itemAtual.isRecarregavel())
            return;
        
        ArmaLonga arma = (ArmaLonga) itemAtual;
        
        arma.recarregar();
        estadoAtual = Estado.RECARREGANDO;
        painelAnimacao.setAnimacao(arma.getAnimacao(estadoAtual));
        
        itemAtual.reproduzSom(estadoAtual);
    }
    
    public int getQtdConsumivel() {
        if(itemAtual == null) 
            return -1;
        
        return itemAtual.getAtributoConsumivel();
    }
    
    public void desenhaComponentes() {
        hudJogador.repaint();
        painelAnimacao.repaint();
    }
}
