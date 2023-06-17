package com.raycaster.interfaces;

import com.raycaster.engine.Diretorio;
import com.raycaster.engine.Engine;
import com.raycaster.interfaces.LabelAnimado.Animacao;
import com.raycaster.mapa.Mapa;
import static com.raycaster.mapa.Mapa.carregarMapList;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Classe que cria o menu inicial
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MenuInicial {

    private static BufferedImage imagemBackground;

    private static int indiceMapa;

    /**
     * Método para iniciar o jogo em uma thread apropriada
     *
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public static void inicia() {
        try {
            imagemBackground = lerImagem("ImagemInicial.png");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível encontrar o arquivo "
                    + "parede.JPG" + ".", "Visualizador", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível ler o arquivo "
                    + "parede.JPG" + ".", "Visualizador", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //carregarTexturas();
        SwingUtilities.invokeLater(() -> {
            MenuInicial();
        });

    }

    /**
     * Metodo que cria e mostra a interface grafica responsavel pelo menu
     * inicial
     *
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    private static void MenuInicial() {
        JFrame frameInicial = new JFrame("Menu inicial");

        frameInicial.setSize(800, 600);
        frameInicial.setResizable(true);
        frameInicial.setLocationRelativeTo(null);
        frameInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font fonte = carregaFonte().deriveFont(Font.PLAIN, 100f);

        LabelAnimado logo = new LabelAnimado("RayCaster",
                fonte.deriveFont(Font.PLAIN, 150f), Animacao.FLOAT);

        JPanel panelPrincipal = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponents(g);

                g.drawImage(imagemBackground, 0, 0,
                        getWidth(), getHeight(), this);
            }
        };

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal,
                BoxLayout.Y_AXIS));

        Runnable acaoJogar = new Runnable() {
            @Override
            public void run() {
                panelPrincipal.removeAll();
                panelPrincipal.repaint();
                panelPrincipal.revalidate();
                selecionaMapa(frameInicial, panelPrincipal, fonte);
            }
        };

        Runnable acaoMapa = new Runnable() {
            @Override
            public void run() {
                frameInicial.setVisible(false);
                MapEditorMenu.inicia(frameInicial);
            }

        };

        Runnable acaoSair = new Runnable() {

            @Override
            public void run() {
                frameInicial.dispose();
                System.exit(0);
            }
        };

        BotaoCustom botaoJogar = new BotaoCustom("Jogar", 
                fonte, acaoJogar);
        BotaoCustom botaoMapa = new BotaoCustom("Editor de mapas", 
                fonte, acaoMapa);
        BotaoCustom botaoSair = new BotaoCustom("Sair", 
                fonte, acaoSair);

        panelPrincipal.add(logo);
        panelPrincipal.add(botaoJogar);
        panelPrincipal.add(botaoMapa);
        panelPrincipal.add(botaoSair);

        frameInicial.add(panelPrincipal);
        frameInicial.setVisible(true);
    }

    /**
     * Carrega a fonte personalizada que será aplicada nos componentes.
     */
    private static Font carregaFonte() {
        Font fontePersonalizada = null;

        try {
            File fontFile = new File(Diretorio.SPRITE_HUD + "font.ttf");
            fontePersonalizada = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (IOException | FontFormatException e) {
            System.err.println("Fonte não existe!");
        }

        return fontePersonalizada;
    }

    /**
     * Metodo que inicia o jogo
     *
     * @param f Janela anterior que vai ser reaberta quando o jogo fechar
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    private static void jogar(JFrame f, Mapa map) {
        JFrame janela = new JFrame();
        janela.setTitle("RayCaster");
        janela.setSize(800, 600);
        janela.addWindowListener(new MapEditorMenu.event(f));
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janela.setResizable(true);

        Engine game = new Engine(800, 600, janela, map);

        janela.add(game);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    /**
     * Cria o menu de seleção de mapas.
     * @param frame Janela atual do jogo
     * @param panel Painel atual do jogo
     * @param font Fonte personalizada do menu
     */
    private static void selecionaMapa(JFrame frame, JPanel panel, Font font) {
        ArrayList<Mapa> mapas = carregarMapList();
        indiceMapa = 0;

        LabelAnimado textoMapa = new LabelAnimado("Selecione um mapa",
                font.deriveFont(Font.PLAIN, 150f), Animacao.FLOAT);

        LabelAnimado mapaAtual = new LabelAnimado(mapas.get(indiceMapa).toString(),
                font.deriveFont(Font.PLAIN, 100f), Animacao.FADE);

        panel.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                int keyCode = event.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                        indiceMapa = (indiceMapa - 1 + mapas.size()) % mapas.size();
                        mapaAtual.setText(mapas.get(indiceMapa).toString());
                    }
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                        indiceMapa = (indiceMapa + 1) % mapas.size();
                        mapaAtual.setText(mapas.get(indiceMapa).toString());
                    }
                    case KeyEvent.VK_ENTER ->
                        jogar(frame, mapas.get(indiceMapa));
                }
            }
        });

        BotaoCustom botaoVoltar = new BotaoCustom("Voltar", font);

        botaoVoltar.addActionListener((ActionEvent) -> {
            panel.removeAll();
            frame.dispose();
            MenuInicial();
        });

        panel.setFocusable(true);
        panel.requestFocus();

        textoMapa.setAlignmentX(CENTER_ALIGNMENT);
        mapaAtual.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(textoMapa);
        panel.add(Box.createVerticalStrut(100));
        panel.add(mapaAtual);
        panel.add(Box.createVerticalStrut(50));
        panel.add(botaoVoltar);
    }

    /**
     * Metodo para Abrir uma imagem
     *
     * @param s uma String com o diretorio do arquivo da imagem
     * @return BufferedImage contendo a imagem que esta no diretorio
     * especificado ou null se o diretorio estar vazio
     * @throws FileNotFoundException
     * @throws IOException
     *
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public static BufferedImage lerImagem(String s) throws FileNotFoundException, IOException {
        return lerImagem(new File(s));
    }

    /**
     * Metodo para Abrir uma imagem
     *
     * @param s uma String com o diretorio do arquivo da imagem
     * @return BufferedImage contendo a imagem que esta no diretorio
     * especificado ou null se o diretorio estar vazio
     * @throws FileNotFoundException
     * @throws IOException
     *
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    private static BufferedImage lerImagem(File f) throws FileNotFoundException, IOException {
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        return ImageIO.read(f);
    }
}
