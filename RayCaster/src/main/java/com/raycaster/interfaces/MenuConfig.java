package com.raycaster.interfaces;

import com.raycaster.interfaces.LabelAnimado.Animacao;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author vinicius
 */
public class MenuConfig extends Painel {

    private final JFrame frame;
    private LabelAnimado labelConfig;
    private BotaoCustom botaoVoltar;
    private Image background;
    
    public MenuConfig(JFrame frame, Image background, Font fonte) {
        this.frame = frame;
        this.background = background;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        carregaComponentes(fonte);
    }
    
    public void setImagem(Image background) {
        this.background = background;
    }
    
    private void carregaComponentes(Font fonte) {
        labelConfig = new LabelAnimado("Configuração", 
                fonte.deriveFont(Font.BOLD, 150f),
                Animacao.FLOAT);
        
        botaoVoltar = new BotaoCustom("Voltar", 
                fonte, () -> voltar());
        
        add(labelConfig);
        add(botaoVoltar);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        super.paintComponent(g);
        
        g.drawImage(background, 0, 0, getWidth(),
                getHeight(), this);
    }
    
    private void voltar() {
        frame.remove(this);
        InterfaceManager.pop();
        frame.add(InterfaceManager.peek());
        frame.repaint();
        frame.revalidate();
    }
    
    @Override
    public void entrar() {
        super.entrar();
        
        SwingUtilities.invokeLater(() -> {
            botaoVoltar.requestFocusInWindow();
        });
    }
}
