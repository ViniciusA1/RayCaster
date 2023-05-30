package com.raycaster.engine;

import com.raycaster.interfaces.AnimacaoPlayer;
import com.raycaster.interfaces.HUD;
import com.raycaster.entidades.Player;
import com.raycaster.itens.Arma;
import com.raycaster.mapa.Mapa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Classe que guarda todos os atributos e métodos do motor gŕafico do jogo.
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class Engine extends JPanel implements ActionListener {

    private final int SCREENWIDTH;
    private final int SCREENHEIGHT;
    private final Timer gameTimer;
    private Player jogador;
    private Mapa mapaAtual;
    private KeyInput keyHandler;
    private MouseInput mouseHandler;
    private int[][] textura;
    private Clip musicaBackground;
    private JFrame janela;

    /**
     * Construtor da engine, recebe o tamanho horizontal e vertical da JFrame
     *
     * @param width Comprimento horizontal da tela
     * @param height Comprimento vertical da tela
     * @param janela a janela a que esse componente esta associado
     */
    public Engine(int width, int height, JFrame janela) {
        this.SCREENWIDTH = width;
        this.SCREENHEIGHT = height;
        setLayout(null);

        configInicial();

        int intervalo = 8;
        gameTimer = new Timer(intervalo, this);
        gameTimer.start();

        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        setFocusable(true);

        carregaMusicaPrincipal();
        
        this.janela = janela;
        janela.addWindowListener(new desligaSom(musicaBackground));
    }

    /**
     * Aplica as configurações iniciais ao jogo, incluindo configurações do
     * player, armas e keybindings.
     */
    private void configInicial() {
        //jogador = new Player(100, 400, 300, 16, 2, 60, 500);
        jogador = ArquivoUtils.criaObjeto("dados/player/player", Player.class);
        mapaAtual = new Mapa("lobby.txt", 20);
        mapaAtual.carregar();

        mouseHandler = new MouseInput(jogador, 0.001);

        List<Arma> armas = ArquivoUtils.leItens(Diretorio.DADOS_ITENS, Arma.class);

        jogador.adicionaArma(armas);

        HUD hudJogador = new HUD(jogador);
        hudJogador.setBounds(0, SCREENHEIGHT - 100, SCREENWIDTH, 100);
        this.add(hudJogador);

        AnimacaoPlayer painelAnimacao = new AnimacaoPlayer(jogador);
        painelAnimacao.setBounds(SCREENWIDTH / 2, SCREENHEIGHT / 2, SCREENWIDTH / 2, SCREENHEIGHT / 2);
        this.add(painelAnimacao);

        jogador.setPainelAnimacao(painelAnimacao);
        jogador.setHUD(hudJogador);

        jogador.sacaItem(0);

        keyHandler = new KeyInput();
        keyBindings();

        carregaTexturas();
    }

    /**
     * Inclui todas as keybindings no controlador de evento das teclas.
     */
    private void keyBindings() {
        keyHandler.adicionaKey(KeyEvent.VK_W, () -> jogador.move(0, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_A, () -> jogador.move(-Math.PI / 2, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_S, () -> jogador.move(0, -1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_D, () -> jogador.move(Math.PI / 2, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_R, () -> jogador.recarregaItem());
        keyHandler.adicionaKey(KeyEvent.VK_ESCAPE, () -> this.fechaJogo());

        keyHandler.adicionaKey(KeyEvent.VK_1, () -> jogador.sacaItem(0));
        keyHandler.adicionaKey(KeyEvent.VK_2, () -> jogador.sacaItem(1));
    }

    /**
     * Carrega todas as texturas das paredes do jogo armazenadas no grid.
     */
    private void carregaTexturas() {
        textura = new int[1][128 * 128];
        try {
            BufferedImage imagem = ImageIO.read(new File(Diretorio.TEXTURA_PAREDE + "03 - wall.png"));
            imagem.getRGB(0, 0, imagem.getWidth(), imagem.getHeight(),
                    textura[0], 0, 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a música principal do jogo que ficará em loop durante a execução.
     */
    private void carregaMusicaPrincipal() {
        File arquivoAudio = new File(Diretorio.SONS + "background.wav");
        AudioInputStream audioStream;

        try {
            audioStream = AudioSystem.getAudioInputStream(arquivoAudio);
        } catch (UnsupportedAudioFileException | IOException ex) {
            return;
        }

        try {
            musicaBackground = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            return;
        }

        try {
            musicaBackground.open(audioStream);
        } catch (LineUnavailableException | IOException ex) {
            return;
        }

        musicaBackground.start();
        musicaBackground.loop(Clip.LOOP_CONTINUOUSLY);
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
        double playerFOV = jogador.getFov();

        // Distancia de visão máxima permitida pelo jogo
        double distanciaMaxima = jogador.getFOG();

        // Guarda o tamanho dos blocos do mapa
        int tamanhoBloco = mapaAtual.getTamanhoBloco();

        // Fator que representa qual lado da parede foi atingido (vertical ou horizontal)
        int eixo;

        // Protótipo de "framebuffer" utilizado para acelerar a renderização
        int[] frameBuffer = new int[SCREENHEIGHT * SCREENWIDTH];

        // Classe filha de Graphics que inclui métodos adicionais para gráficos 2D
        Graphics2D render2D = (Graphics2D) g;

        // Fator de projeção utilizado para adequar as paredes ao monitor
        double fatorProjecao = (SCREENWIDTH / (2 * Math.tan(playerFOV / 2.0)));

        // Tamanho das texturas para renderização
        int tamanhoTextura = 128;

        // Renderiza o teto e o chão do mapa
        renderizaExtremidades(frameBuffer, playerX, playerY, playerAngulo, playerFOV,
                tamanhoTextura, tamanhoBloco);

        // Cálculo e renderização de todas as colunas de pixel da tela
        for (int i = 0; i < SCREENWIDTH; i++) {
            double anguloRaio = (playerAngulo - playerFOV / 2.0)
                    + ((double) i / SCREENWIDTH) * playerFOV;

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
            int comecoParede = (int) (SCREENHEIGHT - alturaParede) / 2;

            // Verifica se o indice da parede sai do limite da tela
            if (comecoParede < 0) {
                comecoParede = 0;
            }

            int fimParede = alturaParede + comecoParede;

            // Verifica se o indice da parede sai do limite da tela
            if (fimParede >= SCREENHEIGHT) {
                fimParede = SCREENHEIGHT - 1;
            }

            // Busca o id da textura com base no seu valor no mapa
            int idTextura = mapaAtual.getValor(posX, posY) - 1;

            // Valor real da coluna de textura que deve ser percorrida
            int texturaX = (int) (paredeX * tamanhoTextura);

            // Determina a posição final da textura em X com base em seu comprimento real
            if ((eixo == 0 && cosRaio > 0) || (eixo == 1 && sinRaio < 0)) {
                texturaX = tamanhoTextura - texturaX - 1;
            }

            // Calcula o fator de escala que deve ser usado para andar na imagem da textura
            double variacao = 1.0 * tamanhoTextura / alturaParede;
            double posicaoTextura = (comecoParede - SCREENHEIGHT / 2 + alturaParede / 2) * variacao;

            // Percorre todo os pixels da coluna atribuindo a cor da textura a eles
            for (int y = comecoParede; y < fimParede; y++) {
                int texturaY = (int) posicaoTextura & (tamanhoTextura - 1);
                posicaoTextura += variacao;

                int corPixel = textura[idTextura][tamanhoTextura * texturaY + texturaX];

                frameBuffer[y * SCREENWIDTH + i] = transformaCor(corPixel, fatorFOG);
            }
        }

        // Usa as cores RGB acumuladas no frameBuffer para criar uma imagem e renderiza-la
        BufferedImage frame = new BufferedImage(SCREENWIDTH, SCREENHEIGHT,
                BufferedImage.TYPE_INT_RGB);

        // "Seta" os valores das cores do frame
        frame.setRGB(0, 0, SCREENWIDTH, SCREENHEIGHT, frameBuffer,
                0, SCREENWIDTH);

        // Renderiza o frame todo
        render2D.drawImage(frame, 0, 0, null);
    }

    /**
     * Renderiza as extremidades (teto e chão) do mapa atual.
     * @param frameBuffer Array de números inteiros que guarda os pixels do frame
     * @param playerX Posição em x do jogador
     * @param playerY Posição em y do jogador
     * @param playerAngulo Angulo do jogador
     * @param playerFOV Campo de visão do jogador
     * @param tamanhoTextura Tamanho inteiro da textura
     * @param tamanhoBloco Tamanho de cada bloco do mapa
     */
    private void renderizaExtremidades(int[] frameBuffer, double playerX, double playerY,
            double playerAngulo, double playerFOV,
            int tamanhoTextura, int tamanhoBloco) 
    {
        
        // Posições do raio mínimo (mais a esquerda) e do raio máximo (mais a direita).
        // Ambos representam os limites mínimos e máximos do teto/chão a ser renderizado
        double angle = playerAngulo - playerFOV / 2.0;
        double cosRaioMinimo = Math.cos(angle);
        double sinRaioMinimo = Math.sin(angle);
        double cosRaioMaximo = Math.cos(angle + playerFOV);
        double sinRaioMaximo = Math.sin(angle + playerFOV);
        
        // Posição central da visão do jogador (meio da tela)
        double centroDeVisao = SCREENHEIGHT / 2;

        // Percorre todas linhas necessárias da tela para renderizar a imagem
        for (int y = 0; y < SCREENHEIGHT; y++) {
            
            // Posição relativa da linha sendo desenhada e da posição
            // central da tela.
            int posicaoRelativa = (int) (y - centroDeVisao);

            // Distancia horizontal da linha baseado no centro de visão e 
            // na posição relativa da linha em relação ao centro
            double distanciaLinha = centroDeVisao / posicaoRelativa;

            // Calcula os incrementos necessários para a posição real do 
            // teto/chão em x e y
            double incrementoX = distanciaLinha * (cosRaioMaximo - cosRaioMinimo) / SCREENWIDTH;
            double incrementoY = distanciaLinha * (sinRaioMaximo - sinRaioMinimo) / SCREENWIDTH;

            // Posições nas coordenadas x e y da posição atual no teto e chão
            double posX = playerX / tamanhoBloco + distanciaLinha * cosRaioMinimo;
            double posY = playerY / tamanhoBloco + distanciaLinha * sinRaioMinimo;

            // Percorre, em cada linha, os pixels da coluna para aplicar 
            // a cor da textura adequada
            for (int x = 0; x < SCREENWIDTH; x++) {
                
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
                int chaoID = 0;
                int tetoID = 0;
                int corPixel;

                // Aplica as cores das texturas em ambas as figuras
                corPixel = textura[chaoID][tamanhoTextura * texturaY + texturaX];
                frameBuffer[y * SCREENWIDTH + x] = corPixel;

                corPixel = textura[tetoID][tamanhoTextura * texturaY + texturaX];
                frameBuffer[(SCREENHEIGHT - y - 1) * SCREENWIDTH + x] = corPixel;
            }
        }
    }

    /**
     * Transforma as componentes de uma cor aplicando o efeito de FOG
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
        keyHandler.executaMetodo();
    }

    private void fechaJogo() {
        //musicaBackground.stop();
        janela.dispose();
        //função pra fechar o painel

    }
    
    static class desligaSom extends WindowAdapter{
        Clip c;

        desligaSom(Clip c){
            this.c = c;
        }
        
        @Override
        public void windowOpened(WindowEvent e){
            if(c != null)
                c.start();
        }

        @Override
        public void windowClosed(WindowEvent e){
            if(c == null){
                System.exit(0);
            }
            c.stop();
        }
    }
}
