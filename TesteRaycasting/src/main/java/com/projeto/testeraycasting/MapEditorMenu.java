/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.testeraycasting;
import java.awt.event.ActionEvent;
import java.util.EventListener;
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
public class MapEditorMenu {
    
    public static void inicia(){
        SwingUtilities.invokeLater(() -> {
            naoImplementadoPopUp();
        });
        
    }
    
    
    
    
    private static void naoImplementadoPopUp(){
        JFrame ni = new JFrame("Não implementado Ainda");
        ni.setSize(300, 100);
        JPanel painel = new JPanel();
        JPanel linha1, linha2;
        JLabel label = new JLabel("Essa opção não foi implementada ainda");
        JButton ok = new JButton("ok");
        ok.addActionListener((ActionEvent e) ->{
            ni.setVisible(false);
        });
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        linha1 = new JPanel();
        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
        linha2 = new JPanel();
        linha2.setLayout(new BoxLayout(linha2, BoxLayout.X_AXIS));
       
        linha1.add(Box.createHorizontalGlue());
        linha1.add(label);
        linha1.add(Box.createHorizontalGlue());
        
        linha2.add(Box.createHorizontalGlue());
        linha2.add(ok);
        linha2.add(Box.createHorizontalGlue());
        painel.add(linha1);
        painel.add(linha2);
        ni.add(painel);
        ni.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ni.setVisible(true);
    }
}
