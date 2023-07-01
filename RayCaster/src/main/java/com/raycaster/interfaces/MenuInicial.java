package com.raycaster.interfaces;

import com.raycaster.engine.ArquivoUtils;
import com.raycaster.engine.Diretorio;
import com.raycaster.engine.EfeitosSonoros;
import com.raycaster.engine.Engine;
import com.raycaster.engine.Estado;
import com.raycaster.interfaces.LabelAnimado.Animacao;
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
import java.util.EnumSet;
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
    private static int indiceMapa;
    private static EfeitosSonoros musicaInicial;

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

        Font fonte = carregaFonte().deriveFont(Font.PLAIN, 100f);

        LabelAnimado logo = new LabelAnimado("RayCaster",
                fonte.deriveFont(Font.BOLD, 150f), Animacao.FLOAT);
        
        musicaInicial = new EfeitosSonoros("menu", Estado.OCIOSO);
        musicaInicial.emiteSom(Estado.OCIOSO);
        musicaInicial.setLoop(Estado.OCIOSO);
        
        Runnable acaoJogar = () -> {
            frameInicial.remove(InterfaceManager.peek());
            frameInicial.repaint();
            frameInicial.revalidate();
            selecionaMapa(frameInicial, fonte);
        };

        Runnable acaoMapa = () -> {
            musicaInicial.stopLoop(Estado.OCIOSO);
            MapEditorMenu.inicia(frameInicial);
        };
        
        Runnable acaoConfig = () -> {
            frameInicial.remove(InterfaceManager.peek());
            frameInicial.repaint();
            frameInicial.revalidate();
            configuraJogo(frameInicial, fonte);
        };

        Runnable acaoSair = () -> {
            frameInicial.dispose();
            InterfaceManager.clear();
            musicaInicial.stopLoop(Estado.OCIOSO);
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
                    musicaInicial.emiteSom(Estado.OCIOSO);
                    musicaInicial.setLoop(Estado.OCIOSO);
                }
                
                SwingUtilities.invokeLater(() -> {
                   botaoJogar.requestFocusInWindow();
                });
            }
        };

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal,
                BoxLayout.Y_AXIS));
        
        InterfaceManager.push(panelPrincipal);

        panelPrincipal.add(logo);
        panelPrincipal.add(botaoJogar);
        panelPrincipal.add(botaoMapa);
        panelPrincipal.add(botaoConfig);
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
     */
    private static void jogar(JFrame f, Mapa map) {
        musicaInicial.stopLoop(Estado.OCIOSO);
        
        f.remove(InterfaceManager.peek());
        Engine game = new Engine(320, 200, f, map);
        InterfaceManager.push(game);
        f.add(game);
    }
    
    private static void configuraJogo(JFrame frame, Font fonte) {
        MenuConfig menuConfiguracao = new MenuConfig(frame, 
                imagemBackground, fonte);
        
        InterfaceManager.push(menuConfiguracao);
        
        frame.add(menuConfiguracao);
        frame.repaint();
        frame.revalidate();
        menuConfiguracao.requestFocus();
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
        indiceMapa = 0;

        LabelAnimado textoMapa = new LabelAnimado("Selecione um mapa",
                font.deriveFont(Font.BOLD, 150f), Animacao.FLOAT);

        LabelAnimado mapaAtual = new LabelAnimado(mapas.get(indiceMapa),
                font.deriveFont(Font.PLAIN, 100f), Animacao.FADE);

        EnumSet<Estado> estados = EnumSet.noneOf(Estado.class);

        estados.add(Estado.USANDO);
        estados.add(Estado.SACANDO);

        EfeitosSonoros sons = new EfeitosSonoros("botao", estados);
        
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
                    musicaInicial.emiteSom(Estado.OCIOSO);
                    musicaInicial.setLoop(Estado.OCIOSO);
                }
            }
            
            @Override
            public void sairPop() {
                super.sairPop();
                
                sons.close();
            }
        };

        panel.setLayout(new BoxLayout(panel,
                BoxLayout.Y_AXIS));
        
        InterfaceManager.push(panel);

        panel.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                int keyCode = event.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                        indiceMapa = (indiceMapa - 1 + mapas.size()) % mapas.size();
                        mapaAtual.setText(mapas.get(indiceMapa));
                        sons.emiteSom(Estado.SACANDO);
                    }
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                        indiceMapa = (indiceMapa + 1) % mapas.size();
                        mapaAtual.setText(mapas.get(indiceMapa));
                        sons.emiteSom(Estado.SACANDO);
                    }
                    case KeyEvent.VK_ENTER -> {
                        sons.emiteSom(Estado.USANDO);
                        jogar(frame, Mapa.carregaMapa(mapas.get(indiceMapa)));
                    }
                }
            }
        });
        
        Runnable acaoVoltar = () -> {
            frame.remove(panel);
            InterfaceManager.pop();
            Painel novoPainel = InterfaceManager.peek();
            frame.add(novoPainel);
            frame.repaint();
            frame.revalidate();
            
            novoPainel.requestFocus();
        };

        BotaoCustom botaoVoltar = new BotaoCustom("Voltar", 
                font, acaoVoltar);

        textoMapa.setAlignmentX(CENTER_ALIGNMENT);
        mapaAtual.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(textoMapa);
        panel.add(Box.createVerticalStrut(100));
        panel.add(mapaAtual);
        panel.add(Box.createVerticalStrut(50));
        panel.add(botaoVoltar);
        
        frame.add(panel);
        frame.revalidate();
        
        panel.setFocusable(true);
        panel.requestFocus();
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
