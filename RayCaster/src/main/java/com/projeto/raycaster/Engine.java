package com.projeto.raycaster;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
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
 *
 * @author vinic
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
    
    private void keyBindings() {
        keyHandler.adicionaKey(KeyEvent.VK_W, () -> jogador.move(0, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_A, () -> jogador.move(-Math.PI / 2, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_S, () -> jogador.move(0, -1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_D, () -> jogador.move(Math.PI / 2, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_R, () -> jogador.recarregaItem());
        
        keyHandler.adicionaKey(KeyEvent.VK_1, () -> jogador.sacaItem(0));
        keyHandler.adicionaKey(KeyEvent.VK_2, () -> jogador.sacaItem(1));
    }

    private void carregaTexturas() {
        textura = new int[1][64*64];
        try {
            BufferedImage imagem = ImageIO.read(new File(Diretorio.TEXTURA_PAREDE + "01 - redbrick.png"));
            imagem.getRGB(0, 0, imagem.getWidth(), imagem.getHeight(),
                    textura[0], 0, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    /*@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[] cor = new int[1];
        double CORRECTION = (SCREENWIDTH / (2 * Math.tan(jogador.getFov() / 2.0)));
        int tamanhoBloco = mapaAtual.getTamanhoBloco();
        
        Graphics2D g2d = (Graphics2D) g;
        
        AffineTransform affine = new AffineTransform();
        g2d.setTransform(affine);

        for (int i = 0; i < SCREENWIDTH; i++) {
            double rayAngle = (jogador.getAngulo() - jogador.getFov() / 2.0) 
                    + ((float) i / (float) SCREENWIDTH) * jogador.getFov();


            double distancia = calculaDistancia(rayAngle, cor) * 
                    Math.cos(rayAngle - jogador.getAngulo());

            int alturaParede = (int) ((tamanhoBloco / distancia) * CORRECTION);
            int comecoParede = (int) (SCREENHEIGHT - alturaParede) / 2;
            
            if (comecoParede < 0) 
                comecoParede = 0;
            
            int fimParede = comecoParede + alturaParede; 
            
            if (fimParede >= SCREENHEIGHT) 
                fimParede = SCREENHEIGHT - 1;
            
            
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawLine(i, 0, i, comecoParede);
            g2d.setColor(new Color(255 / cor[0], 0, 0));
            g2d.drawLine(i, comecoParede, i, fimParede);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawLine(i, fimParede, i, SCREENHEIGHT);
        }
               
        jogador.desenhaComponentes();
    }
    
    private double calculaDistancia(double anguloRaio, int[] corLocal) {
        int posX, posY;
        int direcaoX, direcaoY;
        double deltaX, deltaY;
        double distanciaX, distanciaY;
        double cosRay = Math.cos(anguloRaio);
        double sinRay = Math.sin(anguloRaio);

        // Posição inicial do ponto extremo do raio
        posX = (int) jogador.getX();
        posY = (int) jogador.getY();
        
        // Quantas unidades por vez o raio deve andar
        deltaX = (cosRay == 0 ? Double.MAX_VALUE : Math.abs(1 / cosRay));
        deltaY = (sinRay == 0 ? Double.MAX_VALUE : Math.abs(1 / sinRay));

        // Verifica a orientação em X do raio
        if (cosRay < 0) {
            direcaoX = -1;
            distanciaX = (jogador.getX() - posX) * deltaX;
        } else {
            direcaoX = 1;
            distanciaX = (posX + 1.0 - jogador.getX()) * deltaX;
        }

        // Verifica a orientação em Y do raio
        if (sinRay < 0) {
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
                corLocal[0] = 1;
            } else {
                distanciaY += deltaY;
                posY += direcaoY;
                corLocal[0] = 2;
            }

            // Checa a colisão do raio com a parede
            if (mapaAtual.checaColisao(posX, posY)) {
                break;
            }
        }

        return (corLocal[0] == 1 ? (distanciaX - deltaX) : (distanciaY - deltaY));
    }*/
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[] cor = new int[1];
        int[][] frameBuffer = new int[SCREENHEIGHT][SCREENWIDTH];
        
        Graphics2D g2d = (Graphics2D) g;

        int cameraOffsetY = (int) (Math.sin(Math.toRadians(jogador.getPitch())) * 10);
        int cameraOffsetX = (int) (Math.sin(Math.toRadians(jogador.getPitch())) * 10);
        
        int tamanhoBloco = mapaAtual.getTamanhoBloco();
        double CORRECTION = (SCREENWIDTH / (2 * Math.tan(jogador.getFov() / 2.0)));

        for (int i = 0; i < SCREENWIDTH; i++) {
            double rayAngle = (jogador.getAngulo() - jogador.getFov() / 2.0)
                    + ((double) i / SCREENWIDTH) * jogador.getFov();

            int posX, posY;
            int direcaoX, direcaoY;
            double deltaX, deltaY;
            double distanciaX, distanciaY;
            double cosRay = Math.cos(rayAngle);
            double sinRay = Math.sin(rayAngle);

            // Posição inicial do ponto extremo do raio
            posX = (int) jogador.getX();
            posY = (int) jogador.getY();

            deltaX = Math.sqrt(1 + (sinRay * sinRay) / (cosRay * cosRay));
            deltaY = Math.sqrt(1 + (cosRay * cosRay) / (sinRay * sinRay));

            // Verifica a orientação em X do raio
            if (cosRay < 0) {
                direcaoX = -1;
                distanciaX = (jogador.getX() - posX) * deltaX;
            } else {
                direcaoX = 1;
                distanciaX = (posX + 1.0 - jogador.getX()) * deltaX;
            }

            // Verifica a orientação em Y do raio
            if (sinRay < 0) {
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

            double distanciaFinal = (cor[0] == 0) ? (distanciaX - deltaX) : (distanciaY - deltaY);
            distanciaFinal *= Math.cos(rayAngle - jogador.getAngulo());

            int alturaParede = (int) ((tamanhoBloco / distanciaFinal) * CORRECTION);
            
            int comecoParede = (int) (SCREENHEIGHT - alturaParede) / 2;
            if(comecoParede < 0) comecoParede = 0;
            int fimParede = alturaParede + comecoParede;
            if (fimParede >= SCREENHEIGHT) {
                fimParede = SCREENHEIGHT - 1;
            }

            int texNum = mapaAtual.getValor(posX, posY) - 1;
            int texWidth = 64;
          

            double wallX;
            if (cor[0] == 0) {
                wallX = jogador.getY() + distanciaFinal * sinRay;
            } else {
                wallX = jogador.getX() + distanciaFinal * cosRay;
            }
            System.out.println(wallX);
            wallX -= Math.floor(wallX);

            int texX = (int) (wallX * texWidth);
            
            if ((cor[0] == 0 && cosRay > 0) || (cor[0] == 1 && sinRay < 0)) {
                texX = texWidth - texX - 1;
            }

            double step = 1.0 * texWidth / alturaParede;
            double texPos = (comecoParede - SCREENHEIGHT / 2 + alturaParede / 2) * step;
            
            for (int y = comecoParede; y < fimParede; y++) {
                int texY = (int) (texPos) & (texWidth - 1);
                texPos += step;
                int color = textura[texNum][texWidth*texY + texX];
                if (cor[0] == 1) {
                    color = (color >> 1) & 8355711;
                }
                
                g2d.setColor(new Color(color));
                g2d.fillRect(i, y, 1,1);
            }

        }

    }

    private void calculaDistancia(double anguloRaio, int[] corLocal, int[][] frameBuffer, int x) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        keyHandler.executaMetodo();
    }
}
