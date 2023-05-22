package com.projeto.raycaster;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private BufferedImage[] textura;

    public Engine(int width, int height) {
        this.SCREENWIDTH = width;
        this.SCREENHEIGHT = height;

        configInicial();

        int intervalo = 8;
        gameTimer = new Timer(intervalo, this);
        gameTimer.start();

        addKeyListener(keyHandler);
        addMouseMotionListener(mouseHandler);
        setFocusable(true);
        
        carregaMusicaPrincipal();
    }

    private void configInicial() {
        jogador = new Player(100, 2.5, 2.5, 0.05, 60);
        mapaAtual = new Mapa("lobby.txt", 20);
        mapaAtual.carregar();

        mouseHandler = new MouseInput(jogador, 0.001);
        
        Item pistola = new ArmaLonga("pistol.txt", 100, 100, 1);
        Item ak47 = new ArmaLonga("ak47.txt", 300, 300, 0.5);
        Item shotgun = new ArmaLonga("shotgun.txt", 50, 50, 2);
        Item faca = new ArmaCurta("knife.txt", 50, 2);
        jogador.adicionaItem(pistola);
        jogador.adicionaItem(ak47);
        jogador.adicionaItem(shotgun);
        jogador.adicionaItem(faca);
        jogador.sacaItem(0);

        keyHandler = new KeyInput();
        keyHandler.adicionaKey(KeyEvent.VK_W, () -> jogador.move(0, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_A, () -> jogador.move(-Math.PI/2, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_S, () -> jogador.move(0, -1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_D, () -> jogador.move(Math.PI/2, 1, mapaAtual));
        keyHandler.adicionaKey(KeyEvent.VK_1, () -> jogador.sacaItem(0));
        keyHandler.adicionaKey(KeyEvent.VK_2, () -> jogador.sacaItem(1));
        keyHandler.adicionaKey(KeyEvent.VK_3, () -> jogador.sacaItem(2));
        keyHandler.adicionaKey(KeyEvent.VK_4, () -> jogador.sacaItem(3));

        carregaTexturas();
    }

    private void carregaTexturas() {
        /*try {
            textura = new BufferedImage[1];
            textura[0] = ImageIO.read(new File("machine-gun.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        
        //musicaBackground.start();
        //musicaBackground.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[] cor = new int[1];
        
        int cameraOffsetY = (int) (Math.sin(Math.toRadians(jogador.getPitch())) * 10);
        int cameraOffsetX = (int) (Math.sin(Math.toRadians(jogador.getPitch())) * 10);

        for (int i = 0; i < SCREENWIDTH; i++) {
            double rayAngle = (jogador.getAngulo() - jogador.getFov() / 2.0) 
                    + ((float) i / (float) SCREENWIDTH) * jogador.getFov();


            double distancia = calculaDistancia(rayAngle, cor) * 
                    Math.cos(rayAngle - jogador.getAngulo());

            int alturaParede = (int) (SCREENHEIGHT / distancia);
            int comecoParede = (int) (SCREENHEIGHT - alturaParede) / 2;
            if (comecoParede < 0) comecoParede = 0;
            int fimParede = comecoParede + alturaParede; 
            if (fimParede >= SCREENHEIGHT) fimParede = SCREENHEIGHT - 1;
            
            
            g.setColor(new Color(0, 128, 255));
            g.drawLine(i, 0, i, comecoParede);
            g.setColor(new Color(255 / cor[0], 0, 0));
            g.drawLine(i, comecoParede, i, fimParede);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(i, fimParede, i, SCREENHEIGHT);
        }
        
        BufferedImage imagem = jogador.getFrameAtual();
        
        g.drawImage(imagem, SCREENWIDTH / 2 + cameraOffsetX, SCREENHEIGHT / 2 + cameraOffsetY, SCREENWIDTH / 2, SCREENHEIGHT / 2, this);
        
        //g.drawImage(jogador.getFrameAtual(), cameraOffsetX, SCREENHEIGHT - 500 + cameraOffsetY, 
          //      SCREENWIDTH, 600, this);
        
        g.setColor(Color.BLUE);
        g.fillRect(0, SCREENHEIGHT - 100, SCREENWIDTH, 100);
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

        return(corLocal[0] == 1 ? (distanciaX - deltaX) : (distanciaY - deltaY));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        keyHandler.executaMetodo();
        //mouseHandler.centralizaCursor();
    }
}