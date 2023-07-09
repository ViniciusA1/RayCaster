package com.raycaster.interfaces.menus;

import com.raycaster.engine.arquivos.ArquivoUtils;
import com.raycaster.engine.arquivos.Diretorio;
import com.raycaster.engine.Engine;
import com.raycaster.engine.Estado;
import com.raycaster.engine.sons.Musica;
import com.raycaster.interfaces.componentes.BotaoCustom;
import com.raycaster.interfaces.paineis.InterfaceManager;
import com.raycaster.interfaces.componentes.LabelAnimado;
import com.raycaster.interfaces.componentes.LabelAnimado.Animacao;
import com.raycaster.interfaces.paineis.Painel;
import com.raycaster.mapa.Mapa;
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
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Classe que cria o menu inicial
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MenuInicial {

    private static BufferedImage imagemBackground;
    private static Musica musicaInicial;
    
    private static MenuConfig menuConfig;

    /**
     * Método para iniciar o jogo em uma thread apropriada
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

        SwingUtilities.invokeLater(() -> {
            MenuInicial();
        });
    }

    /**
     * Metodo que cria e mostra a interface grafica responsavel pelo menu
     * inicial.
     */
    private static void MenuInicial() {
        JFrame frameInicial = new JFrame("Menu inicial");

        frameInicial.setSize(800, 600);
        frameInicial.setResizable(true);
        frameInicial.setLocationRelativeTo(null);
        frameInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font fonte = carregaFonte().deriveFont(Font.PLAIN, 80f);

        LabelAnimado logo = new LabelAnimado("RayCaster",
                fonte.deriveFont(Font.BOLD, 150f), Animacao.FLOAT);
        
        musicaInicial = new Musica("menu", Estado.OCIOSO);
        musicaInicial.playSom(Estado.OCIOSO);
        
        Runnable acaoJogar = () -> {
            selecionaMapa(frameInicial, fonte);
        };

        Runnable acaoMapa = () -> {
            musicaInicial.stopSom(Estado.OCIOSO);
            MapEditorMenu.inicia(frameInicial);
        };
        
        Runnable acaoConfig = () -> {
            configuraJogo(frameInicial);
        };

        Runnable acaoSair = () -> {
            frameInicial.dispose();
            InterfaceManager.clear();
            musicaInicial.stopSom(Estado.OCIOSO);
            System.exit(0);
        };

        BotaoCustom botaoJogar = new BotaoCustom("Jogar",
                fonte, acaoJogar);
        BotaoCustom botaoMapa = new BotaoCustom("Editor de mapas",
                fonte, acaoMapa);
        BotaoCustom botaoConfig = new BotaoCustom("Configuração", 
                fonte, acaoConfig);
        BotaoCustom botaoSair = new BotaoCustom("Sair",
                fonte, acaoSair);

        Painel panelPrincipal = new Painel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponents(g);

                g.drawImage(imagemBackground, 0, 0,
                        getWidth(), getHeight(), this);
            }
            
            @Override
            public void entrar() {
                super.entrar();
                
                if(!musicaInicial.isRunning(Estado.OCIOSO)) {
                    musicaInicial.playSom(Estado.OCIOSO);
                }
                
                SwingUtilities.invokeLater(() -> {
                   botaoJogar.requestFocusInWindow();
                });
            }
        };

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal,
                BoxLayout.Y_AXIS));

        panelPrincipal.add(logo);
        panelPrincipal.add(botaoJogar);
        panelPrincipal.add(botaoMapa);
        panelPrincipal.add(botaoConfig);
        panelPrincipal.add(botaoSair);
        
        InterfaceManager.push(frameInicial, panelPrincipal);

        frameInicial.setVisible(true);
        
        menuConfig = new MenuConfig(frameInicial, 
                imagemBackground, fonte);
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
     */
    private static void jogar(JFrame frame, Mapa map) {
        musicaInicial.stopSom(Estado.OCIOSO);
        
        Engine game = new Engine(frame, map, menuConfig);
        InterfaceManager.push(frame, game);
    }
    
    private static void configuraJogo(JFrame frame) {
        InterfaceManager.push(frame, menuConfig);
    }

    /**
     * Cria o menu de seleção de mapas.
     *
     * @param frame Janela atual do jogo
     * @param panel Painel atual do jogo
     * @param font Fonte personalizada do menu
     */
    private static void selecionaMapa(JFrame frame, Font font) {
        List<String> mapas = ArquivoUtils.leMapas();

        LabelAnimado textoMapa = new LabelAnimado("Selecione um mapa",
                font.deriveFont(Font.PLAIN, 150f), 
                Animacao.FLOAT);

        LabelAnimado mapaAtual = new LabelAnimado(mapas.toArray(new String[0]), 
                0, font.deriveFont(Font.PLAIN, 100f), 
                Animacao.FADE);
        
        Painel panel = new Painel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponents(g);

                g.drawImage(imagemBackground, 0, 0,
                        getWidth(), getHeight(), this);
            }
            
            @Override
            public void entrar() {
                super.entrar();
                
                if(!musicaInicial.isRunning(Estado.OCIOSO)) {
                    musicaInicial.playSom(Estado.OCIOSO);
                    musicaInicial.playSom(Estado.OCIOSO);
                }
            }
        };

        panel.setLayout(new BoxLayout(panel,
                BoxLayout.Y_AXIS));

        panel.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {                
                if(event.getKeyCode() == KeyEvent.VK_ENTER) {
                    InterfaceManager.playSom(Estado.USANDO);
                    
                    jogar(frame, Mapa.carregaMapa(mapas.
                            get(mapaAtual.getIdAtual())));
                }
                
                mapaAtual.interacaoTeclado(event);
            }
        });
        
        Runnable acaoVoltar = () -> {
            InterfaceManager.pop();
        };

        BotaoCustom botaoVoltar = new BotaoCustom("Voltar", 
                font, acaoVoltar, true);

        textoMapa.setAlignmentX(CENTER_ALIGNMENT);
        mapaAtual.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(textoMapa);
        panel.add(Box.createVerticalGlue());
        panel.add(mapaAtual);
        panel.add(Box.createVerticalGlue());
        panel.add(botaoVoltar);
        
        InterfaceManager.push(frame, panel);
    }

    /**
     * Metodo para Abrir uma imagem
     *
     * @param s uma String com o diretorio do arquivo da imagem
     * @return BufferedImage contendo a imagem que esta no diretorio
     * especificado ou null se o diretorio estar vazio
     * @throws FileNotFoundException Exception lançado por não encontrar arquivo
     * @throws IOException Exception lançado por erro de entrada/saida
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
     * @throws FileNotFoundException Exception lançado por não encontrar arquivo
     * @throws IOException Exception lançado por erro de entrada/saida
     */
    private static BufferedImage lerImagem(File f) throws FileNotFoundException, IOException {
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        return ImageIO.read(f);
    }
}
