package com.raycaster.interfaces;

import com.raycaster.engine.Diretorio;
import com.raycaster.engine.Engine;
import com.raycaster.interfaces.MapEditorMenu.event;
import com.raycaster.mapa.MapGroup.ListData;
import com.raycaster.mapa.Mapa;
import static com.raycaster.mapa.Mapa.carregarMapList;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 * Classe que cria o menu inicial
 *
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MenuInicial {

    private static BufferedImage imagemBackground;
    //private static ArrayList<BufferedImage> texturas;

    private static double direcao;
    private static int posicaoY;

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

        JLabel logo = new JLabel("RayCaster");
        logo.setFont(fonte.deriveFont(Font.BOLD, 150f));
        logo.setAlignmentX(CENTER_ALIGNMENT);
        logo.setForeground(Color.RED);

        BotaoCustom botaoJogar = new BotaoCustom("Jogar", fonte);
        BotaoCustom botaoMapa = new BotaoCustom("Editor de mapas", fonte);
        BotaoCustom botaoSair = new BotaoCustom("Sair", fonte);

        botaoJogar.addActionListener((ActionEvent) -> {
            frameInicial.setVisible(false);
            selecionaMapa(frameInicial);
        });

        botaoMapa.addActionListener((ActionEvent) -> {
            frameInicial.setVisible(false);
            MapEditorMenu.inicia(frameInicial);
        });

        botaoSair.addActionListener((ActionEvent) -> {
            frameInicial.dispose();
            System.exit(0);
        });

        direcao = 1;
        posicaoY = logo.getY();
        
        JPanel panelPrincipal = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                logo.setLocation((this.getWidth() - logo.getWidth()) / 2, posicaoY);

                g.drawImage(imagemBackground, 0, 0,
                        getWidth(), getHeight(), this);
            }
        };

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal,
                BoxLayout.Y_AXIS));

        panelPrincipal.add(logo);
        panelPrincipal.add(botaoJogar);
        panelPrincipal.add(botaoMapa);
        panelPrincipal.add(botaoSair);

        Timer timer = new Timer(60, e -> mudaLogo(panelPrincipal, logo));
        timer.start();

        frameInicial.add(panelPrincipal);
        frameInicial.setVisible(true);
    }

    private static void mudaLogo(JPanel panel, JLabel logo) {
        posicaoY += direcao;

        if (posicaoY <= 0) {
            posicaoY = 0;
            direcao = 1;
        } else if (posicaoY + logo.getHeight() >= 200) {
            posicaoY = 200 - logo.getHeight();
            direcao = -1;
        }

        logo.setLocation((panel.getWidth() - logo.getWidth()) / 2, posicaoY);
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

    private static void selecionaMapa(JFrame f) {
        ArrayList<Mapa> mapas = carregarMapList();
        JList<Mapa> listaMapa = new JList<>(new ListData<>(mapas));
        listaMapa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JFrame selec = new JFrame("Excluir Mapa");
        selec.addWindowListener(new event(f));
        selec.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        selec.setLocationRelativeTo(null);
        JPanel linha1, linha2, linha3;
        linha1 = new JPanel();
        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
        linha1.add(Box.createHorizontalGlue());
        linha1.add(new JLabel("Selecione o mapa a ser excluido"));
        linha1.add(Box.createHorizontalGlue());

//        lista = new JPanel();
//        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
//        lista.add(new JLabel("ID - Nome - tamanho"));
//        lista.add(Box.createVerticalStrut(10));
//        lista.add(new JScrollPane(listaMapa));
        linha2 = new JPanel();
        linha2.setBorder(BorderFactory.createTitledBorder("ID - Nome - tamanho"));
        linha2.setLayout(new BoxLayout(linha2, BoxLayout.X_AXIS));
        linha2.add(Box.createHorizontalGlue());
        linha2.add(new JScrollPane(listaMapa));
        linha2.add(Box.createHorizontalGlue());
        selec.setSize(200, 300);

        JButton ok, cancel;
        ok = new JButton("OK");
        ok.addActionListener((ActionEvent e) -> {
            if (listaMapa.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "Voce deve selecionar um mapa para Jogar!");
            } else {
                jogar(selec, mapas.get(listaMapa.getSelectedIndex()));
            }

        });
        cancel = new JButton("CANCELAR");
        cancel.addActionListener((ActionEvent e) -> {
            f.setVisible(true);
            selec.dispose();
        });
        linha3 = new JPanel();
        linha3.setLayout(new BoxLayout(linha3, BoxLayout.X_AXIS));
        linha3.add(Box.createHorizontalGlue());
        linha3.add(ok);
        linha3.add(Box.createHorizontalStrut(10));
        linha3.add(cancel);
        linha3.add(Box.createHorizontalGlue());

        JPanel painelEx = new JPanel();
        painelEx.setLayout(new BoxLayout(painelEx, BoxLayout.Y_AXIS));
        painelEx.add(linha1);
        painelEx.add(linha2);
        painelEx.add(linha3);

        selec.setLocationRelativeTo(null);
        selec.add(painelEx);
        selec.setVisible(true);
    }

//    private static void carregarTexturas(){
//        texturas = new ArrayList<>();
//        File f = new File("modelos" + File.separator + "paredes");
//        File[] imageFiles = f.listFiles();
//        for(File aux: imageFiles){
//            try{
//                texturas.add(lerImagem(aux));
//            }
//            catch(IOException e){
//                System.exit(1);
//            }
//        }
//    }
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
