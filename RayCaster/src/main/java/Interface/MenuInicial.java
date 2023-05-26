/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interface;

import com.projeto.raycaster.Engine;
import com.projeto.raycaster.Mapa;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author bruno
 */
public class MenuInicial {
    private static BufferedImage imagem;
    private static ArrayList<BufferedImage> texturas;
    public static void inicia(){
        try {
            imagem = lerImagem("ImgemInicial.png");
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível encontrar o arquivo " + "parede.JPG" + ".", "Visualizador", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível ler o arquivo " + "parede.JPG" + ".", "Visualizador", JOptionPane.ERROR_MESSAGE);
            return;
        }
        carregarTexturas();
        SwingUtilities.invokeLater(() -> {
            MenuInicial();
        });
        
    }
    
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
                g.drawImage(imagem, 0, 0, imagem.getWidth(), imagem.getHeight(), null);
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
            MapEditorMenu.inicia(inicial, texturas);
        });
//        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
//        linha1.add(Box.createVerticalGlue());
//        linha1.add(b1);
//        linha1.add(Box.createVerticalGlue());
        
        b2 = new JButton("Jogar");
        b2.addActionListener((ActionEvent e) ->{
            jogar(inicial);
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
    
    private static void jogar(JFrame f){
        JFrame janela = new JFrame();
            janela.setTitle("RayCaster");
            janela.setSize(800, 600);
            janela.addWindowListener(new MapEditorMenu.event(f));
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            janela.setResizable(false);

            Engine game = new Engine(800, 600);

            janela.add(game);
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
    }
    
    private static void carregarTexturas(){
        texturas = new ArrayList<>();
        File f = new File("modelos" + File.separator + "paredes");
        File[] imageFiles = f.listFiles();
        for(File aux: imageFiles){
            try{
                texturas.add(lerImagem(aux));
            }
            catch(IOException e){
                System.exit(1);
            }
        }
    }
    
    private static BufferedImage lerImagem(String s) throws FileNotFoundException, IOException {
        return lerImagem(new File(s));
    }

    private static BufferedImage lerImagem(File f) throws FileNotFoundException, IOException {
        if (!f.exists()) throw new FileNotFoundException();
        return ImageIO.read(f);
    }
}
