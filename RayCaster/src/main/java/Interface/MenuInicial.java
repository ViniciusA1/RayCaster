/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interface;

import com.projeto.raycaster.Engine;
import com.projeto.raycaster.Mapa;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author bruno
 */
public class MenuInicial {
        public static void inicia(){
        SwingUtilities.invokeLater(() -> {
            MenuInicial();
        });
        
    }
    
    private static void MenuInicial(){
        JFrame inicial = new JFrame("Menu inicial");
        
        inicial.setSize(250, 230);
        inicial.setResizable(false);
        inicial.setLocationRelativeTo(null);
        JPanel painel = new JPanel(), linha0 = new JPanel(), linha1 = new JPanel(), linha2 = new JPanel(), linha3 = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        
        linha0.setLayout(new BoxLayout(linha0, BoxLayout.X_AXIS));
        linha0.setBorder(BorderFactory.createTitledBorder("Bem Vindo Ao (*Nome do jogo aqui*)"));
        linha0.add(Box.createHorizontalGlue());
        linha0.add(new JLabel("Selecione uma opção"));
        linha0.add(Box.createHorizontalGlue());
        JButton b1, b2, b3;
        b1 = new JButton("Editor de mapas");
        b1.addActionListener((ActionEvent e) -> {
            inicial.setVisible(false);
            MapEditorMenu.inicia(inicial);
        });
        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
        linha1.add(Box.createVerticalGlue());
        linha1.add(b1);
        linha1.add(Box.createVerticalGlue());
        
        b2 = new JButton("Jogar");
        b2.addActionListener((ActionEvent e) ->{
            jogar(inicial);
            inicial.setVisible(false);
        });
        linha2.setLayout(new BoxLayout(linha2, BoxLayout.X_AXIS));
        linha2.add(Box.createVerticalGlue());
        linha2.add(b2);
        linha2.add(Box.createVerticalGlue());
        
        
        b3 = new JButton("Sair");
        b3.addActionListener((ActionEvent e) ->{
            System.exit(0);//Temporario
        });
        linha3.setLayout(new BoxLayout(linha3, BoxLayout.X_AXIS));
        linha3.add(Box.createVerticalGlue());
        linha3.add(b3);
        linha3.add(Box.createVerticalGlue());
        
        painel.add(Box.createVerticalStrut(10));
        painel.add(linha0);
        painel.add(Box.createVerticalStrut(10));
        painel.add(linha1);
        painel.add(Box.createVerticalStrut(10));
        painel.add(linha2);
        painel.add(Box.createVerticalStrut(20));
        painel.add(linha3);
        painel.add(Box.createVerticalStrut(10));
        inicial.add(painel);
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
}
