package com.raycaster.interfaces;


import com.raycaster.engine.Textura;
import static com.raycaster.interfaces.MenuInicial.lerImagem;
import com.raycaster.mapa.MapGroup.ListData;
import com.raycaster.mapa.Mapa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.PlainDocument;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Classe que cria e executa as intefaces grafica referentes ao editor de mapas 
 * @author Vinicius Augusto
 * @author Bruno Zara
 */
public class MapEditorMenu {
    private static ArrayList<Mapa> mapas;
    private static Mapa novoMapa;
    
    /**
     * Metodo que carrega os mapas salvos e inicia o Editor de Mapas em uma thread especifica
     * @param f Frame da janela anterior
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    public static void inicia(JFrame f){
        mapas = Mapa.carregarMapList();
        novoMapa = null;
        SwingUtilities.invokeLater(() -> {
            mapEditorOp(f);
        });
        
        
    }
    
    
    private static void mapEditor(JFrame f){
        ArrayList<Textura> texturas = Textura.carregaTexturas(new File("modelos" + File.separator + "paredes"));
        double[] zoomFactor = new double[1];
        zoomFactor[0] = 1;
        int[] selecionado = new int[1];
        selecionado[0] = -1;
        int[] constJSP = new int[1];
        constJSP[0] = 0;
        Mapa[] mapa = new Mapa[1];
        mapa[0] = null;
                
        int[] x = new int[2];
        int[] y = new int[2];
        JFrame editor = new JFrame("Editor de mapas");
        editor.setSize(800, 600);
        
        JPanel grid = new JPanel(){
                   
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                g2.translate(this.getWidth()/2, this.getHeight()/2);
                if(mapa[0] == null){
                    g2.setColor(Color.WHITE);
                    g2.drawString("Selecione um Mapa!!", 0, 0);
                    return;
                }
                
                Path2D path = new Path2D.Double();
                int bloco = (int) (64 * zoomFactor[0]);
                int xi = -(mapa[0].getLimite()/2)*bloco;
                int yi = -(mapa[0].getLimite()/2)*bloco;
                for(int i = 0; i<mapa[0].getLimite(); i++){
                    for(int j = 0 ; j<mapa[0].getLimite(); j++){
                        int id = mapa[0].getID(i, j);
                        if((this.getWidth()/2 <= ((Math.abs(mapa[0].getLimite())/2) + i + constJSP[0])*bloco + x[0]  && this.getHeight()/2 <= ((Math.abs(mapa[0].getLimite()/2) + j) *bloco + y[0])))
                            if(id > 0){
                                Textura textura = Textura.getTextura(texturas, id);
                                BufferedImage imagem = textura.getRGB();
                                g2.drawImage(imagem, xi + x[0] + i*bloco, yi  + y[0] + j*bloco, bloco, bloco, null);
                            }
                    }
                }
                
                
                
                g2.setColor(Color.WHITE);
                for(int i = 0; i <= mapa[0].getLimite(); i++){
                    path.moveTo(xi + x[0] + i*bloco, yi  + y[0]);
                    path.lineTo(xi + x[0] + i*bloco, -yi  + y[0]);
                    path.closePath();
                }
                
                for(int i = 0; i <= mapa[0].getLimite() ; i++){
                    path.moveTo(xi + x[0], yi  + y[0] + i*bloco);
                    path.lineTo(-xi + x[0], yi  + y[0] + i*bloco);
                    path.closePath();
                }
                
                g2.draw(path);
                g2.setColor(Color.BLUE);
                g2.fillOval(xi + x[0] + mapa[0].getSpawnX() * bloco, yi + y[0] + mapa[0].getSpawnY() * bloco, bloco, bloco);
                
                
            }
        };
        
        
        class ListPanel extends JPanel {

            private static final int N = 5;
            private DefaultListModel dlm = new DefaultListModel();
            private JList list = new JList(dlm);

            public ListPanel() {
                super(new GridLayout());
                dlm.addElement("Player Spawn Point");
                dlm.addElement("0 - limpar");
                for (Textura t: texturas){
                    dlm.addElement(t.toString());
                }
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                list.setVisibleRowCount(N);
                list.setCellRenderer(new ListRenderer());
                list.addListSelectionListener(new SelectionHandler());
                this.add(list);
            }
            
            private class ListRenderer extends DefaultListCellRenderer {

                
                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus);
                    label.setBorder(BorderFactory.createEmptyBorder(N, N, N, N));
                    if(label.getText().equals("Player Spawn Point")){
                        
                    }
                    else{
                       String[] s = label.getText().split(" - ");
                        int id = Integer.parseInt(s[0]);
                        for(Textura aux: texturas){
                            if(aux.getID() == id){
                                label.setIcon(new ImageIcon(aux.getRGB()));
                                break;
                            }
                        } 
                    }
                    
                    
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.BOTTOM);
                    return label;
//                    TextureBox tb = (TextureBox) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus);;
//                    return tb;
                }
                
                
            }
            
            

            private class SelectionHandler implements ListSelectionListener {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        String label = (String) dlm.getElementAt(list.getSelectedIndex());
                        if(label.equals("Player Spawn Point")){
                            selecionado[0] = -1;
                        }
                        else{
                            String[] s = label.split(" - ");
                            selecionado[0] = Integer.parseInt(s[0]);
                        }
                        
                    }
                    
                }
            }
        }
        
        
        
        ListPanel lista = new ListPanel();
        
        
        JSplitPane jsp = new JSplitPane();
        jsp.setLeftComponent(new JScrollPane(lista));
        jsp.setRightComponent(grid);
        constJSP[0] = jsp.getDividerLocation() +10;
        jsp.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, (PropertyChangeEvent pce) -> {
            constJSP[0] = jsp.getDividerLocation() +10;
        });
        
        // método novo para mover a grid
        MouseAdapter adaptador = new MouseAdapter() {
            private int anteriorX;
            private int anteriorY;
            private int botao;
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if(botao == 3){
                    int atualX = e.getX();
                    int atualY = e.getY();

                    x[0] += (atualX - anteriorX);
                    y[0] += (atualY - anteriorY);

                    anteriorX = atualX;
                    anteriorY = atualY;
                }
                else if(botao== 1){
                    mouseClicked(e);
                }

                
                
                
                grid.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                anteriorX = e.getX();
                anteriorY = e.getY();
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e){
                int movimento = e.getWheelRotation();
                if(movimento > 0 && zoomFactor[0] < 6){
                    zoomFactor[0] += 0.1 * movimento;
                    grid.repaint();
                }
                else if(movimento < 0  && zoomFactor[0] > 0.5){
                    zoomFactor[0] += 0.1 * movimento;
                    grid.repaint();
                }
                
            }
            
            @Override
            public void mouseClicked(MouseEvent e){
                if(mapa[0] == null)
                    return;
                x[1] = e.getX();
                y[1] = e.getY();
                
                Dimension d = grid.getSize();
                Point p = grid.getLocation();
                x[1] = (x[1] - p.x) + jsp.getDividerLocation() +10;
                y[1] = (y[1] - p.y) ;
                x[1] = x[1] -d.width/2;
                y[1] = y[1] -d.height/2;
                int bloco = (int) (64 * zoomFactor[0]);
                //JOptionPane.showMessageDialog(null, "X = " + (x[1] -x[0] +(mapa.getLimite()/2) * bloco)/bloco + ", y = " + (y[1] -y[0] +(mapa.getLimite()/2) * bloco)/bloco);
                int aux = selecionado[0];
                int xi = (x[1] -x[0] +(mapa[0].getLimite()/2) * bloco)/bloco;
                int yi = (y[1] -y[0] +(mapa[0].getLimite()/2) * bloco)/bloco;
                if(xi > mapa[0].getLimite() || xi < 0 || yi > mapa[0].getLimite() || yi < 0){
                    return;
                }
                if(aux == -1){
                    if(mapa[0].getID(xi, yi) == 0)
                        mapa[0].setSpawn(xi, yi);
                    else
                        JOptionPane.showMessageDialog(null, 
                                "Você não pode colocar o player na parede!");
                }
                else{
                    if(xi == mapa[0].getSpawnX()  &&  yi == mapa[0].getSpawnY())
                        JOptionPane.showMessageDialog(null, 
                                "Você não pode colocar uma parede em cima do player!");
                    else
                        mapa[0].setValor(xi, yi, aux);
                }
                grid.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                botao = e.getButton();
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                botao = 0;
            }
        };
        
        JMenuBar barra = new JMenuBar();
        
        DefaultComboBoxModel dcm = new DefaultComboBoxModel<Mapa>();
        dcm.addElement("-- Selecione um Mapa --");
        for(Mapa aux : mapas){
            dcm.addElement(aux);
        }
        
        JComboBox mapSelector = new JComboBox(dcm);
        mapSelector.addActionListener((ActionEvent e) -> {
            if(mapSelector.getSelectedItem() instanceof String){
                mapa[0] = null;
            }
            else{
                mapa[0] = mapas.get(mapSelector.getSelectedIndex() - 1);
            }
            grid.repaint();
        });
        
        JButton salvar = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
        salvar.addActionListener((ActionEvent e) -> {
            for(Mapa aux: mapas){
                try {
                    aux.salvar();
                }
                catch(IOException exe){
                    JOptionPane.showMessageDialog(null, 
                            "Erro ao salvar os mapas!");
                    return;
                }
            }
            
            JOptionPane.showMessageDialog(null, 
                    "Mapas salvos com sucesso!");
        });
        BufferedImage plus;
        BufferedImage trash;
        try{
            plus = lerImagem("modelos" + File.separator + "icones" + File.separator + "plus.png");
            trash = lerImagem("modelos" + File.separator + "icones" + File.separator + "trash.png");
        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Erro ao ler os icones");
            plus = null; trash = null;
        }
        JButton criarMapa = new JButton(new ImageIcon(plus));
        criarMapa.addActionListener(((e) ->{
            criarMapa();
            if(novoMapa != null){
                dcm.addElement(novoMapa);
                dcm.setSelectedItem(novoMapa);
            }
        }));

        
        barra.add(criarMapa);
        barra.add(mapSelector);
        barra.add(salvar);
        
        grid.addMouseListener(adaptador);
        grid.addMouseWheelListener(adaptador);
        grid.addMouseMotionListener(adaptador);
        
        editor.add(jsp);
        editor.setJMenuBar(barra);
        editor.addWindowListener(new event(f));
        editor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editor.setVisible(true);
        
        
    }
    
    private static void mapEditorOp(JFrame f){
        JFrame inicial = new JFrame("Editor de mapa");
        
        inicial.setSize(200, 200);
        inicial.setLocationRelativeTo(null);
        JPanel painel = new JPanel(), linha1 = new JPanel(), linha2 = new JPanel(), linha3 = new JPanel(), linha4 = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        JButton b1, b2, b3, b4;
        b1 = new JButton("Criar Novo Mapa");
        b1.addActionListener((ActionEvent e) -> {
            inicial.setVisible(false);
            criarMapa();
        });
        linha1.setLayout(new BoxLayout(linha1, BoxLayout.X_AXIS));
        linha1.add(Box.createVerticalGlue());
        linha1.add(b1);
        linha1.add(Box.createVerticalGlue());
        
        b2 = new JButton("Editar Mapa");
        b2.addActionListener((ActionEvent e) ->{
            //editor de mapa
            mapEditor(inicial);
            //naoImplementadoPopUp();
        });
        linha2.setLayout(new BoxLayout(linha2, BoxLayout.X_AXIS));
        linha2.add(Box.createVerticalGlue());
        linha2.add(b2);
        linha2.add(Box.createVerticalGlue());
        
        b3 = new JButton("Apagar mapa");
        b3.addActionListener((ActionEvent e) -> {
            inicial.setVisible(false);
            excluiMapa(inicial);
        });
        linha3.setLayout(new BoxLayout(linha3, BoxLayout.X_AXIS));
        linha3.add(Box.createVerticalGlue());
        linha3.add(b3);
        linha3.add(Box.createVerticalGlue());
        
        b4 = new JButton("Voltar");
        b4.addActionListener((ActionEvent e) ->{
            f.setVisible(true);
            inicial.dispose();
        });
        linha4.setLayout(new BoxLayout(linha4, BoxLayout.X_AXIS));
        linha4.add(Box.createVerticalGlue());
        linha4.add(b4);
        linha4.add(Box.createVerticalGlue());
        
        painel.add(Box.createVerticalStrut(10));
        painel.add(linha1);
        painel.add(Box.createVerticalStrut(10));
        painel.add(linha2);
        painel.add(Box.createVerticalStrut(10));
        painel.add(linha3);
        painel.add(Box.createVerticalStrut(20));
        painel.add(linha4);
        painel.add(Box.createVerticalStrut(10));
        inicial.add(painel);
        inicial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicial.addWindowListener(new event(f));
        inicial.setVisible(true);
    }
    
    
    private static void criarMapa(){
        JFrame janela = new JFrame("Criar novo mapa");
        janela.setDefaultCloseOperation(janela.DISPOSE_ON_CLOSE);
        janela.setSize(300, 200);
        janela.setLocationRelativeTo(null);
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
                novoMapa = map;
                try {
                    map.salvar();
                } catch (IOException ex) {
                    Logger.getLogger(MapEditorMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                janela.dispose();
            }
        });
        cancel = new JButton("CANCELAR");
        cancel.addActionListener((ActionEvent e) ->{
//            janela.setVisible(false);
//            nomeCampo.setText("");
//            tamanhoCampo.setText("");
            novoMapa = null;
            janela.dispose();
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
        
        janela.setResizable(false);
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
    
    private static void excluiMapa(JFrame f){
        JList<Mapa> listaMapa = new JList<>(new ListData<>(mapas));
        listaMapa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JFrame exclui = new JFrame("Excluir Mapa");
        exclui.addWindowListener(new event(f));
        exclui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        exclui.setLocationRelativeTo(null);
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
        exclui.setSize(200, 300);
        
        
        JButton ok, cancel;
        ok = new JButton("OK");
        ok.addActionListener((ActionEvent e) ->{
            if(listaMapa.isSelectionEmpty()){
                JOptionPane.showMessageDialog(null, "Voce deve selecionar um mapa para excluir!");
            }
            else if(JOptionPane.showConfirmDialog(null, "Voce tem certeza que deseja continuar?") == 0){
                int index= listaMapa.getSelectedIndex();
                Mapa aux  = mapas.get(index);
                mapas.remove(aux);
                aux.excluir();
                f.setVisible(true);
                exclui.dispose();
            }
            
        });
        cancel = new JButton("CANCELAR");
        cancel.addActionListener((ActionEvent e) ->{
//            janela.setVisible(false);
//            nomeCampo.setText("");
//            tamanhoCampo.setText("");
            f.setVisible(true);
            exclui.dispose();
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
        
        exclui.add(painelEx);
        exclui.setVisible(true);
    }
    
    
    
    
    /**
     * Classe auxiliar que serve para ler os eventos da janela e executar comandos dependendo da ação tomada
     * @author Vinicius Augusto
     * @author Bruno Zara
     */
    static class event extends WindowAdapter{
        JFrame f;

        /**
         * Contrutor do evento 
         * @param f referencia da janela anterior
         * @author Vinicius Augusto
         * @author Bruno Zara
         */
        event(JFrame f){
            this.f = f;
        }
        
        /**
         * Metodo executado quando a janela atual é aberta
         * @param e
         * @author Vinicius Augusto
         * @author Bruno Zara
         */
        @Override
        public void windowOpened(WindowEvent e){
            if(f != null)
                f.setVisible(false);
        }

        /**
         * Metodo executado quando a janela atual é fechada
         * @param e 
         * @author Vinicius Augusto
         * @author Bruno Zara
         */
        @Override
        public void windowClosed(WindowEvent e){
            if(f == null){
                System.exit(0);
            }
            f.setVisible(true);
        }
    }
    
    
    

}
