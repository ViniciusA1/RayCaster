package com.raycaster.engine;

import com.raycaster.interfaces.MenuPause;
import com.raycaster.interfaces.LayoutEngine;
import com.raycaster.interfaces.AnimacaoPlayer;
import com.raycaster.interfaces.HUD;
import com.raycaster.entidades.Player;
import com.raycaster.interfaces.InterfaceManager;
import com.raycaster.interfaces.Painel;
import com.raycaster.interfaces.PainelInformacao;
import com.raycaster.interfaces.PainelMira;
import com.raycaster.itens.Arma;
import com.raycaster.mapa.Mapa;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Classe que guarda todos os atributos e métodos do motor gŕafico do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Engine extends Painel implements ActionListener {

    private int screenWidth;
    private int screenHeight;

    private final int fpsMaximo;
    private Timer gameTimer;
    private long tempoAnterior;
    private double deltaTime;
    private long tempoFrame;
    private int frameCounter;

    private PainelInformacao painelInfo;

    private Player jogador;
    private Mapa mapaAtual;
    
    private KeyInput keyHandler;
    private MouseInput mouseHandler;
    
    private List<Textura> texturas;
    private EfeitosSonoros musicaBackground;
    private final JFrame janela;

    private Font fontePersonalizada;

    /**
     * Construtor da engine, recebe o tamanho horizontal e vertical da JFrame
     *
     * @param width Comprimento horizontal da tela
     * @param height Comprimento vertical da tela
     * @param janela a janela a que esse componente esta associado
     * @param map Mapa que será jogado
     */
    public Engine(int width, int height, JFrame janela, Mapa map) {
        this.fpsMaximo = 60;
        this.screenWidth = width;
        this.screenHeight = height;
        this.janela = janela;
        this.mapaAtual = map;
        this.texturas = Textura.carregaTexturas(new File(Diretorio.TEXTURA_PAREDE));

        setLayout(new LayoutEngine());

        start();
    }

    /**
     * Seta uma nova resolução para a tela do jogo.
     * @param screenWidth Nova largura da tela
     * @param screenHeight Nova altura da tela
     */
    public void setResolution(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Aplica e inicia todas as configurações principais da engine.
     */
    private void start() {
        initMapa();
        initPlayer();
        initArmas();
        initPanels();
        initHandlers();
        initMusica();
        initCursor();
        initTimer();
    }

    /**
     * Carrega e inicia o mapa inicial que será jogado.
     */
    private void initMapa() {
//        mapaAtual = new Mapa("lobby.txt", 20);
        mapaAtual.carregar();
    }

    /**
     * Carrega e inicia todos os players associados ao jogo.
     */
    private void initPlayer() {
        List<Player> jogadores = ArquivoUtils.leObjetos(Diretorio.DADOS_ENTIDADES,
                Player.class);

        jogador = jogadores.get(0);

        jogador.moveX(mapaAtual.getBlockSpawnX());
        jogador.moveY(mapaAtual.getBlockSpawnY());
    }

    /**
     * Carrega e inicia todas as armas possuídas pelo player.
     */
    private void initArmas() {
        List<Arma> armas = ArquivoUtils.leObjetos(Diretorio.DADOS_ITENS,
                Arma.class);

        jogador.adicionaArma(armas);
    }

    /**
     * Inicia todos os painéis do jogo que dependem da engine.
     */
    private void initPanels() {
        carregaFonte();

        HUD hudJogador = new HUD(jogador, fontePersonalizada);
        AnimacaoPlayer painelAnimacao = new AnimacaoPlayer(jogador);
        PainelMira painelMira = new PainelMira();
        painelInfo = new PainelInformacao(fontePersonalizada);

        this.add(hudJogador, "hud");
        this.add(painelMira, "mira");
        this.add(painelAnimacao, "animacao");
        this.add(painelInfo, "info");

        jogador.setPainelAnimacao(painelAnimacao);
        jogador.setHUD(hudJogador);
        jogador.sacaItem(0);
    }

    /**
     * Inicia os "handlers" de teclado e mouse do jogo.
     */
    private void initHandlers() {
        mouseHandler = new MouseInput(jogador, 0.002);
        keyHandler = new KeyInput();
        keyBindings();

        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        setFocusable(true);
        requestFocus();
    }

    /**
     * Inclui todas as keybindings no controlador de evento das teclas.
     */
    private void keyBindings() {
        keyHandler.adicionaKey(KeyEvent.VK_W, ()
                -> jogador.move(0, 1, mapaAtual, deltaTime));

        keyHandler.adicionaKey(KeyEvent.VK_A, ()
                -> jogador.move(-Math.PI / 2, 1, mapaAtual, deltaTime));

        keyHandler.adicionaKey(KeyEvent.VK_S, ()
                -> jogador.move(0, -1, mapaAtual, deltaTime));

        keyHandler.adicionaKey(KeyEvent.VK_D, ()
                -> jogador.move(Math.PI / 2, 1, mapaAtual, deltaTime));

        keyHandler.adicionaKey(KeyEvent.VK_R, ()
                -> jogador.recarregaItem());

        keyHandler.adicionaKey(KeyEvent.VK_ESCAPE, ()
                -> this.pausaJogo());

        keyHandler.adicionaKey(KeyEvent.VK_1, ()
                -> jogador.sacaItem(0));

        keyHandler.adicionaKey(KeyEvent.VK_2, ()
                -> jogador.sacaItem(1));

    }

    /**
     * Carrega a música principal do jogo que ficará em loop durante a execução.
     */
    private void initMusica() {
        musicaBackground = new EfeitosSonoros("mapa" + File.separator + 
                mapaAtual.toString(), Estado.OCIOSO);
        
        musicaBackground.emiteSom(Estado.OCIOSO);
        musicaBackground.setLoop(Estado.OCIOSO);

        janela.addWindowListener(new DesligaSom(musicaBackground));
    }
    
    /**
     * Inicializa a imagem do cursor padrão.
     */
    private void initCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        Image imagemCursor = toolkit.createImage("");
        Point pontoCursor = new Point(0,0);
        
        Cursor cursorTransparente = toolkit.createCustomCursor(
                imagemCursor, pontoCursor, "transparente");
        
        this.setCursor(cursorTransparente);
    }

    /**
     * Inicia o timer do jogo para controlar o output de frames.
     */
    private void initTimer() {
        gameTimer = new Timer(Math.round(1000f / fpsMaximo), this);
        gameTimer.start();
    }

    /**
     * Carrega a fonte personalizada que será aplicada nos componentes.
     */
    private void carregaFonte() {
        fontePersonalizada = null;

        try {
            File fontFile = new File(Diretorio.SPRITE_HUD + "font.ttf");
            fontePersonalizada = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (IOException | FontFormatException e) {
            System.err.println("Fonte não existe!");
        }
    }

    /**
     * Renderiza todos os componentes associados a tela (JPanel) principal do
     * jogo, incluindo o cálculo e uso efetivo do raycasting.
     *
     * @param g Componente gráfico utilizado pela JPanel para renderizar a tela
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Coleta alguns dados do player para evitar overhead de métodos no loop
        double playerAngulo = jogador.getAngulo();
        double playerX = jogador.getX();
        double playerY = jogador.getY();
        double playerFOV = jogador.getFOV();

        // Distancia de visão máxima permitida pelo jogo
        double distanciaMaxima = jogador.getFOG();

        // Guarda o tamanho dos blocos do mapa
        int tamanhoBloco = mapaAtual.getTamanhoBloco();

        // Fator que representa qual lado da parede foi atingido (vertical ou horizontal)
        int eixo;

        // Protótipo de "framebuffer" utilizado para acelerar a renderização
        int[] frameBuffer = new int[screenHeight * screenWidth];

        // Classe filha de Graphics que inclui métodos adicionais para gráficos 2D
        Graphics2D render2D = (Graphics2D) g;

        // Fator de projeção utilizado para adequar as paredes ao monitor
        double fatorProjecao = screenWidth / (2 * Math.tan(playerFOV / 2.0));

        // Tamanho das texturas para renderização
        int tamanhoTextura = texturas.get(0).getTamanho();

        // Renderiza o teto e o chão do mapa
        renderizaExtremidades(frameBuffer, playerX, playerY, playerAngulo, playerFOV,
                fatorProjecao, tamanhoTextura, tamanhoBloco);

        // Cálculo e renderização de todas as colunas de pixel da tela
        for (int i = 0; i < screenWidth; i++) {
            double anguloRaio = Math.atan2(i - screenWidth / 2, fatorProjecao)
                    + playerAngulo;

            int posX, posY;
            int direcaoX, direcaoY;
            double deltaX, deltaY;
            double distanciaX, distanciaY;
            double cosRaio = Math.cos(anguloRaio);
            double sinRaio = Math.sin(anguloRaio);

            // Posição inicial do ponto extremo do raio
            posX = (int) playerX;
            posY = (int) playerY;

            // Determina o quanto é possivel andar em cada unidade do mapa
            deltaX = (cosRaio == 0) ? Double.MAX_VALUE : Math.abs(1 / cosRaio);
            deltaY = (sinRaio == 0) ? Double.MAX_VALUE : Math.abs(1 / sinRaio);

            // Verifica a orientação em X do raio
            if (cosRaio < 0) {
                direcaoX = -1;
                distanciaX = (playerX - posX) * deltaX;
            } else {
                direcaoX = 1;
                distanciaX = (posX + 1.0 - playerX) * deltaX;
            }

            // Verifica a orientação em Y do raio
            if (sinRaio < 0) {
                direcaoY = -1;
                distanciaY = (playerY - posY) * deltaY;
            } else {
                direcaoY = 1;
                distanciaY = (posY + 1.0 - playerY) * deltaY;
            }

            // Percorre o mapa até encontrar o primeiro obstáculo
            while (true) {

                // Verifica qual componente do raio é menor e aumenta-a
                if (distanciaX < distanciaY) {
                    distanciaX += deltaX;
                    posX += direcaoX;
                    eixo = 0;
                } else {
                    distanciaY += deltaY;
                    posY += direcaoY;
                    eixo = 1;
                }

                // Checa a colisão do raio com a parede
                if (mapaAtual.checaColisao(posX, posY)) {
                    break;
                }
            }

            // Calcula o valor real da distancia percorrida pelo raio que atingiu a parede primeiro
            // e sua posição na coordenada x
            double paredeX;
            double distanciaFinal;

            if (eixo == 0) {
                distanciaFinal = distanciaX - deltaX;
                paredeX = playerY + distanciaFinal * sinRaio;
            } else {
                distanciaFinal = distanciaY - deltaY;
                paredeX = playerX + distanciaFinal * cosRaio;
            }

            paredeX /= tamanhoBloco;
            paredeX -= Math.floor(paredeX);

            // Fator de correção para o efeito fisheye (olho de peixe)
            distanciaFinal *= Math.cos(anguloRaio - playerAngulo);

            // Determina o fator da "neblina" que deve ser aplicado pela distancia encontrada
            if (distanciaFinal > distanciaMaxima) {
                distanciaFinal = distanciaMaxima;
            }

            double fatorFOG = 1.0 - (distanciaFinal / distanciaMaxima);

            // Calcula os valores da altura da parede, seu começo e fim no eixo y
            int alturaParede = (int) ((tamanhoBloco / distanciaFinal) * fatorProjecao);
            int comecoParede = (int) (screenHeight - alturaParede) / 2;

            // Verifica se o indice da parede sai do limite da tela
            if (comecoParede < 0) {
                comecoParede = 0;
            }

            int fimParede = alturaParede + comecoParede;

            // Verifica se o indice da parede sai do limite da tela
            if (fimParede >= screenHeight) {
                fimParede = screenHeight - 1;
            }

            // Busca o id da textura com base no seu valor no mapa
            int idTextura = mapaAtual.getValor(posX, posY) - 1;
            idTextura = Math.max(idTextura, 0);

            // Valor real da coluna de textura que deve ser percorrida
            int texturaX = (int) (paredeX * tamanhoTextura);

            // Determina a posição final da textura em X com base em seu comprimento real
            if ((eixo == 0 && cosRaio > 0) || (eixo == 1 && sinRaio < 0)) {
                texturaX = tamanhoTextura - texturaX - 1;
            }

            // Calcula o fator de escala que deve ser usado para andar na imagem da textura
            double variacao = 1.0 * tamanhoTextura / alturaParede;
            double posicaoTextura = (comecoParede - screenHeight / 2 + alturaParede / 2) * variacao;

            // Percorre todo os pixels da coluna atribuindo a cor da textura a eles
            for (int y = comecoParede; y < fimParede; y++) {
                int texturaY = (int) posicaoTextura & (tamanhoTextura - 1);
                
                posicaoTextura += variacao;
                
                int corPixel = texturas.get(idTextura).getPixel(tamanhoTextura * texturaY + texturaX);

                frameBuffer[y * screenWidth + i] = transformaCor(corPixel, fatorFOG);
            }
        }

        // Usa as cores RGB acumuladas no frameBuffer para criar uma imagem e renderiza-la
        BufferedImage frame = new BufferedImage(screenWidth, screenHeight,
                BufferedImage.TYPE_INT_RGB);

        // "Seta" os valores das cores do frame
        frame.setRGB(0, 0, screenWidth, screenHeight, frameBuffer,
                0, screenWidth);

        // Renderiza o frame todo
        render2D.drawImage(frame, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    /**
     * Renderiza as extremidades (teto e chão) do mapa atual.
     *
     * @param frameBuffer Array de números inteiros que guarda os pixels do
     * frame
     * @param playerX Posição em x do jogador
     * @param playerY Posição em y do jogador
     * @param playerAngulo Angulo do jogador
     * @param playerFOV Campo de visão do jogador
     * @param tamanhoTextura Tamanho inteiro da textura
     * @param tamanhoBloco Tamanho de cada bloco do mapa
     */
    private void renderizaExtremidades(int[] frameBuffer, double playerX, double playerY,
            double playerAngulo, double playerFOV, double fatorProjecao,
            int tamanhoTextura, int tamanhoBloco) {

        // Posições do raio mínimo (mais a esquerda) e do raio máximo (mais a direita).
        // Ambos representam os limites mínimos e máximos do teto/chão a ser renderizado
        double angle = Math.atan2(-screenWidth / 2, fatorProjecao)
                + playerAngulo;

        double cosRaioMinimo = Math.cos(angle);
        double sinRaioMinimo = Math.sin(angle);
        double cosRaioMaximo = Math.cos(angle + playerFOV);
        double sinRaioMaximo = Math.sin(angle + playerFOV);

        double playerFOG = playerFOV * jogador.getFOG() / 50;

        // Posição central da visão do jogador (meio da tela)
        double centroDeVisao = screenHeight / 2;

        // Percorre todas linhas necessárias da tela para renderizar a imagem
        for (int y = 0; y < screenHeight; y++) {

            // Posição relativa da linha sendo desenhada e da posição
            // central da tela.
            double posicaoRelativa = y - centroDeVisao;

            // Distancia horizontal da linha baseado no centro de visão e
            // na posição relativa da linha em relação ao centro
            double distanciaLinha = centroDeVisao / posicaoRelativa;

            // Calcula os incrementos necessários para a posição real do
            // teto/chão em x e y
            double incrementoX = distanciaLinha * (cosRaioMaximo - cosRaioMinimo) / screenWidth;
            double incrementoY = distanciaLinha * (sinRaioMaximo - sinRaioMinimo) / screenWidth;

            // Posições nas coordenadas x e y da posição atual no teto e chão
            double posX = (playerX / (tamanhoBloco * (1.5 / playerFOV))) + distanciaLinha * cosRaioMinimo;
            double posY = (playerY / (tamanhoBloco * (1.5 / playerFOV))) + distanciaLinha * sinRaioMinimo;

            double distanciaFOG = Math.abs(distanciaLinha);

            if (distanciaFOG > playerFOG) {
                distanciaFOG = playerFOG;
            }

            double fatorFOG = 1 - distanciaFOG / playerFOG;

            // Percorre, em cada linha, os pixels da coluna para aplicar
            // a cor da textura adequada
            for (int x = 0; x < screenWidth; x++) {

                // Posições das coordenadas reais convertidas em posições
                // dentro da própria imagem da textura
                int texturaX = (int) (tamanhoTextura * (posX - (int) posX))
                        & (tamanhoTextura - 1);

                int texturaY = (int) (tamanhoTextura * (posY - (int) posY))
                        & (tamanhoTextura - 1);

                // Incrementa as posições para desenhar o próximo pixel
                posX += incrementoX;
                posY += incrementoY;

                // Índices das texturas para teto e chão
                int chaoID = 1;
                int tetoID = 2;
                int corPixel;

                // Aplica as cores das texturas em ambas as figuras
                corPixel = texturas.get(chaoID).getPixel(tamanhoTextura * texturaY + texturaX);
                frameBuffer[y * screenWidth + x] = transformaCor(corPixel, fatorFOG);

                corPixel = texturas.get(tetoID).getPixel(tamanhoTextura * texturaY + texturaX);
                frameBuffer[(screenHeight - y - 1) * screenWidth + x] = transformaCor(corPixel, fatorFOG);
            }
        }
    }

    /**
     * Transforma as componentes de uma cor aplicando o efeito de FOG
     *
     * @param cor Cor atual do pixel
     * @param fator Fator para aplicação do FOG
     * @return Retorna a cor com o efeito aplicado
     */
    private int transformaCor(int cor, double fator) {
        int red = (int) (((cor >> 16) & 255) * fator);
        int green = (int) (((cor >> 8) & 255) * fator);
        int blue = (int) ((cor & 255) * fator);

        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Executa as ações do frame atual (determinado pelo timer).
     *
     * @param e Evento ocorrido
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    /**
     * Atualiza os eventos do jogo associados ao player
     */
    private void update() {
        long tempoAtual = System.currentTimeMillis();

        double deltaFrame = (tempoAtual - tempoFrame) / 1000.0;
        deltaTime = (tempoAtual - tempoAnterior) / 1000.0;
        tempoAnterior = tempoAtual;

        frameCounter++;

        if (deltaFrame >= 1.0) {
            painelInfo.setFPS(frameCounter);
            frameCounter = 0;
            tempoFrame = tempoAtual;
        }
        
        double prevX = jogador.getX();
        double prevY = jogador.getY();

        keyHandler.executaMetodo();
        
        double fator = Math.abs((Math.abs(jogador.getX() - prevX) + 
                                 Math.abs(jogador.getY() - prevY)) / 2) * 8;
        
        jogador.setPitch(fator * deltaTime, (getHeight() + getWidth()) / 50);
    }

    /**
     * Pausa o jogo quando o usuário desejar.
     */
    private void pausaJogo() {
        if (!gameTimer.isRunning()) {
            return;
        }

        gameTimer.stop();

        BufferedImage imagemBackground = new BufferedImage(this.getWidth(),
                this.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D render = imagemBackground.createGraphics();
        this.paint(render);

        render.dispose();

        janela.remove(this);

        MenuPause menu = new MenuPause(janela, fontePersonalizada, imagemBackground);
        menu.setBackground(Color.DARK_GRAY);
        InterfaceManager.push(menu);

        janela.add(menu);
        janela.revalidate();
        janela.repaint();
        
        menu.requestFocus();
    }
    
    @Override
    public void entrar() {
        super.entrar();
        
        janela.repaint();
        janela.revalidate();
        
        if(!gameTimer.isRunning()) {
            gameTimer.start();
        }
        
        keyHandler.limpaMetodos();
        
        SwingUtilities.invokeLater(() -> {
            this.requestFocusInWindow();
        });
    }
    
    @Override
    public void sair() {
       super.sair();
       
       janela.repaint();
       janela.revalidate();
       
       if(gameTimer.isRunning())
            musicaBackground.close();
       
       gameTimer.stop();
      
    }

    /**
     * Fecha as janelas e paineis dependentes da engine.
     */
    public void fechaJogo() {
        janela.remove(InterfaceManager.peek());
        InterfaceManager.pop();
        InterfaceManager.pop();
        janela.add(InterfaceManager.peek());
        janela.repaint();
        janela.revalidate();
    }

    /**
     * Classe que ouve uma série de eventos da janela para encerrar o som do
     * jogo.
     */
    private static class DesligaSom extends WindowAdapter {

        EfeitosSonoros som;

        public DesligaSom(EfeitosSonoros som) {
            this.som = som;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            if (som != null) {
                som.emiteSom(Estado.OCIOSO);
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
            if (som == null) {
                System.exit(0);
            }
            som.stopLoop(Estado.OCIOSO);
        }
    }
}
