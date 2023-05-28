package com.raycaster.engine;

import com.raycaster.interfaces.AnimacaoPlayer;
import com.raycaster.itens.ArmaCurta;
import com.raycaster.itens.ArmaLonga;
import com.raycaster.interfaces.HUD;
import com.raycaster.itens.Item;
import com.raycaster.entidades.Player;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Classe que guarda todos os atributos e métodos do motor gŕafico do jogo.
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

    /**
     * Construtor da engine, recebe o tamanho horizontal e vertical da JFrame
     * @param width Comprimento horizontal da tela
     * @param height Comprimento vertical da tela
     */
    public Engine(int width, int height) {
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

        //carregaMusicaPrincipal();
    }

    /**
     * Aplica as configurações iniciais ao jogo, incluindo configurações
     * do player, armas e keybindings.
     */
    private void configInicial() {
        jogador = new Player(100, 400, 300, 2, 60);
        mapaAtual = new Mapa("lobby.txt", 20);
        mapaAtual.carregar();

        mouseHandler = new MouseInput(jogador, 0.001);

        Item pistola = new ArmaLonga("pistol", 100, 100, 30, 500);
        Item faca = new ArmaCurta("knife", 100, 1000);

        jogador.adicionaItem(pistola);
        jogador.adicionaItem(faca);

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

        keyHandler.adicionaKey(KeyEvent.VK_1, () -> jogador.sacaItem(0));
        keyHandler.adicionaKey(KeyEvent.VK_2, () -> jogador.sacaItem(1));
    }

    /**
     * Carrega todas as texturas das paredes do jogo armazenadas no grid.
     */
    private void carregaTexturas() {
        textura = new int[1][64 * 64];
        try {
            BufferedImage imagem = ImageIO.read(new File(Diretorio.TEXTURA_PAREDE + "01 - redbrick.png"));
            imagem.getRGB(0, 0, imagem.getWidth(), imagem.getHeight(),
                    textura[0], 0, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a música principal do jogo que ficará em loop durante a execução.
     */
    private void carregaMusicaPrincipal() {
        File arquivoAudio = new File("sons" + File.separator + "background.wav");
        AudioInputStream audioStream;

        try {
            audioStream = AudioSystem.getAudioInputStream(arquivoAudio);
        } catch (UnsupportedAudioFileException | IOException ex) {
            return;
        }

        Clip musicaBackground;
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
     * Renderiza todos os componentes associados a tela (JPanel) principal do jogo,
     * incluindo o cálculo e uso efetivo do raycasting.
     * @param g Componente gráfico utilizado pela JPanel para renderizar a tela
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fator que representa qual lado da parede foi atingido (vertical ou horizontal)
        int[] cor = new int[1];
        
        // Protótipo de "framebuffer" utilizado para acelerar a renderização
        int[] frameBuffer = new int[SCREENHEIGHT * SCREENWIDTH];

        // Classe filha de Graphics que inclui métodos adicionais para gráficos 2D
        Graphics2D render2D = (Graphics2D) g;

        // Guarda o tamanho dos blocos do mapa
        int tamanhoBloco = mapaAtual.getTamanhoBloco();
        
        // Fator de projeção utilizado para adequar as paredes ao monitor
        double fatorProjecao = (SCREENWIDTH / (2 * Math.tan(jogador.getFov() / 2.0)));

        
        // Cálculo e renderização de todas as colunas de pixel da tela
        for (int i = 0; i < SCREENWIDTH; i++) {
            double anguloRaio = (jogador.getAngulo() - jogador.getFov() / 2.0)
                    + ((double) i / SCREENWIDTH) * jogador.getFov();

            int posX, posY;
            int direcaoX, direcaoY;
            double deltaX, deltaY;
            double distanciaX, distanciaY;
            double cosRaio = Math.cos(anguloRaio);
            double sinRaio = Math.sin(anguloRaio);

            // Posição inicial do ponto extremo do raio
            posX = (int) jogador.getX();
            posY = (int) jogador.getY();

            // Determina o quanto é possivel andar em cada unidade do mapa
            deltaX = (cosRaio == 0) ? Double.MAX_VALUE : Math.abs(1 / cosRaio);
            deltaY = (sinRaio == 0) ? Double.MAX_VALUE : Math.abs(1 / sinRaio);

            // Verifica a orientação em X do raio
            if (cosRaio < 0) {
                direcaoX = -1;
                distanciaX = (jogador.getX() - posX) * deltaX;
            } else {
                direcaoX = 1;
                distanciaX = (posX + 1.0 - jogador.getX()) * deltaX;
            }

            // Verifica a orientação em Y do raio
            if (sinRaio < 0) {
                direcaoY = -1;
                distanciaY = (jogador.getY() - posY) * deltaY;
            } else {
                direcaoY = 1;
                distanciaY = (posY + 1.0 - jogador.getY()) * deltaY;
            }

            // Percorre o mapa até encontrar o primeiro obstáculo
            while (true) {

                // Verifica qual componente do raio é menor e aumenta-a
                if (distanciaX < distanciaY) {
                    distanciaX += deltaX;
                    posX += direcaoX;
                    cor[0] = 0;
                } else {
                    distanciaY += deltaY;
                    posY += direcaoY;
                    cor[0] = 1;
                }

                // Checa a colisão do raio com a parede
                if (mapaAtual.checaColisao(posX, posY)) {
                    break;
                }
            }

            // Calcula o valor real da distancia percorrida pelo raio que atingiu a parede primeiro
            double distanciaFinal = (cor[0] == 0) ? (distanciaX - deltaX) : (distanciaY - deltaY);

            // Fator de correção para o efeito fisheye (olho de peixe)
            distanciaFinal *= Math.cos(anguloRaio - jogador.getAngulo());

            // Calcula os valores da altura da parede, seu começo e fim no eixo y
            int alturaParede = (int) ((tamanhoBloco / distanciaFinal) * fatorProjecao);
            int comecoParede = (int) (SCREENHEIGHT - alturaParede) / 2;
            
            // Verifica se o indice da parede sai do limite da tela
            if(comecoParede < 0)
                comecoParede = 0;
            
            int fimParede = alturaParede + comecoParede;
            
            // Verifica se o indice da parede sai do limite da tela
            if(fimParede > SCREENHEIGHT)
                fimParede = SCREENHEIGHT;

            // Busca o id da textura com base no seu valor no mapa
            int idTextura = mapaAtual.getValor(posX, posY) - 1;
            
            // Determina o tamanho da textura a ser ajustado
            int tamanhoTextura = 64;

            // Determina a posição no eixo x em que a parede foi atingida
            double paredeX;
            
            if (cor[0] == 0)
                paredeX = jogador.getY() + distanciaFinal * sinRaio;
            else
                paredeX = jogador.getX() + distanciaFinal * cosRaio;
            
            paredeX /= tamanhoBloco;
            paredeX -= Math.floor(paredeX);

            // Valor real da coluna de textura que deve ser percorrida
            int texturaX = (int) (paredeX * tamanhoTextura);

            // 
            if ((cor[0] == 0 && cosRaio > 0) || (cor[0] == 1 && sinRaio < 0)) {
                texturaX = tamanhoTextura - texturaX - 1;
            }

            // Calcula o fator de escala que deve ser usado para andar na imagem da textura
            double variacao = 1.0 * tamanhoTextura / alturaParede;
            double posicaoTextura = (comecoParede - SCREENHEIGHT / 2 + alturaParede / 2) * variacao;

            // Percorre todo os pixels da coluna atribuindo a cor da textura a eles
            for (int y = comecoParede; y < fimParede; y++) {
                int texturaY = (int) posicaoTextura & (tamanhoTextura - 1);
                posicaoTextura += variacao;
                
                int color = textura[idTextura][tamanhoTextura * texturaY + texturaX];
                
                if(cor[0] == 1) color = (color >> 1) & 8355711;
                frameBuffer[y * SCREENWIDTH + i] = color;
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
     * Executa as ações do frame atual (determinado pelo timer).
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
}