/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raycaster.interfaces;

import com.raycaster.engine.Engine;
import com.raycaster.interfaces.MapEditorMenu.event;
import com.raycaster.mapa.MapGroup.ListData;
import com.raycaster.mapa.Mapa;
import static com.raycaster.mapa.Mapa.carregarMapList;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
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
import javax.swing.SwingUtilities;

/**
 * Classe que cria o menu inicial
 * 
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MenuInicial {
    private static BufferedImage imagem;
    //private static ArrayList<BufferedImage> texturas;
    
    /**
     * Método para iniciar o jogo em uma thread apropriada
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public static void inicia(){
        try {
            imagem = lerImagem("ImagemInicial.png");
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível encontrar o arquivo " + "parede.JPG" + ".", "Visualizador", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível ler o arquivo " + "parede.JPG" + ".", "Visualizador", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //carregarTexturas();
        SwingUtilities.invokeLater(() -> {
            MenuInicial();
        });
        
    }
    
    /**
     * Metodo que cria e mostra a interface grafica responsavel pelo menu inicial
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    private static void MenuInicial(){
        JFrame inicial = new JFrame("Menu inicial");
        JPanel coluna = new JPanel();
        coluna.setLayout(new BorderLayout());
        
        inicial.setSize(800, 600);
        inicial.setResizable(false);
        inicial.setLocationRelativeTo(null);
        JPanel imagemPane = new JPanel() {
            @Override
            public void paintComponent(Graphics g){
                g.drawImage(imagem, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        imagemPane.setSize(800, 500);
        //inicial.add(Box.createVerticalStrut(500));
        coluna.add(imagemPane, BorderLayout.CENTER);
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.X_AXIS));
        
//        linha0.setLayout(new BoxLayout(linha0, BoxLayout.X_AXIS));
//        linha0.setBorder(BorderFactory.createTitledBorder("Bem Vindo Ao (*Nome do jogo aqui*)"));
//        linha0.add(Box.createHorizontalGlue());
//        linha0.add(new JLabel("Selecione uma opção"));
//        linha0.add(Box.createHorizontalGlue());
        JButton b1, b2, b3;
        b1 = new JButton("Editor de mapas");
        b1.addActionListener((ActionEvent e) -> {
            inicial.setVisible(false);
            MapEditorMenu.inicia(inicial);
        });
//        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
//        linha1.add(Box.createVerticalGlue());
//        linha1.add(b1);
//        linha1.add(Box.createVerticalGlue());
        
        b2 = new JButton("Jogar");
        b2.addActionListener((ActionEvent e) ->{
            selecionaMapa(inicial);
            inicial.setVisible(false);
        });
//        linha2.setLayout(new BoxLayout(linha2, BoxLayout.X_AXIS));
//        linha2.add(Box.createVerticalGlue());
//        linha2.add(b2);
//        linha2.add(Box.createVerticalGlue());
        
        
        b3 = new JButton("Sair");
        b3.addActionListener((ActionEvent e) ->{
            System.exit(0);//Temporario
        });
//        linha3.setLayout(new BoxLayout(linha3, BoxLayout.X_AXIS));
//        linha3.add(Box.createVerticalGlue());
//        linha3.add(b3);
//        linha3.add(Box.createVerticalGlue());
        
//        painel.add(Box.createVerticalStrut(10));
//        painel.add(linha0);
//        painel.add(Box.createVerticalStrut(10));
//        painel.add(linha1);
//        painel.add(Box.createVerticalStrut(10));
//        painel.add(linha2);
//        painel.add(Box.createVerticalStrut(20));
//        painel.add(linha3);
//        painel.add(Box.createVerticalStrut(10));
        painel.setSize(800, 100);
        painel.setBorder(BorderFactory.createTitledBorder("Bem Vindo Ao (*Nome do jogo aqui*), Seelcione uma opção" ));
        painel.add(Box.createHorizontalStrut(20));
        painel.add(b3);
        painel.add(Box.createHorizontalGlue());
        painel.add(b1);
        painel.add(Box.createHorizontalStrut(20));
        painel.add(b2);
        painel.add(Box.createHorizontalStrut(20));
        //coluna.add(Box.createVerticalGlue());
        coluna.add(painel, BorderLayout.PAGE_END);
        inicial.add(coluna);
        inicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicial.setVisible(true);
    }
    
    /**
     * Metodo que inicia o jogo
     * @param f Janela anterior que vai ser reaberta quando o jogo fechar
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    private static void jogar(JFrame f, Mapa map){
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
    
    private static void selecionaMapa(JFrame f){
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
        ok.addActionListener((ActionEvent e) ->{
            if(listaMapa.isSelectionEmpty()){
                JOptionPane.showMessageDialog(null, "Voce deve selecionar um mapa para Jogar!");
            }
            else {
                jogar(selec, mapas.get(listaMapa.getSelectedIndex()));
            }
            
        });
        cancel = new JButton("CANCELAR");
        cancel.addActionListener((ActionEvent e) ->{
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
     * @return  BufferedImage contendo a imagem que esta no diretorio especificado ou null se o diretorio estar vazio
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
     * @return  BufferedImage contendo a imagem que esta no diretorio especificado ou null se o diretorio estar vazio
     * @throws FileNotFoundException
     * @throws IOException 
     * 
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    private static BufferedImage lerImagem(File f) throws FileNotFoundException, IOException {
        if (!f.exists()) throw new FileNotFoundException();
        return ImageIO.read(f);
    }
}
