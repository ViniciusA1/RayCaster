/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projeto.raycaster;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.PlainDocument;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

/**
 *
 * @author bruno
 */
public class MapEditorMenu {
    private static final ArrayList<Mapa> mapas = new ArrayList<>();
    private static Mapa mapaSelecionado = null;
    
    public static void inicia(){
        SwingUtilities.invokeLater(() -> {
            mapEditorOp();
        });
        
    }
    
    private static void mapEditorOp(){
        JFrame inicial = new JFrame("Editor de mapa");
        inicial.setSize(200, 300);
        JPanel painel = new JPanel();
        JButton b1, b2, b3, b4;
        b1 = new JButton("Criar Novo Mapa");
        b1.addActionListener((ActionEvent e) -> {
            inicial.setVisible(false);
            criarMapa(inicial);
        });
        b2 = new JButton("Editar Mapa");
        b2.addActionListener((ActionEvent e) ->{
            //editor de mapa
            naoImplementadoPopUp();
        });
        
        b3 = new JButton("Apagar mapa");
        b3.addActionListener((ActionEvent e) -> {
            excluiMapa(inicial);
        });
        
        painel.add(Box.createVerticalGlue());
        painel.add(b1);
        painel.add(Box.createVerticalGlue());
        painel.add(b2);
        painel.add(Box.createVerticalGlue());
        painel.add(b3);
        painel.add(Box.createVerticalGlue());
        inicial.add(painel);
        inicial.setVisible(true);
    }
    
    
    private static void criarMapa(JFrame f){
        JFrame janela = new JFrame("Criar novo mapa");
        janela.setSize(300, 200);
        JPanel coluna, linha1, linha2, linha3, linha4;
        JLabel cabecalho, nomeLabel, tamanhoLabel;
        cabecalho = new JLabel("Criar Novo Mapa");
        nomeLabel = new JLabel("Nome do Mapa: ");
        tamanhoLabel = new JLabel("Tamanho do Mapa (min: 10): ");
        linha1 = new JPanel();
        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
        linha1.add(Box.createHorizontalGlue());
        linha1.add(cabecalho);
        linha1.add(Box.createHorizontalGlue());

        JTextField nomeCampo = new JTextField(30);
        linha2 = new JPanel();
        linha2.setLayout(new BoxLayout(linha2, BoxLayout.X_AXIS));
        linha2.add(Box.createHorizontalStrut(20));
        linha2.add(nomeLabel);
        linha2.add(Box.createHorizontalStrut(10));
        linha2.add(nomeCampo);
        linha2.add(Box.createHorizontalStrut(20));

        JTextField tamanhoCampo = new JTextField(30);
        linha3 = new JPanel();
        linha3.setLayout(new BoxLayout(linha3, BoxLayout.X_AXIS));
        linha3.add(Box.createHorizontalStrut(20));
        linha3.add(tamanhoLabel);
        linha3.add(Box.createHorizontalStrut(10));
        linha3.add(tamanhoCampo);
        linha3.add(Box.createHorizontalStrut(20));
        
        
        JButton ok, cancel;
        ok = new JButton("OK");
        ok.addActionListener((ActionEvent e) ->{
            if(nomeCampo.getText().isBlank()  ||  tamanhoCampo.getText().isBlank()){
                JOptionPane.showMessageDialog(null,"Os parametros não podem estar em branco");
            }
            else if(jaExiste(nomeCampo.getText())){
                JOptionPane.showMessageDialog(null,"Já existe Mapa com esse nome");
            }
            else if(Integer.parseInt( (String) (tamanhoCampo.getText()) ) < 10){
                JOptionPane.showMessageDialog(null,"O tamanho deve ser maior ou igual a 10");
            }
            else{
                Mapa map = new Mapa(nomeCampo.getText(), Integer.parseInt((String)(tamanhoCampo.getText())));
                mapas.add(map);
                mapaSelecionado = map;
                janela.setVisible(false);
                //mapEditor(f);
                f.setVisible(true);
            }
        });
        cancel = new JButton("CANCELAR");
        cancel.addActionListener((ActionEvent e) ->{
            mapaSelecionado = null;
            janela.setVisible(false);
            nomeCampo.setText("");
            tamanhoCampo.setText("");
            f.setVisible(true);
        });
        linha4 = new JPanel();
        linha4.setLayout(new BoxLayout(linha4, BoxLayout.X_AXIS));
        linha4.add(Box.createHorizontalGlue());
        linha4.add(ok);
        linha4.add(Box.createHorizontalStrut(10));
        linha4.add(cancel);
        linha4.add(Box.createHorizontalGlue());

        coluna = new JPanel();
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.add(Box.createVerticalStrut(10));
        coluna.add(linha1);
        coluna.add(Box.createVerticalStrut(10));
        coluna.add(linha2);
        coluna.add(Box.createVerticalStrut(10));
        coluna.add(linha3);
        coluna.add(Box.createVerticalStrut(10));
        coluna.add(linha4);
        coluna.add(Box.createVerticalStrut(10));

        PlainDocument doc = (PlainDocument) tamanhoCampo.getDocument();
        doc.setDocumentFilter(new IntFilter());
        
        
        janela.add(coluna);
        janela.setVisible(true);
        
        
        
        
    }
    
    private static boolean jaExiste(String nomeMapa){
        if(nomeMapa.isBlank())
            return false;
        for(Mapa aux : mapas){
            if(nomeMapa.equals(aux.getNomeMapa())){
                return true;
            }
        }
        return false;
    }
    
    public static void excluiMapa(JFrame f){
        JList<Mapa> listaMapa = new JList<>(new ListData(mapas));
        JFrame exclui = new JFrame("Excluir Mapa");
        JPanel linha1, linha2, linha3;
        linha1 = new JPanel();
        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
        linha1.add(new JLabel("Selecione o mapa a ser excluido"));
        exclui.setSize(200, 300);
        JPanel painelEx = new JPanel();
        painelEx.setLayout(new BoxLayout(painelEx, BoxLayout.Y_AXIS));
        painelEx.add(new JScrollPane(listaMapa));
        exclui.add(painelEx);
        exclui.setVisible(true);
    }
    
    private static void naoImplementadoPopUp(){
        JOptionPane.showMessageDialog(null, "Essa opção ainda não foi implementada");
    }
}



class ListData extends AbstractListModel {
    ArrayList<Mapa> mapas;

    public ListData(ArrayList<Mapa> mapas) {
        this.mapas = mapas;
    }

    @Override
    public int getSize() {
      return mapas.size();
    }

    @Override
    public Object getElementAt(int index) {
      return index + " - " + mapas.get(index).getNomeMapa() + " - " + mapas.get(index).getLimite();
    }
}
